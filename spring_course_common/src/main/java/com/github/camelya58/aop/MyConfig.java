package com.github.camelya58.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.github.camelya58.aop")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class MyConfig {
}
