package com.github.camelya58.spring;

import org.springframework.stereotype.Component;

//@Component("catBean")
//@Component
public class Cat implements Pet {

    public Cat() {
        System.out.println("Cat is created");
    }

    @Override
    public void say() {
        System.out.println("Meow-Meow!");
    }
}
