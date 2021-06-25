package com.github.camelya58.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test6 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        Person person = context.getBean("personBean", Person.class);
//        person.callYourPet();
        System.out.println(person.getName());
        System.out.println(person.getAge());

//        Pet cat = context.getBean("catBean", Pet.class);
//        Pet cat1 = context.getBean("catBean", Pet.class);
       // cat.say();

        context.close();
    }
}
