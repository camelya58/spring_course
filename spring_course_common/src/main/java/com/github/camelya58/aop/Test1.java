package com.github.camelya58.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        UniLibrary uniLibrary = context.getBean("uniLibrary", UniLibrary.class);
        Book book = context.getBean("book", Book.class);
        uniLibrary.getBook();
        uniLibrary.getMagazine(2021);
//        uniLibrary.getBook("The witcher");
//        uniLibrary.getBook(book);
        uniLibrary.returnBook();
        uniLibrary.returnMagazine();
        uniLibrary.addBook("Kamila", book);
        uniLibrary.addMagazine("Zaur", book);

//        HomeLibrary homeLibrary = context.getBean("homeLibrary", HomeLibrary.class);
//        homeLibrary.getBook();

        context.close();
    }
}
