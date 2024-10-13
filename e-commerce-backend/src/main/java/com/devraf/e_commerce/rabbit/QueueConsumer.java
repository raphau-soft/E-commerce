package com.devraf.e_commerce.rabbit;

import com.devraf.e_commerce.payload.signup.SignupRequest;
import com.devraf.e_commerce.service.UserService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.devraf.e_commerce.config.QueuesConfig.FORGOT_PASSWORD_ROUTING_KEY;
import static com.devraf.e_commerce.config.QueuesConfig.SIGNUP_ROUTING_KEY;

@Service
public class QueueConsumer {

    @Autowired
    private UserService userService;

    @RabbitListener(queues = SIGNUP_ROUTING_KEY)
    public void signupConsumer(SignupRequest request) {
        try {
            userService.createUser(request);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = FORGOT_PASSWORD_ROUTING_KEY)
    public void forgotPasswordConsumer(String email) {
        try {
            userService.forgotPassword(email);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
