package com.couponmania2.coupon_project.configuration;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    /**
     * configuring the rest template that is used to test the app
     * @param builder RestTemplateBuilder
     * @return the rest template.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3_000))
                .setReadTimeout(Duration.ofMillis(3_000))
                .build();
    }
}
