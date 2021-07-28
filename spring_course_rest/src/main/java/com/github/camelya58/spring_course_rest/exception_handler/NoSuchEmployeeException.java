package com.github.camelya58.spring_course_rest.exception_handler;

/**
 * Class NoSuchEmployeeException
 *
 * @author Kamila Meshcheryakova
 * created 29.06.2021
 */
public class NoSuchEmployeeException extends RuntimeException {

    public NoSuchEmployeeException(String message) {
        super(message);
    }
}
