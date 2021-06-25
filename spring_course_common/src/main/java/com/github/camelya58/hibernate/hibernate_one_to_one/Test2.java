package com.github.camelya58.hibernate.hibernate_one_to_one;

import com.github.camelya58.hibernate.hibernate_one_to_one.entity.Detail;
import com.github.camelya58.hibernate.hibernate_one_to_one.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
//            Employee emp1 = new Employee("Misha", "Sidorov", "Sales", 400);
//            Detail detail1 = new Detail("Penza", "4654221", "misha@mymail.ru");
//            emp1.setEmpDetail(detail1);
//            detail1.setEmployee(emp1);
            session.beginTransaction();
//            session.save(detail1);
            Detail detail = session.get(Detail.class, 5);
            System.out.println(detail.getEmployee());
            detail.getEmployee().setEmpDetail(null);
            session.delete(detail);
            session.getTransaction().commit();
        }
    }
}
