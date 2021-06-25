package com.github.camelya58.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test4 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        UniLibrary uniLibrary = context.getBean("uniLibrary", UniLibrary.class);
        uniLibrary.getBook();

        HomeLibrary homeLibrary = context.getBean("homeLibrary", HomeLibrary.class);
        homeLibrary.getBook();

        context.close();
    }
}
