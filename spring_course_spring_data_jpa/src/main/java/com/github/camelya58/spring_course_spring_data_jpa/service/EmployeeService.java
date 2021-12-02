package com.github.camelya58.spring_course_spring_data_jpa.service;

import com.github.camelya58.spring_course_spring_data_jpa.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployee(int id);
    void deleteEmployee(int id);
    List<Employee> getAllByName(String name);
}
