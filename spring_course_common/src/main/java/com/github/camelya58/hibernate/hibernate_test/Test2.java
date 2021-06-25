package com.github.camelya58.hibernate.hibernate_test;

import com.github.camelya58.hibernate.hibernate_test.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            Employee emp = new Employee("Dima", "Sidorov", "PR", 450);
            session.beginTransaction();
            session.save(emp);
            int myId = emp.getId();
            Employee emp1 = session.get(Employee.class, myId);
            session.getTransaction().commit();

            System.out.println(emp1);
        }
    }
}
