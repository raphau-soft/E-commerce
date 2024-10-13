package com.devraf.e_commerce.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfig {

    public static final String SIGNUP_ROUTING_KEY = "sign.up.queue";
    public static final String FORGOT_PASSWORD_ROUTING_KEY = "forgot.password.queue";

    @Bean
    public Queue signUpQueue() {
        return new Queue(SIGNUP_ROUTING_KEY, false);
    }

    @Bean
    public Queue forgotPasswordQueue() {
        return new Queue(FORGOT_PASSWORD_ROUTING_KEY, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTrustedPackages("com.devraf.e_commerce.payload.signup");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

}
