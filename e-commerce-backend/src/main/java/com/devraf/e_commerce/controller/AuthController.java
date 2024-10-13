package com.devraf.e_commerce.controller;

import com.devraf.e_commerce.db.entity.Token;
import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.payload.password.ResetPasswordRequest;
import com.devraf.e_commerce.utils.TokenEnum;
import com.devraf.e_commerce.utils.exception.TokenNotValidException;
import com.devraf.e_commerce.utils.exception.UserNotActiveException;
import com.devraf.e_commerce.payload.login.LoginRequest;
import com.devraf.e_commerce.payload.login.LoginResponse;
import com.devraf.e_commerce.payload.signup.ConfirmAccountRequest;
import com.devraf.e_commerce.payload.signup.SignupRequest;
import com.devraf.e_commerce.rabbit.QueueProducer;
import com.devraf.e_commerce.service.JwtService;
import com.devraf.e_commerce.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.devraf.e_commerce.config.QueuesConfig.FORGOT_PASSWORD_ROUTING_KEY;
import static com.devraf.e_commerce.config.QueuesConfig.SIGNUP_ROUTING_KEY;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(value = "*")
public class AuthController {

    @Autowired
    private UserDetailsService service;

    @Autowired
    private UserService userService;

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        User user = authenticateUser(loginRequest);
        addCookies(user, response, new TokenEnum[]{TokenEnum.AUTH_TOKEN, TokenEnum.REFRESH_TOKEN, TokenEnum.REMEMBER_ME_TOKEN}, loginRequest.isRememberMe());

        LoginResponse loginResponse = buildLoginResponse(user);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/remember-me")
    public ResponseEntity<LoginResponse> rememberMe(HttpServletRequest request, HttpServletResponse response) {
        String rememberMeToken = getCookieValue(request, TokenEnum.REMEMBER_ME_TOKEN.name());

        if (jwtService.isTokenValid(rememberMeToken, TokenEnum.REMEMBER_ME_TOKEN)) {
            User user = jwtService.getTokenByToken(rememberMeToken).getUser();
            addCookies(user, response, new TokenEnum[]{
                    TokenEnum.AUTH_TOKEN,
                    TokenEnum.REFRESH_TOKEN,
                    TokenEnum.REMEMBER_ME_TOKEN
            }, true);

            jwtService.deleteToken(rememberMeToken);
            return ResponseEntity.ok(buildLoginResponse(user));
        } else {
            throw new TokenNotValidException();
        }
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String csrfToken = request.getHeader(TokenEnum.CSRF_TOKEN.name());
        String refreshToken = getCookieValue(request, TokenEnum.REFRESH_TOKEN.name());

        if (validateTokens(csrfToken, refreshToken)) {
            User user = jwtService.getTokenByToken(csrfToken).getUser();
            addCookies(user, response, new TokenEnum[]{
                    TokenEnum.AUTH_TOKEN,
                    TokenEnum.REFRESH_TOKEN
            }, false);

            jwtService.deleteToken(csrfToken);
            jwtService.deleteToken(refreshToken);
            return ResponseEntity.ok(buildLoginResponse(user));
        } else {
            throw new TokenNotValidException();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestParam(name = "email") @Email @NotBlank @Size(min = 5, max = 254) String email) {
        sendForgotPasswordMessage(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequest signupRequest) {
        sendSignupMessage(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/signup/confirm")
    public ResponseEntity confirmAccount(@RequestBody @Valid ConfirmAccountRequest request) {
        userService.confirmUser(request);
        return ResponseEntity.ok().build();
    }

    private boolean validateTokens(String csrfToken, String refreshToken) {
        return jwtService.isTokenValid(csrfToken, TokenEnum.CSRF_TOKEN) &&
                jwtService.isTokenValid(refreshToken, TokenEnum.REFRESH_TOKEN);
    }

    private User authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = performAuthentication(loginRequest);
        checkIfAuthenticated(authentication);
        return fetchActiveUser(loginRequest.getEmail());
    }

    private Authentication performAuthentication(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
    }

    private void addCookies(User user, HttpServletResponse response, TokenEnum[] tokenEnums, boolean isRememberMe) {
        Arrays.stream(tokenEnums)
                .map(tokenEnum -> createCookie(user, tokenEnum, isRememberMe))
                .forEach(response::addCookie);
    }

    private Cookie createCookie(User user, TokenEnum tokenEnum, boolean isRememberMe) {
        if (!isRememberMe && tokenEnum.equals(TokenEnum.REMEMBER_ME_TOKEN)) {
            return createEmptyCookie(TokenEnum.REMEMBER_ME_TOKEN);
        }
        return createJwtCookie(user, tokenEnum);
    }

    private Cookie createEmptyCookie(TokenEnum tokenEnum) {
        return new Cookie(tokenEnum.name(), null);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Cookie createJwtCookie(User user, TokenEnum tokenEnum) {
        String tokenValue = jwtService.createToken(user, tokenEnum).getToken();
        return new Cookie(tokenEnum.name(), tokenValue);
    }

    private User fetchActiveUser(String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getActive()) {
            throw new UserNotActiveException("User is not active");
        }

        return user;
    }

    private LoginResponse buildLoginResponse(User user) {
        return LoginResponse.builder()
                .csrfToken(generateToken(user, TokenEnum.CSRF_TOKEN))
                .build();
    }

    private String generateToken(User user, TokenEnum tokenType) {
        return jwtService.createToken(user, tokenType).getToken();
    }

    private void checkIfAuthenticated(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Invalid credentials!");
        }
    }

    private void sendSignupMessage(SignupRequest signupRequest) {
        queueProducer.sendMessage(SIGNUP_ROUTING_KEY, signupRequest);
    }

    private void sendForgotPasswordMessage(String email) {
        queueProducer.sendMessage(FORGOT_PASSWORD_ROUTING_KEY, email);
    }

}
