package com.github.camelya58.spring_course_spring_data_rest.repository;


import com.github.camelya58.spring_course_spring_data_rest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByName(String name);
}
