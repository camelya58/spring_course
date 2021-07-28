# Acquaintance with Spring Rest Api

Connection between Client and Server using HTTP protocol.

HTTP methods: GET and POST.

**GET**:
* The transmitted information is stored in the url;
* Limited size;
* Does not support transfer of binary data (image, video);
* Allows to share a link;
* Usually used to get the information.

**POST**:
* The transmitted information is stored in the request body;
* Unlimited size;
* Supports transfer of binary data (image, video);
* Does not allow to share a link;
* Usually used to add some data.

**HTTP Request**:
* *Request line* - HTTP method and url;
* *Headers* - request metadata (zero or more);
* *Empty line* - separate the header from the body;
* *Message body*(optional) - payload.

**HTTP Response**:
* *Status line* - status code and status text;
* *Headers* - response metadata (zero or more);
* *Empty line* - separate the header from the body;
* *Message body*(optional) - payload.

**HTTP Response Status Codes**: 
* **1XX** - Informational - *Request received, process continues*;
* **2XX** - Success - *Request successfully received*;
* **3XX** - Redirection - *Need to complete the following actions*;
* **4XX** - Client error - *Request contains incorrect data or can't be completed*;
* **5XX** - Server error - *Server can't fulfill the correct request*.

### REST API Standards

![Image alt](https://i.ibb.co/4P6Bdsr/df.png)

## Configure Spring MVC + Hibernate application without xml file.

* Create maven project using maven-archetype-webapp.
* Connect to database (my_db) with Workbench or DBeaver.

* Add dependencies to pom.xml.
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.8</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.5.2.Final</version>
    </dependency>
    <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>c3p0</artifactId>
        <version>0.9.5.2</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>5.3.8</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.25</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.12.3</version>
    </dependency>
</dependencies>
```

* Create configuration file.

We need to create all beans like in applicationContext.xml from spring_course_mvc_hibernate_aop.
```java
@Configuration
@ComponentScan(basePackages = "com.github.camelya58.spring_course_rest")
@EnableWebMvc
@EnableTransactionManagement
public class MyConfig {

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/my_db?useSSL=false");
            dataSource.setUser("bestuser");
            dataSource.setPassword("bestuser");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.github.camelya58.spring_course_rest.entity");

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
```

Be sure that your pom has maven-war-plugin.

* Create a class with DispatcherServlet.

We need to create a class responsible for DispatcherServlet instead of using web.xml.
```java
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MyConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

* Add a tomcat to project.

### Get a list of employees

Create a controller.
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


}
```

Copy packages service, entity, dao from previous project from spring_course_mvc_hibernate_aop.
Correct packages in import.

Start tomcat.

Use a browser or Postman.

### Get the employee by id

Add a method to controller.

Annotation @PathVariable allows to read a variable from url.
```java
  @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return service.getEmployee(id);
    }
```

### Exception Handling
Create a class which contains massage in case of exception.
```java
public class EmployeeIncorrectData {
    
    private String info;

    public EmployeeIncorrectData() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
```

Create an exception.
```java
public class NoSuchEmployeeException extends RuntimeException {

    public NoSuchEmployeeException(String message) {
        super(message);
    }
}
```
Change the method in the controller.
```java
 @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        if (employee == null) {
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in Database");
        }
        return employee;
    }
```

Create the method for exception handling in the controller in case when we don't have such employee by id.
```java
 @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(NoSuchEmployeeException exception) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
```

Create the method for exception handling in the controller in case of a bad request.
```java
   @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(Exception exception) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
```
Restart tomcat.

If you have several controller classes the best practise is to have exception handling in a separate class using AOP.
```java
@ControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(NoSuchEmployeeException exception) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(Exception exception) {
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
```

### Add the employee
Add the method to the controller.
```java
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }
```

### Update the employee
Add the method to the controller.
```java
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return employee;
    }
```

### Delete the employee
Add the method to the controller.
```java
    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id) {
        Employee employee = service.getEmployee(id);
        if (employee == null) {
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in Database");
        }
        service.deleteEmployee(id);
        return "Employee with ID = " + id + " was deleted";
    }
```
