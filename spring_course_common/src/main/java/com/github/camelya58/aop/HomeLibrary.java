package com.github.camelya58.aop;

import org.springframework.stereotype.Component;

@Component
public class HomeLibrary extends AbstractLibrary {

    @Override
    public void getBook() {
        System.out.println("Borrow a book in a HomeLibrary");
    }
}
