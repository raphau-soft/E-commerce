package com.devraf.e_commerce.rabbit;

import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import com.devraf.e_commerce.service.UserService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.devraf.e_commerce.config.QueuesConfig.SIGNUP_ROUTING_KEY;

@Service
public class SignUpConsumer {

    @Autowired
    private UserService userService;

    @RabbitListener(queues = SIGNUP_ROUTING_KEY)
    public void handleMessage(SignupRequest request) {
        try {
            userService.createUser(request);
        } catch (DataIntegrityViolationException e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
