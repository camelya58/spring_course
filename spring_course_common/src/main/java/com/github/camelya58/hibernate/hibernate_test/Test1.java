package com.github.camelya58.hibernate.hibernate_test;

import com.github.camelya58.hibernate.hibernate_test.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            Employee emp = new Employee("Kamila", "Mescheryakova", "IT", 500);
            session.beginTransaction();
            session.save(emp);
            session.getTransaction().commit();
        }
    }
}
