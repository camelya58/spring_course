package com.github.camelya58.hibernate.hibernate_many_to_many;

import com.github.camelya58.hibernate.hibernate_many_to_many.entity.Child;
import com.github.camelya58.hibernate.hibernate_many_to_many.entity.Section;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Child.class)
                .addAnnotatedClass(Section.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {

            Section section1 = new Section("dance");
            Child child1 = new Child("Nikita", 5);
            Child child2 = new Child("Ivan", 6);
            section1.addChildToSection(child1);
            section1.addChildToSection(child2);

            Section section2 = new Section("volleyball");
            Child child3 = new Child("Nina", 7);
            section2.addChildToSection(child2);
            section1.addChildToSection(child3);
            session.beginTransaction();
//            Section section = session.get(Section.class, 1);
//            System.out.println(section.getChildren());
//
//            Child child = session.get(Child.class, 2);
//            System.out.println(child.getSections());
            session.persist(section1);
            session.persist(section2);
            session.getTransaction().commit();
        }
    }
}
