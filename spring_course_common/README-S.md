## Acquaintance with Spring

The work of Spring based on the concepts of *IoC (Inversion of Control)* and *DI (Dependency Injection)*.

**Inversion of Control** is a principle in software engineering which transfers the control 
of objects or portions of a program to a container or framework. 

**Dependency injection** is a pattern we can use to implement IoC, where the control being inverted 
is setting an object's dependencies (connecting objects with other objects, or “injecting” objects into other objects).

In the Spring framework, the interface ApplicationContext represents the IoC container. 
The Spring container is responsible for instantiating, configuring and assembling objects known as beans, 
as well as managing their life cycles.

The Spring framework provides several implementations of the ApplicationContext interface: 
* ClassPathXmlApplicationContext;
* FileSystemXmlApplicationContext for standalone applications;
* WebApplicationContext for web applications.

Configuration methods of Spring Container:
* xml file (old);
* annotations and xml file;
* java code.

## XML configuration

### Inversion of Control
Create applicationContext.xml and add the description of your been:
id - bean's name, class - full name of the class.
Create interface (Pet) and classes (Dog and Cat).
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <bean id="myPet"
          class="com.github.camelya58.spring.Dog">
    </bean>
</beans>
```

Then create a class where you want to invoke your been.
You need to create the ApplicationContext instance.
```java
public class Test {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        Pet pet = context.getBean("myPet", Pet.class);
        pet.say();

        context.close();
    }
}
```
 Don't forget to close the context.
 
 ### Dependency injection
 The ways to inject dependencies:
 * Constructor-Based;
 * Setter-Based;
 * Autowiring.
 
 #### Constructor-Based
Create a Person class.
```java
public class Person {
      private Pet pet;

      public Person(Pet pet) {
          this.pet = pet;
      }
}
```
Add to applicationContext.xml.
```xml
 <bean id="myPerson"
          class="com.github.camelya58.spring.Person">
        <constructor-arg ref="myPet"/>
    </bean>
```

 #### Setter-Based 
 ```java
 public class Person {
       private Pet pet;
 
        public void setPet(Pet pet) {
            this.pet = pet;
        }
 }
 ```
 Add to applicationContext.xml.
 ```xml
  <bean id="myPerson"
           class="com.github.camelya58.spring.Person">
         <property name="pet" ref="myPet"/>
     </bean>
 ```

#### Injection of meanings.
We also can inject the value of the field.
 ```java
 public class Person {
        private Pet pet;
        private String name;
        
        public void setName(String name) {
            this.name = name;
        }

 }
 ```
 Add to applicationContext.xml.
 ```xml
  <bean id="myPerson"
           class="com.github.camelya58.spring.Person">
         <property name="name" value="Kamila"/>
     </bean>
 ```
Or we can use the values from property file.
Create application.properties.
```properties
person.name = Kamila
```
Add to applicationContext.xml.
 ```xml
<beans>
<context:property-placeholder location="classpath:application.properties"/>

    <bean id="myPerson"
           class="com.github.camelya58.spring.Person">
         <property name="name" value="${person.name}"/>
     </bean>
</beans>
 ```

### Bean scope
It is life cycle of the bean and the possible number of instances of one bean.
* Singleton;
* Prototype;
* Request;
* Session;
* Global session.

#### Singleton
- default scope;
- create a bean immediately;
- the one for all who invoke from Spring Container;
- suit to stateless objects.

```xml
    <bean id="myPet"
          class="com.github.camelya58.spring.Dog">
<!--          scope = "singleton">-->
    </bean>
```

#### Prototype
- create a bean after Invoking the method getBean() from Spring Container;
- create as many numbers of bean as invoke from Spring Container;
- suit to stateful objects.

```xml
    <bean id="myPet"
          class="com.github.camelya58.spring.Dog"
          scope = "prototype">
    </bean>
```

### Life cycle of bean
- Run the application.
- Spring Container starts.
- Creation of bean.
- Dependency injection.
- The work of init method.
- Bean is ready for using.
- Using the bean.
- Spring Container ends.
- The work of destroy method.
- Stop the application.

#### Init and destroy methods
Used to open and close the resources.
- can have any modifier;
- can be void or any returned value but it's impossible to get the value; 
- can't have parameters;
- in case scope prototype, the destroy method can't be invoked,
the init method will be invoked for every bean;
- can have any names.
```xml
    <bean id="myPet"
          class="com.github.camelya58.spring.Dog"
          scope = "prototype"
          init-method="init"
          destroy-method="destroy">
    </bean>
```

## Annotation configuration
Create applicationContext.xml. Add package for scanning any components
and add location for property file.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.github.camelya58.spring"/>
    <context:property-placeholder location="classpath:application.properties"/>

</beans>
```

Use annotation @Component in every class to create a bean.
You can create your name - **@Component("catBean")** or it will 
have the name of class starting with lower case. For class Cat - "cat".

### @Autowired
Use for Dependency injection.
Can be used with:
- Constructor;
- Setter or other method;
- Field.

### @Qualifier
If more than one can be created we need to instantiate 
the bean id.
```java
@Component("personBean")
public class Person {

    @Autowired
    @Qualifier("catBean")
    private Pet pet;
/* 
    or 
    @Autowired
    public Person(@Qualifier("catBean") Pet pet) {
        System.out.println("Person is created");
        this.pet = pet;
    }
*/
}
```

### @Value
Used to define the field value.
```
@Value("Kamila")
String name;

or

@Value("${person.name}
String name;
```

### @Scope, @PostConstruct, @PreDestroy
We can choose the scope type - **@Scope("prototype")**

To create the init and destroy methods you need to add dependency
```xml
     <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
            <version>1.3.2</version>
     </dependency>
```
and annotations to methods:
```
    @PostConstruct
    public void init() {
        System.out.println("Class Dog: init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Class Dog: destroy");
    }

```

## Java code configuration
### Way 1
Create a configuration class. All classes for beans must have the annotation @Component.
```java
@Configuration
@ComponentScan("com.github.camelya58.spring")
public class MyConfig {
}
```
Then create test class.
```java
public class Test6 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        Person person = context.getBean("personBean", Person.class);
        person.callYourPet();

        context.close();
    }
}
```
### Way 2
- don't use package scan or bean searching, beans configure in a a config class;
- don't use @Autowired, you need to write the dependencies manually;
- the name of the method is the bean id;
- annotation @Bean catches all invocations to bean and control 
his creation depends on scope. 

Create a configuration class.
```java
@Configuration
public class MyConfig {
 @Bean
    @Scope("prototype")
    public Pet catBean() {
        return new Cat();
    }

    @Bean
    public Person personBean() {
        return new Person(catBean());
    }
}
```
Then create test class.
```java
public class Test6 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        Person person = context.getBean("personBean", Person.class);
        person.callYourPet();

        context.close();
    }
}
```
#### @PropertySource
To be able to use values from a property file, you can use @PropertySource
and mark the class as a parameter. That allows using @Value.
```java
@Configuration
@PropertySource("classpath:application.properties")
public class MyConfig {
    
}
```