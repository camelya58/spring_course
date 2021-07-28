package com.github.camelya58.spring_course_rest.controller;

import com.github.camelya58.spring_course_rest.entity.Employee;
import com.github.camelya58.spring_course_rest.exception_handler.NoSuchEmployeeException;
import com.github.camelya58.spring_course_rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class RestController
 *
 * @author Kamila Meshcheryakova
 * created 29.06.2021
 */
@RestController
@RequestMapping("/api")
public class MyRestController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/employees")
    public List<Employee> showAllEmployees() {
        return service.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        if (employee == null) {
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in Database");
        }
        return employee;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        if (employee == null) {
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in Database");
        }
        service.deleteEmployee(id);
        return "Employee with ID = " + id + " was deleted";
    }
}
