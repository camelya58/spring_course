package com.github.camelya58.spring_course_rest_client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Class Config
 *
 * @author Kamila Meshcheryakova
 * created 02.07.2021
 */
@Configuration
@ComponentScan(basePackages = "com.github.camelya58.spring_course_rest_client")
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
