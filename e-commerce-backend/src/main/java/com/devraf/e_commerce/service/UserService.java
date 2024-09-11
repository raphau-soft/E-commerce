package com.devraf.e_commerce.service;

import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.db.repository.UserDAO;
import com.devraf.e_commerce.security.service.JwtService;
import com.devraf.e_commerce.utils.TokenEnum;
import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import com.devraf.e_commerce.utils.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
                    "http://localhost:8080/api/auth/confirm/" + jwtService.createToken(user, TokenEnum.CONFIRM_ACCOUNT).getToken());
        } else {
            emailService.sendEmail(userOptional.get().getEmail(), "E-commerce sign up", "Hello did you forget that you have an active account?");
        }
    }

    public void confirmUser(String token) {
        if(jwtService.validateToken(token)) {
            String email = jwtService.extractEmail(token);
            userDAO.activateUserByEmail(email);
            jwtService.deleteToken(token);
        }
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
