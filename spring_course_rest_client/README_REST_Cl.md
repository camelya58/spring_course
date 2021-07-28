# REST Client

Simple project with Spring REST.

## Step 1
Create a maven project with maven-archetype-quickstart.

## Step 2
Add the dependencies.
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.8</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.3</version>
        </dependency>
</dependencies>
```

## Step 3
Create a config class with RestTemplate bean.
```java
@Configuration
@ComponentScan(basePackages = "com.github.camelya58.spring_course_rest_client")
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## Step 4
Create an entity class.
```java
public class Employee {

    private int id;
    private String name;
    private String surname;
    private String department;
    private int salary;

    // no-args, all-args constructor
    // getters, setters, toString
}
```

## Step 5
Create a class for connection to REST Server.
```java

@Component
public class Communication {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL = "http://localhost:8080/api/employees";

    public List<Employee> showAllEmployee() {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Employee>>() {});
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
```

## Step 6
Create the main class.
```java
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        Communication communication = context.getBean(Communication.class);
        List<Employee> allEmployees = communication.showAllEmployee();
        System.out.println(allEmployees);

        // for create
        Employee employee1 = new Employee("Sveta", "Kozlova", "HR", 600);
        communication.saveEmployee(employee1);
        
        // for update
        Employee employee1 = new Employee("Sveta", "Kozlova", "IT", 900);
        employee1.setId(6);
        communication.saveEmployee(employee1);
        
        
        context.close();

    }
}
```