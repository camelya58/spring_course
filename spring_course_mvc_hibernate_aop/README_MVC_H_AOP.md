# spring_course_mvc_hibernate_aop

Simple project with Spring MVC, Hibernate and AOP.

### Step 1
Create Maven project with an archetype: maven-archetype-webapp.

### Step 2
Open mysql connector or DBeaver and create a table.
```sql
USE my_db;

CREATE TABLE employees (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  surname varchar(25),
  department varchar(20),
  salary int,
  PRIMARY KEY (id)
) ;

INSERT INTO my_db.employees (name, surname, department, salary)
VALUES
	('Zaur', 'Tregulov', 'IT', 500),
	('Oleg', 'Ivanov', 'Sales', 700),
	('Nina', 'Sidorova', 'HR', 850);
``` 

### Step 3
Add Tomcat to project.

Connect Tomcat to IntelliJ IDEA.
* Go to IntelliJ IDEA: Run -> Edit Configurations -> + (Add New Configuration) Tomcat Server -> Local.
* Name it as "Tomcat". Choose "Application server" - the bottom "Configure.." -> + Fill Tomcat Home (choose the folder of saved tomcat).
* Bottom "Fix" - choose "spring_course_mvc:war exploded" (the name of the project) -> Apply and Ok.
* Run -> Run Tomcat (or your name). Then wait.
* The browser will open the page with 404. And you will see in output "Artifact is deployed successfully".
* Restart IDEA.

### Step 4
Add dependencies.
```xml
   <dependencies>
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-webmvc</artifactId>
         <version>5.3.8</version>
       </dependency>
       <dependency>
         <groupId>javax.servlet</groupId>
         <artifactId>jstl</artifactId>
         <version>1.2</version>
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
   </dependencies>
```

### Step 5
Create a file - web.xml.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         id="WebApp_ID" version="3.1">

  <display-name>spring-course-mvc-hibernate-aop</display-name>

  <absolute-ordering />

  <servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>/WEB-INF/applicationContext.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>

  <servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

</web-app>
```

### Step 6
Create a file - applicationContext.xml.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context.xsd
    	http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.github.camelya58.spring_course_mvc_hibernate_aop" />

    <mvc:annotation-driven/>

    <bean
            class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/view/" />
        <property name="suffix" value=".jsp" />
    </bean>

    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
          destroy-method="close">
        <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/my_db?useSSL=false&amp;serverTimezone=UTC" />
        <property name="user" value="bestuser" />
        <property name="password" value="bestuser" />
    </bean>

    <bean id="sessionFactory"
          class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="packagesToScan" value="com.github.camelya58.spring_course_mvc_hibernate_aop.entity" />
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
                <prop key="hibernate.show_sql">true</prop>
            </props>
        </property>
    </bean>

    <bean id="transactionManager"
          class="org.springframework.orm.hibernate5.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <tx:annotation-driven transaction-manager="transactionManager" />


</beans>
```

### Step 7
Create an entity.
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

    //no-args, all-args constructor
    //getters, setters and toString
}
```

### Step 8
Create dao.
```java
public interface EmployeeDAO {
    List<Employee> getAllEmployees();
}
```

Create an implementation.
```java
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Employee", Employee.class)
                               .getResultList();
    }
}
```

### Step 9
Add a controller.
```java
@Controller
public class MyController {

    @Autowired
    private EmployeeDAO employeeDAO;

    @RequestMapping("/")
    public String showAllEmployees(Model model) {
        List<Employee> allEmployees = employeeDAO.getAllEmployees();
        model.addAttribute("AllEmps", allEmployees);
        return "all_employees";
    }

}
```

### Step 10
Create all_employees.jsp.
```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>


<h2>All Employees</h2>
<br>

<table>
    <tr>
        <th>Name</th>
        <th>Surname</th>
        <th>Department</th>
        <th>Salary</th>
    </tr>
    <c:forEach var="emp" items="${allEmps}"/>
    <tr>
        <td>${emp.name}</td>
        <td>${emp.surname}</td>
        <td>${emp.department}</td>
        <td>${emp.salary}</td>
    </tr>
    </c:forEach>
</table>
</body>

</html>
```

### Step 11
Create service and correct controller and dao.
```java
public interface EmployeeService {
    List<Employee> getAllEmployees();
}
```
Create implementation.
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
}
```
Remove @Transactional from the method in dao class.

Change controller.
```java
    @Autowired
    private EmployeeService service;
```
Run Tomcat.

### Step 12
Add the button "add" to all_employees.jsp.
```html
<br>
<input type="button" value="Add"
       onclick="window.location.href = 'addNewEmployee'"/>
</body>
```

Create new method in a controller.
```java
    @RequestMapping("/addNewEmployee")
    public String addNewEmployee(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "employee_info";
    }
```

Create employee_info.jsp.
```html
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

<body>
<h2>Employee info</h2>
<br>

<form:form action="saveEmployee" modelAttribute="employee">
    Name <form:input path="name"/>
    <br><br>
    Surname <form:input path="surname"/>
    <br><br>
    Department <form:input path="department"/>
    <br><br>
    Salary <form:input path="salary"/>
    <br><br>
    <input type="submit" value="OK">

</form:form>

</body>

</html>
```
Create new method in a controller.
```java
    @RequestMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        service.saveEmployee(employee);
        return "redirect:/";
    }
```

Add the method saveEmployee to service and implementation.
```java
    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        employeeDAO.saveEmployee(employee);
    }
```

Add the method saveEmployee to dao and implementation.
```java

    @Override
    public void saveEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.save(employee);
    }
```
Restart tomcat.

### Step 13
Add the field "Operations" and the button "Update" for all employees to all_employees.jsp.
```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>


<h2>All Employees</h2>
<br>

<table>
    <tr>
        <th>Name</th>
        <th>Surname</th>
        <th>Department</th>
        <th>Salary</th>
        <th>Operations</th>
    </tr>
    <c:forEach var="emp" items="${allEmps}">
        
        <c:url var="updateButton" value="/updateInfo">
            <c:param name="empId" value="${emp.id}"/>
        </c:url>
        
        <tr>
            <td>${emp.name}</td>
            <td>${emp.surname}</td>
            <td>${emp.department}</td>
            <td>${emp.salary}</td>
            <td>
                <input type="button" value="Update"
                onclick="window.location.href = '${updateButton}'">
            </td>

        </tr>
    </c:forEach>
</table>
<br>
<input type="button" value="Add"
       onclick="window.location.href = 'addNewEmployee'"/>
</body>

</html>
```
Add the method getEmployee to service and dao. Write the implementation.
```java
    @Override
    public Employee getEmployee(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Employee.class, id);
    }
```
Create the method to controller.
```java
    @RequestMapping("/updateInfo")
    public String updateEmployee(@RequestParam("empId") int id, Model model) {
        Employee employee = service.getEmployee(id);
        model.addAttribute("employee", employee);
        return "employee_info";
    }
```
We need to add to employee_info.jsp information about id.
```html
  <form:hidden path="id"/>
```
Change the method from dao.
```java
    @Override
    public void saveEmployee(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(employee);
    }
```
Restart tomcat.

### Step 14
Add the button "Delete" for all employees to all_employees.jsp.
```html

        <c:url var="deleteButton" value="/deleteEmployee">
            <c:param name="empId" value="${emp.id}"/>
        </c:url>

            <td>
                <input type="button" value="Delete"
                onclick="window.location.href = '${deleteButton}'">
            </td>
```
Add the method getEmployee to service and dao. Write the implementation.
```java
   @Override
    public void deleteEmployee(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Employee> query = session.createQuery("delete from Employee where id = :employeeId");
        query.setParameter("employeeId", id);
        query.executeUpdate();
    }
```
Create the method to controller.
```java
    @RequestMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("empId") int id) {
      service.deleteEmployee(id);
        return "redirect:/";
    }
```
Restart tomcat.

### Step 15
Add AOP to project.

Add the dependency to pom.xml.
```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.6</version>
</dependency>
```
Add a config to applicationContext.xml.
```xml
 <beans 
xmlns:aop="http://www.springframework.org/schema/aop"

 xsi:schemaLocation="
 http://www.springframework.org/schema/aop
 http://www.springframework.org/schema/aop/spring-aop.xsd">

<aop:aspectj-autoproxy/>

</beans>
```

Create an aspect.
```java
@Aspect
@Component
public class LoggingAspect {
    
    @Around("execution(* com.github.camelya58.spring_course_mvc_hibernate_aop.dao.*.*(..))")
    public Object aroundAllDaoMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        System.out.println("Begin of " + methodName);
        Object methodResult = joinPoint.proceed();
        System.out.println("End of " + methodName);
        return methodResult;
    }
}
```
Restart tomcat.