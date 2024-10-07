package com.devraf.e_commerce.service;

import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.db.repository.UserDAO;
import com.devraf.e_commerce.utils.TokenEnum;
import com.devraf.e_commerce.utils.exception.TokenNotValidException;
import com.devraf.e_commerce.utils.payload.signup.ConfirmAccountRequest;
import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import com.devraf.e_commerce.utils.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    public void createUser(SignupRequest request) {
        Optional<User> userOptional = userDAO.findByEmail(request.getEmail());
        if(userOptional.isEmpty()) {
            User user = buildUser(request);
            user = userDAO.save(user);
            emailService.sendEmail(user.getEmail(), "E-commerce sign up", "Your account have been created. Click link below to activate it.\n" +
                    "http://localhost:8080/e-commerce/confirm/" + jwtService.createToken(user, TokenEnum.CONFIRM_ACCOUNT_TOKEN).getToken());
        } else {
            emailService.sendEmail(userOptional.get().getEmail(), "E-commerce sign up", "Hello did you forget that you have an active account?");
        }
    }

    public void confirmUser(ConfirmAccountRequest request) {
        if (jwtService.isTokenValid(request.getToken(), TokenEnum.CONFIRM_ACCOUNT_TOKEN)) {
            String userEmail = jwtService.extractEmail(request.getToken());
            User user = userDAO.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));

            updateUserDetails(user, request);
            jwtService.deleteToken(request.getToken());
        } else {
            throw new TokenNotValidException("Token is invalid: " + request.getToken());
        }
    }

    private void updateUserDetails(User user, ConfirmAccountRequest request) {
        user.setActive(true);
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());

        userDAO.save(user);
    }

    private User buildUser(SignupRequest request) {
        return User.builder()
                .id(0L)
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .active(false)
                .role(RolesEnum.ROLE_USER.name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
