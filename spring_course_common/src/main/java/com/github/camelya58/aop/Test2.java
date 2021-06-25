package com.github.camelya58.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Test2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        try {
            University university = context.getBean("university", University.class);
            university.addStudents();
            List<Student> students = university.getStudents();
            System.out.println(students);
        } catch (Throwable e) {
            System.out.println("The exception: " + e);
        }

        context.close();
    }
}
