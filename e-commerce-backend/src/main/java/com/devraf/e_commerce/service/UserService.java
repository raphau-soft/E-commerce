package com.devraf.e_commerce.service;

import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.db.repository.UserDAO;
import com.devraf.e_commerce.payload.signup.SignupRequest;
import com.devraf.e_commerce.utils.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder encoder;

    public void createUser(SignupRequest request) {
        User user = User.builder()
                .id(1L)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .active(false)
                .role(RolesEnum.ROLE_USER.name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userDAO.save(user);
    }
}
