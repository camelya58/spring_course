package com.github.camelya58.aop;

import org.springframework.stereotype.Component;

@Component
public class UniLibrary extends AbstractLibrary {

    @Override
    public void getBook() {
        System.out.println("Borrow a book in a UniLibrary");
        System.out.println("______________________________");
    }

    public String returnBook() {
        int a = 10/0;
        System.out.println("Return the book");
        return "The witcher";
    }

    public void getBook(String name) {
        System.out.println("Borrow the book: " + name);
        System.out.println("______________________________");
    }

    public void getBook(Book book) {
        System.out.println("Borrow the book: " + book.getName());
        System.out.println("______________________________");
    }

    public void getMagazine(int number) {
        System.out.println("Borrow a magazine in a UniLibrary for " + number);
        System.out.println("______________________________");
    }

    public void returnMagazine() {
        System.out.println("Return the magazine");
        System.out.println("______________________________");
    }

    public void addBook(String personName, Book book) {
        System.out.printf("%s adds the book [%s] \n", personName, book);
        System.out.println("______________________________");
    }

    public void addMagazine(String personName, Book book) {
        System.out.printf("%s adds the magazine [%s] \n", personName, book);
        System.out.println("______________________________");
    }

}
