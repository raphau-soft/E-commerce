package com.devraf.e_commerce.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String key, Object request) {
        rabbitTemplate.convertAndSend(key, request);
    }

}
