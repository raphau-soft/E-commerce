package com.devraf.e_commerce.rabbit;

import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.devraf.e_commerce.config.QueuesConfig.SIGNUP_ROUTING_KEY;

@Service
public class SignUpProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(SignupRequest request) {
        rabbitTemplate.convertAndSend(SIGNUP_ROUTING_KEY, request);
    }

}
