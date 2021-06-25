package com.github.camelya58.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test5 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext2.xml");

        Dog myDog = context.getBean("myPet", Dog.class);
        myDog.say();

        Dog yourdog = context.getBean("myPet", Dog.class);
        yourdog.say();

        context.close();
    }

}
