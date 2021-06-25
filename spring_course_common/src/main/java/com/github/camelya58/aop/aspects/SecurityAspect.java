package com.github.camelya58.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
public class SecurityAspect {

//    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allAddMethods()")
//    @Before("this(com.github.camelya58.aop.HomeLibrary)")
//    @Before("target(com.github.camelya58.aop.AbstractLibrary)")
//    @Before("within(com.github.camelya58.aop..*)")
    @Before("within(com.github.camelya58.aop.AbstractLibrary+)")
    public void beforeAddSecurityAdvice() {
        System.out.println("Security check before get methods");
        System.out.println("_____________________________");
    }
}
