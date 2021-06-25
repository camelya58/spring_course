package com.github.camelya58.hibernate.hibernate_one_to_many_bi;

import com.github.camelya58.hibernate.hibernate_one_to_many_bi.entity.Department;
import com.github.camelya58.hibernate.hibernate_one_to_many_bi.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
//            Department department = new Department("IT", 400, 1000);
//            Employee emp1 = new Employee("Oleg", "Ivanov", 600);
//            Employee emp2 = new Employee("Olga", "Petrova", 800);
//            department.addEmployeeToDepartment(emp1);
//            department.addEmployeeToDepartment(emp2);
            session.beginTransaction();
            Department department = session.get(Department.class, 1);
            System.out.println("Show the department");
            System.out.println(department);
            System.out.println("Show employees of the department");
            System.out.println(department.getEmpList());
//            Employee employee = session.get(Employee.class, 1);
//            System.out.println(employee);
            session.getTransaction().commit();
        }
    }
}
