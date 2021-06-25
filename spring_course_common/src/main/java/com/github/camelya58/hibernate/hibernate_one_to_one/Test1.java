package com.github.camelya58.hibernate.hibernate_one_to_one;

import com.github.camelya58.hibernate.hibernate_one_to_one.entity.Detail;
import com.github.camelya58.hibernate.hibernate_one_to_one.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        try (factory; session) {
            Employee emp1 = new Employee("Ivan", "Ivanov", "HR", 600);
            Detail detail1 = new Detail("Moscow", "7654321", "igor@mymail.ru");
            emp1.setEmpDetail(detail1);
            session.beginTransaction();
            session.save(emp1);
            Employee emp = session.get(Employee.class, emp1.getId());
            session.delete(emp);
            session.getTransaction().commit();
        }
    }
}
