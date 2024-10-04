package com.devraf.e_commerce.config;

import com.devraf.e_commerce.service.JwtService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Log4j2
public class ScheduledConfig {

    @Autowired
    private JwtService jwtService;

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduleFixedDelayTask() {
        log.info("Deleting expired tokens...");
        jwtService.deleteExpiredTokens();
        log.info("Expired tokens deleted");
    }

}
