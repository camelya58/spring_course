package com.github.camelya58.spring_course_rest_client;

import com.github.camelya58.spring_course_rest_client.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Class Communication
 *
 * @author Kamila Meshcheryakova
 * created 02.07.2021
 */

@Component
public class Communication {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL = "http://localhost:8080/api/employees";

    public List<Employee> showAllEmployee() {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        return responseEntity.getBody();
    }

    public Employee getEmployee(int id) {
        return restTemplate.getForObject(URL + "/" + id, Employee.class);
    }

    public void saveEmployee(Employee employee) {
        int id = employee.getId();
        if (id == 0) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, employee, String.class);
            System.out.println("New employee was added to Database");
            System.out.println(responseEntity.getBody());
        } else {
            restTemplate.put(URL, employee);
            System.out.println("Employee with ID = " + id + " was updated");
        }
    }

    public void deleteEmployee(int id) {
        restTemplate.delete(URL + "/" + id);
        System.out.println("Employee with ID = " + id + " was deleted");
    }
}
