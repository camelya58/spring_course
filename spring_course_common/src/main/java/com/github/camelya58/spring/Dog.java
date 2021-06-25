package com.github.camelya58.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component
//@Scope("singleton")
public class Dog implements Pet {
    private String name;

    public Dog() {
        System.out.println("Dog is created");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void say() {
        System.out.println("Bow-Wow!");
    }

    @PostConstruct
    public void init() {
        System.out.println("Class Dog: init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Class Dog: destroy");
    }

}
