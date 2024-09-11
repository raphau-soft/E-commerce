package com.devraf.e_commerce.controller;

import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.utils.TokenEnum;
import com.devraf.e_commerce.utils.exception.UserNotActiveException;
import com.devraf.e_commerce.utils.payload.login.LoginRequest;
import com.devraf.e_commerce.utils.payload.login.LoginResponse;
import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import com.devraf.e_commerce.rabbit.SignUpProducer;
import com.devraf.e_commerce.security.service.JwtService;
import com.devraf.e_commerce.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailsService service;

    @Autowired
    private UserService userService;

    @Autowired
    private SignUpProducer signUpProducer;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        User user = authenticateUser(loginRequest);
        LoginResponse response = buildLoginResponse(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequest signupRequest) {
        sendSignupMessage(signupRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity confirmAccount(@PathVariable("token") @NotBlank String token) {
        userService.confirmUser(token);
        return ResponseEntity.ok().build();
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
                .token(generateToken(user, TokenEnum.AUTH))
                .refreshToken(generateToken(user, TokenEnum.REFRESH))
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
        signUpProducer.sendMessage(signupRequest);
    }

}
