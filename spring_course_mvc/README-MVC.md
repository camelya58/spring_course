# Acquaintance with Spring MVC

Model-View-Controller

Browser -> Front Controller (DispatcherServlet) -> Model -> Controller -> Model -> View -> Browser

Model is a container for data.

Controller is the brain of the application for some action with the model.

View is a web-page (html, jsp, Thymeleaf, ect.) with model's data.

JSP - Java Server Page (html + java). JSTL - Java Server Pages Standard Tag Library.

## Configuration of Spring MVC

### Step 1
Create maven project using maven-archetype-webapp.

### Step 2
Add dependencies to pom.xml.
```xml
<dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.8</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>5.3.8</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>5.3.8</version>
    </dependency>
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
</dependencies>
```

### Step 3
Download [Tomcat](https://tomcat.apache.org/download-90.cgi). It must be version 9.

Connect Tomcat to IntelliJ IDEA.
* Go to IntelliJ IDEA: Run -> Edit Configurations -> + (Add New Configuration) Tomcat Server -> Local.
* Name it as "Tomcat". Choose "Application server" - the bottom "Configure.." -> + Fill Tomcat Home (choose the folder of saved tomcat).
* Bottom "Fix" - choose "spring_course_mvc:war exploded" (the name of the project) -> Apply and Ok.
* Run -> Run Tomcat (or your name). Then wait.
* The browser will open the page with 404. And you will see in output "Artifact is deployed successfully".
* Restart IDEA.

### Step 4
Add packages in the project: java (mark it as source root) and others.

### Step 5
Configure the file web.xml (webapp->WEB-INF).
This file allows configuring DispatcherServlet.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         id="WebApp_ID" version="3.1">

  <display-name>spring-course-mvc</display-name>

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
Create the file applicationContext.xml (in the same package).
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context.xsd
    	http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <context:component-scan base-package="com.github.camelya58.spring_course_mvc" />

    <mvc:annotation-driven/>

    <bean
            class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/view/" />
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```
Create a package view under WEB-INF.

### Step 6
Create a file first_view.jsp in the package view.
```html
<!DOCTYPE html>
<html>
<body>
<h2>Spring MVC</h2>
</body>
</html>
```
### Step 7
Create a controller. Return value is the name of jsp file.
```java
@Controller
public class MyController {

    @RequestMapping("/")
    public String showFirstView() {
        return "first_view";
    }
}
```

And Run Tomcat.

We can change the url: Run -> Edit Configurations -> Deployment -> Application context -> Apply and Ok.

### Step 8
Add new methods to controller.
```java
    @RequestMapping("/askDetails")
    public String askEmployeeDetails() {
        return "ask_emp_details_view";
    }

    @RequestMapping("/showDetails")
    public String showEmployeeDetails() {
        return "show_emp_details_view";
    }
```

Create a file ask_emp_details_view.jsp.
```html
<!DOCTYPE html>
<html>
<body>
<h2>Dear Employee, please enter your details</h2>
<br>
<br>
<form action="showDetails" method="get">
    <input type="text" name="employeeName"
           placeholder="Write your name"/>
    <input type="submit"/>
</form>
</body>
</html>
```
Create a file show_emp_details_view.jsp.
```html
<!DOCTYPE html>
<html>
<body>
<h2>Dear Employee, you are welcome!</h2>
<br>
<br>
Your name: ${param.employeeName}
</body>
</html>
```

For convenience, we can add the link to our first page on askDetails form.
```html
<a href="askDetails">Please write your details</a>
```

### Step 9
Add new dependency to pom.xml.
```xml
   <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
```
If we want to use and change the model, we can use it in a controller.
You can change the entered parameter or create new.
```java
    @RequestMapping("/showDetails")
    public String showEmployeeDetails(HttpServletRequest request, Model model) {
        String empName = request.getParameter("employeeName");
        empName = "Mrs. " + empName;
        model.addAttribute("nameAttribute", empName);
        model.addAttribute("description", " - java developer");
        return "show_emp_details_view";
    }
```
You also can use @RequestParam annotation.
```java
    @RequestMapping("/showDetails")
    public String showEmployeeDetails(@RequestParam("employeeName") String empName, Model model) {
        empName = "Mrs. " + empName;
        model.addAttribute("nameAttribute", empName);
        model.addAttribute("description", " - java developer");
        return "show_emp_details_view";
    }
```
Annotation @RequestMapping can be used under the class, if you have several controllers with the same request mappings for methods.
```java
@RequestMapping("/emp") 
@Controller
public class MyController {
}
```
You need to change the param to attribute in show_emp_details_view.jsp.
```html
Your name: ${nameAttribute} ${description}
```

## Spring MVC forms
* form:form - main form which contains other forms (form-container);
* form:input - form for the text;
* form:select - form with drop-down list;
* form:radiobutton - form to switch the values (choose only one);
* form:checkbox - form to set checkbox (choose at least one);
* form:errors - form to show errors.

## Form input
### Step 10
Create class Employee.
```java
public class Employee {

    private String name;
    private String surname;
    private int salary;
    private String department;

    // no-args constructor
    // getters, setters and toString
}
```

### Step 11
Change askEmployeeDetails method in a controller which will ask to fill all fields in a model.
```java
   @RequestMapping("/askDetails")
    public String askEmployeeDetails(Model model) {
        model.addAttribute("employee", new Employee());
        return "ask_emp_details_view";
    }
```
### Step 12
Change ask_emp_details_view.jsp.
```html
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<body>
<h2>Dear Employee, please enter your details</h2>
<br>
<br>
<form:form action = "showDetails" modelAttribute="employee">

    Name <form:input path="name"/>
    <br><br>
    Surname <form:input path="surname"/>
    <br><br>
    Salary <form:input path="salary"/>
    <br><br>
    <input type="submit" value="OK">

</form:form>

</body>
</html>
```

### Step 13
Change showEmployeeDetails method in a controller. As well as we can change emp fields.
```java
  @RequestMapping("/showDetails")
    public String showEmployeeDetails(@ModelAttribute("employee") Employee emp) {
        return "show_emp_details_view";
    }
```
### Step 14
Change show_emp_details_view.jsp.
```html
<!DOCTYPE html>
<html>
<body>
<h2>Dear Employee, you are welcome!</h2>
<br>
<br>
Your name: ${employee.name}
<br>
Your surname: ${employee.surname}
<br>
Your salary: ${employee.salary}
<br>
</body>
</html>
```
Restart tomcat.

## Form select
### Step 15
Add a drop-down list for employee's department to ask_emp_details_view.jsp.
```html
    Department <form:select path="department">
    <form:option value="Information Technology" label="IT"/>
    <form:option value="Human Resources" label="HR"/>
    <form:option value="Sales" label="Sales"/>
    <br><br>
    </form:select>
    <br><br>
```

Add department to show_emp_details_view.jsp.
```html
Your department: ${employee.department}
<br>
```
Restart tomcat.

### Step 16
We can put departments names to a map and use it.
Add the map to an Employee class.
```java
    private Map<String, String> departments;

    public Employee() {
        departments = new HashMap<>();
        departments.put("Information Technology", "IT");
        departments.put("Human Resources", "HR");
        departments.put("Sales", "Sales");
    }

    //getter and setter
```
Change the form in ask_emp_details_view.jsp.
```html
    <form:options items="${employee.departments}"/>
    <br><br>
```
Restart tomcat.

## Form radiobutton
### Step 17
Add to Employee class.
```java
 private String carBrand;

// getter and setter
```

Add the car brand to ask_emp_details_view.jsp.
```html
    <br><br>
    Which car do you want?
    BMW <form:radiobutton path="carBrand" value="BMW"/>
    Audi <form:radiobutton path="carBrand" value="Audi"/>
    MB <form:radiobutton path="carBrand" value="Mercedes-Benz"/>
    <br><br>
```
Add the car brand to show_emp_details_view.jsp.
```html
Your car: ${employee.carBrand}
<br>
```
Restart tomcat.

### Step 18
We can put car brands to a map and use it.
Add the map to an Employee class.
```java
   private Map<String, String> carBrands;

    public Employee() {
        departments = new HashMap<>();
        departments.put("Information Technology", "IT");
        departments.put("Human Resources", "HR");
        departments.put("Sales", "Sales");

        carBrands = new HashMap<>();
        carBrands.put("BMW", "BMW");
        carBrands.put("Audi", "Audi");
        carBrands.put("Mercedes-Benz", "MB");
    }

    //getter and setter
```
Change the form in ask_emp_details_view.jsp.
```html
<form:radiobuttons path="carBrand" items="${employee.carBrands}"/>
```
Restart tomcat.

## Form checkbox
### Step 19
Add to Employee class.
```java
private String[] languages;

// getter and setter
```

Add languages to ask_emp_details_view.jsp.
```html
    <br><br>
    Foreign Language(s)
    EN <form:checkbox path="languages" value="English"/>
    FR <form:checkbox path="languages" value="French"/>
    DE <form:checkbox path="languages" value="Deutch"/>
    <br><br>
```
Add languages to show_emp_details_view.jsp.
```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
Language(s):
<ul>
    <c:forEach var="lang" items="${employee.languages}">
        <li> ${lang}</li>
    </c:forEach>
</ul>
```
Restart tomcat.

### Step 20
We can put languages to a map and use it.
Add the map to an Employee class.
```java
   private Map<String, String> languageList;

    public Employee() {
        departments = new HashMap<>();
        departments.put("Information Technology", "IT");
        departments.put("Human Resources", "HR");
        departments.put("Sales", "Sales");

        carBrands = new HashMap<>();
        carBrands.put("BMW", "BMW");
        carBrands.put("Audi", "Audi");
        carBrands.put("Mercedes-Benz", "MB");

        languageList = new HashMap<>();
        languageList.put("English", "EN");
        languageList.put("French", "FR");
        languageList.put("Deutch", "DE");
    }

    //getter and setter
```
Change the form in ask_emp_details_view.jsp.
```html
<form:checkboxes path="languages" items="${employee.languageList}"/>
```
Restart tomcat.

## Data validation
**Java Standard Bean Validation API** is a specification with the validation rules.
**Hibernate Validation** is an implementation of the validation rules.

## Form errors
### Step 21
Add a dependency.   
```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.0.Final</version>
</dependency>

```

Add annotation to the field - name.
```java
import javax.validation.constraints.Size;

@Size(min = 2, message = "Name must be min 2 symbols")
private String name;
```

Add to ask_emp_details_view.jsp.
```html
Name <form:input path="name"/>
    <form:errors path="name"/>
```

Change the method showEmployeeDetails in a controller.
```java
import javax.validation.Valid;

    @RequestMapping("/showDetails")
    public String showEmployeeDetails(@Valid @ModelAttribute("employee") Employee emp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ask_emp_details_view";
        } else {
            return "show_emp_details_view";
        }
    }
```
We can make the required fields.
You can use @NotEmpty or @NotBlank (count spaces) annotations. 
```java
    @NotBlank(message = "Surname is required field")
    private String surname;
```

Add to ask_emp_details_view.jsp.
```html
 Surname <form:input path="surname"/>
    <form:errors path="surname"/>
```
We can set min and max value for numbers.
```java
    @Min(value = 500, message = "must be greater than 499")
    @Max(value = 1000, message = "must be less than 1001")
    private int salary;
```

Add to ask_emp_details_view.jsp.
```html
 Salary <form:input path="salary"/>
    <form:errors path="salary"/>
```

### Step 24
Add a new field to Employee class.
```java
    private String phoneNumber;

    //getter and setter
```

Add to ask_emp_details_view.jsp.
```html
    <br><br>
    Phone number <form:input path="phoneNumber"/>
    <form:errors path="phoneNumber"/>
    <br><br>
```

Add to show_emp_details_view.jsp.
```html
<br>
Phone number: ${employee.phoneNumber}
<br>
```

We can validate a field using reqex.
```java
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}", message = "Please use pattern XXX-XX-XX")
    private String phoneNumber;
```

### Step 25
We can create own validation annotation.
Add a new field to Employee class.
```java
    private String email;

    //getter and setter
```

Add to ask_emp_details_view.jsp.
```html
    <br><br>
    Email <form:input path="email"/>
    <form:errors path="email"/>
    <br><br>
```

Add to show_emp_details_view.jsp.
```html
<br>
Email: ${employee.email}
<br>
```

Create own annotation.
```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckEmailValidator.class)
public @interface CheckEmail {
    String value() default "@xyz.com";
    String message() default "Email must ends with xyz.com";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

Create annotation validator.
```java
public class CheckEmailValidator implements ConstraintValidator<CheckEmail, String> {
    private String endOfEmail;

    @Override
    public void initialize(CheckEmail checkEmail) {
        endOfEmail = checkEmail.value();
    }
    @Override
    public boolean isValid(String enteredValue, ConstraintValidatorContext context) {
        return enteredValue.endsWith(endOfEmail);
    }
}
```
Add annotation to field.
```java
    @CheckEmail
    private String email;
```
Restart tomcat.