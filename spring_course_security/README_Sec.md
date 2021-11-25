# spring_course_security

Simple project with Spring Security and Spring MVC.

Configuration of Spring MVC + Spring Security application:
- Add dependencies to a pom file.
- Create a configuration Java class.
- Create a class as Dispatcher Servlet.
- Add a Tomcat to a project.
- Create a Security Initializer.
- Create a configuration for Spring Security as authentication.
- Create a controller with views.
- Add a configuration for authorization.
- Save the security user information in a database without and with encoding.

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
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-web</artifactId>
      <version>5.5.1</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-config</artifactId>
      <version>5.5.1</version>
    </dependency>
  </dependencies>
```
You need to have maven-war-plugin.

## Step 3
Create a configuration class.
```java
@Configuration
@ComponentScan("com.github.camelya58.spring_course_security")
@EnableWebMvc
public class MyConfig {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
```

## Step 4
Create a Dispatcher servlet class.
```java
public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MyConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

## Step 5
Add a Tomcat.
* Go to IntelliJ IDEA: Run -> Edit Configurations -> + (Add New Configuration) Tomcat Server -> Local.
* Name it as "Tomcat security". Choose "Application server" - the bottom "Configure.." -> + Fill Tomcat Home (choose the folder of saved tomcat).
* Bottom "Fix" - choose "spring_course_security:war exploded" (the name of the project) -> Apply and Ok.

## Step 6
Create a Security Initializer.
```java
public class MySecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
```

## Step 7
Create a configuration for Spring Security.

We can use in memory authentication.
```java
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(userBuilder.username("zaur").password("zaur").roles("EMPLOYEE"))
                .withUser(userBuilder.username("elena").password("elena").roles("HR"))
                .withUser(userBuilder.username("ivan").password("ivan").roles("MANAGER", "HR"));
                
    }
}
```

## Step 8
Create a controller.
```java
@Controller
public class MyController {

    @GetMapping("/")
    public String getInfoForAllEmps() {
        return "view_for_all_employees";
    }

    @GetMapping("/hr_info")
    public String getInfoOnlyForHR() {
        return "view_for_hr";
    }

    @GetMapping("/manager_info")
    public String getInfoOnlyForManager() {
        return "view_for_manager";
    }
}
```

Create simple views:
- view_for_all_employees.jsp;
```html
<!DOCTYPE html>
<html>
<body>


<h3>Information for all employees</h3>
<br><br>

<input type="button" value="Salary" onclick="window.location.href = 'hr_info'">
Only for HR staff
<br><br>
<input type="button" value="Performance" onclick="window.location.href = 'manager_info'">
Only for Managers
<br><br>

</body>
</html>
```
- view_for_hr.jsp;
```html
<!DOCTYPE html>
<html>

<body>

<h3>Here you can see all salaries</h3>

</body>
</html>
```
- view_for_manager.jsp;
```html
<!DOCTYPE html>
<html>

<body>

<h3>Here you can see performance of your employees</h3>

</body>
</html>
```
## Step 9
Run the tomcat.
At this step all employees can see all data.

## Step 10
Add an authorization.
Add a method to class MySecurityConfig.
```java
  @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER")
                .antMatchers("/hr_info").hasAnyRole("HR")
                .antMatchers("/manager_info").hasAnyRole("MANAGER")
                .and().formLogin().permitAll();
    }
```
Restart Tomcat.

Now hr can see the information for hr and can't see the information for manager.
But hr still sees the button for manager.

## Step 11
Divide the information on the web page by role.
Add a dependency to pom.xml.
```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
    <version>5.6.0</version>
</dependency>
```

Add to a view_for_all_employees.jsp:
```jsp
<security:authorize access="hasRole('HR')">
<input type="button" value="Salary" onclick="window.location.href = 'hr_info'">
Only for HR staff
</security:authorize>

<security:authorize access="hasRole('MANAGER')">
<input type="button" value="Performance" onclick="window.location.href = 'manager_info'">
Only for Managers
```
Restart the Tomcat and see that now the button for manager is hidden from hr.

## Step 12
Save the security user information in a database.
The are 2 ways to save a password:
- {noop}yourpassword - without encoding (no operation);
- {bcrypt}yourpassword - with encoding.

Create tables and user data without encoding.
```sql
USE my_db;

CREATE TABLE users (
  username varchar(15),
  password varchar(100),
  enabled tinyint(1),
  PRIMARY KEY (username)
) ;

CREATE TABLE authorities (
  username varchar(15),
  authority varchar(25),
  FOREIGN KEY (username) references users(username)
) ;

INSERT INTO my_db.users (username, password, enabled)
VALUES
	('zaur', '{noop}zaur', 1),
	('elena', '{noop}elena', 1),
	('ivan', '{noop}ivan', 1);
    
INSERT INTO my_db.authorities (username, authority)
VALUES
	('zaur', 'ROLE_EMPLOYEE'),
	('elena', 'ROLE_HR'),
    ('ivan', 'ROLE_HR'),
	('ivan', 'ROLE_MANAGER');
    
```
Perform the operation one by one.
Any employee can be disabled. You need to update the field enabled with 0 value.

## Step 13
Add the dependencies:
```xml
   <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.25</version>
    </dependency>
    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.2</version>
    </dependency>
```

Add a bean datasource to class MyConfig.
```java
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
```
Add to a MySecurityConfig new bean and change the method.
```java
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }
```

## Step 14
Encrypt and change passwords.
```sql
UPDATE my_db.users set PASSWORD ='{bcrypt}$2a$12$SACouhbaDmQ7AiRbUICFD.GCtauLId/9krL.wDRHX0wW1bQVreAYK' where username = 'zaur';
UPDATE my_db.users set PASSWORD ='{bcrypt}$2a$12$xzTCLwNbxwrT3nZ9Lr.MGOyxl0/HT/ngMLjMsrfZor.1pon89nfzm' where username = 'elena';
UPDATE my_db.users set PASSWORD ='{bcrypt}$2a$12$bOYM41ghni9wdm1Ti2X6x.C6cWxbPy0iIti9h/ybVbMYLyb5dDgMa' where username = 'ivan';
```