# spring_course_spring_data_jpa

Simple project with Spring Boot And Spring Data Jpa.

Spring Data Jpa allows you to interact with tables with a minimum of code.

Methods and Crud Operations:
- findAll - get all employees;
- findById - get the employee by id;
- save - add or change the employee;
- deleteById - delete the employee by id.

## Step 1
Create new project with Spring Initializr.
Add to a project: Spring Web, Spring Data Jpa, MySql.

## Step 2
Add properties with database connection.
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_db?useUnicode=true&serverTimezone=UTC&useSSL=false
spring.datasource.username=bestuser
spring.datasource.password=bestuser
```
## Step 3
Add an entity.
```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "department")
    private String department;
    @Column(name = "salary")
    private int salary;
// no args and all args constructor
// getters and setters
// toString

}
```
## Step 4
Add a repository.
JpaRepository contains generics:
- the class of the entity;
- the class of the entity primary key.

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
```
## Step 5
Add a service with implementation.
```java
public interface EmployeeService {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployee(int id);
    void deleteEmployee(int id);
}
```
```java
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(int id) {
        Employee employee = null;
        Optional<Employee> optional =  employeeRepository.findById(id);
        if (optional.isPresent()) {
            employee = optional.get();
        }
        return employee;
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
```
## Step 6
Add a controller.
```java
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
            return service.getEmployee(id);
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
            service.deleteEmployee(id);
            return "Employee with ID = " + id + " was deleted";
        }
}
```
## Step 7
Run the project.
Go to: http://localhost:8080/api/employees.

## Step 8
You can add own method to repository without realization.
```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByName(String name);
}
```

Add method to service and implementation.
```java
@Service
public class EmployeeServiceImpl implements EmployeeService {

// other methods

    @Override
    public List<Employee> getAllByName(String name) {
        return employeeRepository.findAllByName(name);
    }
}
```
Add a new method to controller.
```java

@RestController
@RequestMapping("/api")
public class MyRestController {

// other methods

    @GetMapping("/employees/name/{name}")
    public List<Employee> showAllEmployeesByName(@PathVariable String name) {
        return service.getAllByName(name);
    }
}
```