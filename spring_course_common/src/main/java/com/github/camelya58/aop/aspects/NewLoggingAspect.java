package com.github.camelya58.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NewLoggingAspect {

    @Around("execution(public String returnBook())")
    public Object aroundReturnBookLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundReturnBookLoggingAdvice: try to return the book in the Library");
        Object targetMethodResult;
        try {
            targetMethodResult = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println("Catch the exception: " + e);
            throw e;
        }
        System.out.println("aroundReturnBookLoggingAdvice: the book has returned to the Library");
        return targetMethodResult;
    }
}
