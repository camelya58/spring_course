package com.github.camelya58.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test3 {

    public static void main(String[] args) {
        System.out.println("Main starts");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        UniLibrary uniLibrary = context.getBean("uniLibrary", UniLibrary.class);

        String bookName = uniLibrary.returnBook();
        System.out.println("The book: " + bookName + " in a library");
        context.close();
        System.out.println("Main ends");

    }
}
