package com.github.camelya58.spring_course_mvc_hibernate_aop.dao;

import com.github.camelya58.spring_course_mvc_hibernate_aop.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployee(int id);
    void deleteEmployee(int id);
}
