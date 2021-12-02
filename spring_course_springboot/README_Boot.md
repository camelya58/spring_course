# spring_course_springboot

Simple project with Spring Boot.

Spring boot is a framework to easily create Spring api.
* autoconfiguration (creation of beans, configs, ect.);
* dependency management (starter packages with related dependencies);
* embedded tomcat and server support.

JPA (Java Persistence API) is a standard specification for managing 
the persistence of java objects in database tables.

Hibernate is the most popular JPA implementation.
JPA contains the rules, Hibernate implements them.

Instead of session JPA has an entityManager.

Configuration of Spring Boot application:
- Create a project with Spring Initializr.
- Prepare tables and database.
- Add dependencies to a pom file.
- Add properties with database connection.
_ Create an entity, a repository, a service and a controller.

## Step 1
Create new project with Spring Initializr.
Add to a project: Spring Web, Spring Data Jpa, MySql.

@SpringBootApplication in a main class include:
- @Configuration;
- @EnableAutoConfiguration;
- @ComponentScan.

## Step 2
Create a table.
```sql
USE my_db;

CREATE TABLE employees (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  surname varchar(25),
  department varchar(20),
  salary int,
  PRIMARY KEY (id)) ;

INSERT INTO my_db.employees (name, surname, department, salary)
VALUES
	('Zaur', 'Tregulov', 'IT', 500),
	('Oleg', 'Ivanov', 'Sales', 700),
	('Nina', 'Sidorova', 'HR', 850);
```
## Step 3
 Add properties with database connection.
 Use application.properties to set values, variables and settings.
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_db?useUnicode=true&serverTimezone=UTC&useSSL=false
spring.datasource.username=bestuser
spring.datasource.password=bestuser
```
## Step 4
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
## Step 5
Add a dao with implementation.
```java
public interface EmployeeDAO {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployee(int id);
    void deleteEmployee(int id);    
}
```
```java
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Employee", Employee.class)
                .getResultList();
    }

     @Override
        public void saveEmployee(Employee employee) {
            Session session = entityManager.unwrap(Session.class);
            session.saveOrUpdate(employee);
        }
    
        @Override
        public Employee getEmployee(int id) {
            Session session = entityManager.unwrap(Session.class);
            return session.get(Employee.class, id);
        }
    
        @Override
        public void deleteEmployee(int id) {
            Session session = entityManager.unwrap(Session.class);
            Query<Employee> query = session.createQuery("delete from Employee where id = :employeeId");
            query.setParameter("employeeId", id);
            query.executeUpdate();
        }
}
```
## Step 6
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
    private EmployeeDAO employeeDAO;

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

@Override
    @Transactional
    public void saveEmployee(Employee employee) {
        employeeDAO.saveEmployee(employee);
    }

    @Override
    @Transactional
    public Employee getEmployee(int id) {
        return employeeDAO.getEmployee(id);
    }

    @Override
    @Transactional
    public void deleteEmployee(int id) {
        employeeDAO.deleteEmployee(id);
    }    
}
```
## Step 7
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
## Step 8
Run the project.
Go to: http://localhost:8080/api/employees.
```json
[
  {
    "id":1,
    "name":"Zaur",
    "surname":"Tregulov",
    "department":"IT",
    "salary":700
  },
  {
    "id":2,
    "name":"Oleg",  
    "surname":"Ivanov",
    "department":"Sales",
    "salary":700  
   },
  {
    "id":3,
    "name":"Nina",
    "surname":"Sidorova",
    "department":"HR",
    "salary":700
  },
  {
    "id":5,
    "name":"Inna",
    "surname":"Orlova",
    "department":"Sales",
    "salary":550
   }
]
```
You can change the port or the url for api:
```properties
server.port=9999
server.servlet.context-path=/springboot_rest
```

The url will be: http://localhost:9999/springboot_rest/api/employees.
## Step 9
Add to pom file the tool that will track changes in the project and update it.
```xml
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
  </dependency>
```
Go to: New project Settings -> Preferences for new projects -> 
Build, Execution, Deployment -> Compiler -> Build the project automatically (mark)

Ctrl + Shift + A -> write "registry" -> compiler.automake.allow.when.app.running (true)

## Step 10
Change hibernate to jpa.
```java
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        return entityManager.createQuery("from Employee", Employee.class)
                .getResultList();
    }

    @Override
    public void saveEmployee(Employee employee) {
        Employee newEmployee = entityManager.merge(employee);
        employee.setId(newEmployee.getId());
    }

    @Override
    public Employee getEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public void deleteEmployee(int id) {
        Query query = entityManager.createQuery("delete from Employee where id = :employeeId");
        query.setParameter("employeeId", id);
        query.executeUpdate();
    }
}
```