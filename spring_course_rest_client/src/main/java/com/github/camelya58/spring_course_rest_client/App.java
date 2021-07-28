package com.github.camelya58.spring_course_rest_client;

import com.github.camelya58.spring_course_rest_client.config.Config;
import com.github.camelya58.spring_course_rest_client.entity.Employee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Class App
 *
 * @author Kamila Meshcheryakova
 * created 02.07.2021
 */
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        Communication communication = context.getBean(Communication.class);
        List<Employee> allEmployees = communication.showAllEmployee();
        System.out.println(allEmployees);

        Employee employee = communication.getEmployee(1);
        System.out.println(employee);

        Employee employee1 = new Employee("Sveta", "Kozlova", "IT", 900);
        employee1.setId(6);
        communication.saveEmployee(employee1);

        communication.deleteEmployee(6);

        context.close();

    }
}
