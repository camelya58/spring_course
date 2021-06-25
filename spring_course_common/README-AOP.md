## Acquaintance with Aspect Oriented Programming (AOP)
 AOP is based on cross-catting logic.
 * Logging;
 * Security check;
 * Transaction handling;
 * Exception handling;
 * Caching, ect.
 
 ### Advantages
 * Cross-catting logic is situated in one or more independent classes. 
 It allows to change the class easier.
 * You can add new cross-catting works for main class or add existing 
 cross-catting works for new classes easier.
 * Application business code is getting rid of cross-catting code,
 it becomes simpler and cleaner.
 
 ### Disadvantages
 * Aspects require additional time to work.
 
**AOP framework**:
 * Spring AOP (provides more popular and needed AOP functionality, easy to use);
 * AspectJ (provides all AOP functionality, more complicated to use).
 
 ### Aspect
 Aspect - class responsible for cross-catting work.
 
 Create new package - aop.
 Create Configuration class.
 ```java
@Configuration
@ComponentScan("com.github.camelya58.aop")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class MyConfig {
}
```
Annotation @EnableAspectJAutoProxy allows using Spring AOP Proxy

Create Library class as usual bean.
```java
@Component("libraryBean")
public class Library {
    public void getBook() {
        System.out.println("Borrow a book");
    }
}
```

Create Test class.
```java
public class Test1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        Library uniLibrary = context.getBean("libraryBean", Library.class);
        uniLibrary.getBook();

        context.close();
    }
}
```

We need to add dependency.
```xml
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.7.M2</version>
</dependency>
```

### Advice types
#### Advice @Before
Advice is a method of aspect and contains cross-catting logic.
Advice determines what should happen and when.
* *Before* (invoke before the method with the main logic);
* *After returning* (invoke after normal work of the method with the main logic);
* *After throwing* (invoke after throwing an exception in the method with the main logic);
* *After/ After finally* (invoke after the method with the main logic);
* *Around* (invoke before and after work of the method with the main logic).


Create Aspect class with before Advice.
```java
@Component
@Aspect
public class LoggingAspect {

    @Before("execution(public void getBook())")
    public void beforeGetBookAdvice() {
        System.out.println("Before getBook method");
    }
}
```
We need to write what method must be executed after advice.
**Pointcut** - the expression describing where the advice must be executed.

#### Pointcut patterns
Pattern for pointcut:
```
execution( modifier? return-type declaring-type? method-name(parameters) throws?)
```

where declaring-type is a full name of class
```
execution(public void com.github.camelya58.aop.Library.getBook())
``` 
Without a declaring-type, this advice will work before all methods in this package that match such a poincut.

We also can use this pattern for all methods starting with "get" without parameters.
```
execution(public void get*())
``` 
This pattern matches methods with any modifier, any return-type, any name and without parameters.
```
execution(* *())
```
This pattern matches methods with any modifier, any return-type, any name and with one parameter string type.
```
execution(public void getBook(String))
```
This pattern matches methods with any modifier, any return-type, any name and with one parameter any type.
```
execution(* *(*))
```
This pattern matches methods with any modifier, any return-type, any name and with any numbers of parameters any type.
```
execution(* *(..))
```
This pattern matches methods with any numbers of parameters any type include Book.
```
execution(public void getBook(com.github.camelya58.aop.Book, ..)
```

#### Declaring Pointcut
```
@Pointcut("pointcut_expression")
private void pointcut_reference(){}

@Before("pointcut_reference()")
public void advice_name() { some code}
```
##### Pointcut Designators
* **execution**

The example how it works.
```java

@Component
@Aspect
public class LoggingAndSecurityAspect {

    @Pointcut("execution(* get*(..))")
    private void allGetMethods(){}

    @Before("allGetMethods()")
    public void beforeGetBookAdvice() {
        System.out.println("Before get method");
    }

    @Before("allGetMethods()")
    public void beforeGetSecurityAdvice() {
        System.out.println("Before security method");
    }
}
```
* **within**

Used to achieve the same result the previous pointcut is for certain class, package or subpackage.
```
@Pointcut("within(com.github.camelya58.aop.UniLibrary")

@Pointcut("within(com.github.camelya58.aop..*")
```
To match class and inheritors.
```
@Pointcut("within(com.github.camelya58.aop.AbstractLibrary+)")
```
* **this** and **target**

*this* limits matching to join points where the bean reference is an instance of the given type
 while *target* limits matching to join points where the target object is an instance of the given type.
```
@Pointcut("this(com.github.camelya58.aop.HomeLibrary)")

@Pointcut("target(com.github.camelya58.aop.AbstractLibrary)")

```
* **@target**

The @target limits matching to join points where the class of the executing object has an annotation of the given type.
```
@Pointcut("@target(org.springframework.stereotype.Repository)")
```

* **@args**

The @args limits matching to join points where the runtime type of the actual arguments passed have annotations of the given type(s). 
```
@Pointcut("@args(com.github.camelya58.aop.annotations.Entity)")
```
* **@within**

This PCD limits matching to join points within types that have the given annotation:
```
@Pointcut("@within(org.springframework.stereotype.Repository)")
```
Which is equivalent to:
```
@Pointcut("within(@org.springframework.stereotype.Repository *)")
```
* **@annotation**

This PCD limits matching to join points where the subject of the join point has the given annotation. 
For example, we may create a @Loggable annotation:
```
@Pointcut("@annotation(com.github.camelya58.aop.annotations.Loggable)")
```

#### Combining Pointcut
We can compine pointcuts using logical operators: **&&**, **||**, **!**.
```java
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.github.camelya58.aop.UniLibrary.get*(..))")
    private void allGetMethodsFromUniLibrary(){}

    @Pointcut("execution(* com.github.camelya58.aop.UniLibrary.return*())")
    private void allReturnMethodsFromUniLibrary(){}

    @Pointcut("allGetMethodsFromUniLibrary() || allReturnMethodsFromUniLibrary()")
    private void allGetAndReturnMethodsFromUniLibrary(){}

    @Before("allGetMethodsFromUniLibrary()")
    public void beforeGetLoggingAdvice() {
        System.out.println("beforeGetLoggingAdvice: writing Log #1");
    }

    @Before("allReturnMethodsFromUniLibrary()")
    public void beforeReturnLoggingAdvice() {
        System.out.println("beforeReturnLoggingAdvice: writing Log #2");
    }

    @Before("allGetAndReturnMethodsFromUniLibrary()")
    public void beforeGetAndReturnLoggingAdvice() {
        System.out.println("beforeGetAndReturnLoggingAdvice: writing Log #3");
    }
}
```

We can set advice before to all methods except one.
```java
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.github.camelya58.aop.UniLibrary.*(..))")
    private void allMethodsFromUniLibrary(){}

    @Pointcut("execution(public void com.github.camelya58.aop.UniLibrary.returnMagazine())")
    private void returnMagazineFromUniLibrary(){}

    @Pointcut("allMethodsFromUniLibrary() && !returnMagazineFromUniLibrary()")
    private void allMethodsExceptReturnMagazineFromUniLibrary(){}

    @Before("allMethodsExceptReturnMagazineFromUniLibrary()")
    public void beforeAllMethodsExceptReturnMagazinesLogging() {
        System.out.println("beforeAllMethodsExceptReturnMagazinesLogging: logging");
    }
}
``` 

#### The order between aspects
For ordering your aspects you need to create aspects in different classes and use annotation @Order.
Create class for pointcuts.
```java
public class MyPointcuts {

    @Pointcut("execution(* get*(..))")
    public void allGetMethods(){}
}
```

Create an aspect for logging.
```java
@Component
@Aspect
@Order(1)
public class LoggingAspect {

    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allGetMethods()")
    public void beforeGetLoggingAdvice() {
        System.out.println("Logging before get methods");
    }
}
```

Create an aspect for security.
```java
@Component
@Aspect
@Order(2)
public class SecurityAspect {

    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allGetMethods()")
    public void beforeGetSecurityAdvice() {
        System.out.println("Security check before get methods");
    }
}
```

Create an aspect for exception handling.
```java
@Component
@Aspect
@Order(3)
public class ExceptionHandlingAspect {

    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allGetMethods()")
    public void beforeGetExceptionHandlingAdvice() {
        System.out.println("ExceptionHandling before get methods");
    }
}
```
#### JoinPoint
It is a point in a programme where the advice should be used.

JoinPoint allows getting information about method signature and parameters.
```
    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allAddMethods()")
    public void beforeAddLoggingAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
    }
``` 
Correct LoggingAspect.
```java
@Component
@Aspect
@Order(1)
public class LoggingAspect {

    @Before("com.github.camelya58.aop.aspects.MyPointcuts.allAddMethods()")
    public void beforeAddLoggingAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println("methodSignature: " + methodSignature);
        System.out.println("methodSignature.getMethod(): " + methodSignature);
        System.out.println("methodSignature.getReturnType(): " + methodSignature.getReturnType());
        System.out.println("methodSignature.getName(): " + methodSignature.getName());
        System.out.println("methodSignature.getParameterNames(): " + Arrays.toString(methodSignature.getParameterNames()));

        System.out.println("Logging before get methods");
        if (methodSignature.getName().equals("addBook")) {
            Object[] args = joinPoint.getArgs();
            for (Object obj: args) {
                if (obj instanceof Book) {
                    Book book = (Book) obj;
                    System.out.println("An information about the book: name - " +
                            book.getName() + ", author - " + book.getAuthor() +
                            ", year of publication - " + book.getYearOfPublication());
                }
                else if (obj instanceof String) {
                    System.out.println(obj + " added the book in a Library");
                }
            }
        }
        System.out.println("_____________________________");
    }

}
```

#### Advice @AfterReturning
Allows getting the result of main method work and do some work after it and change the result.
```java
@Aspect
@Component
public class UniversityLoggingAspect {

    @AfterReturning(pointcut = "execution(* getStudents())",
    returning = "students")
    public void afterReturningGetStudentsLoggingAdvice(List<Student> students) {
        Student first = students.get(0);
        String name = first.getName();
        name = "Mrs. " + name;
        first.setName(name);

        double avgGrade = first.getAvgGrade();
        avgGrade = avgGrade + 0.2;
        first.setAvgGrade(avgGrade);

        System.out.println("afterReturningGetStudentsLoggingAdvice: logging after ");
        System.out.println("______________________");
    }
}
```
The students in the getStudents method are not the same as in list. 
UniversityLoggingAspect has already changed them. 
```java
public class Test2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);

        University university = context.getBean("university", University.class);
        university.addStudents();
        List<Student> students = university.getStudents();
        System.out.println(students);

        context.close();
    }
}
```
#### Advice @AfterThrowing
Allows to get the exception from the method with the main logic, but can't handle it.
```
    @AfterThrowing(pointcut = "execution(* getStudents())",
    throwing = "exception")
    public void afterThrowingGetStudentsLoggingAdvice(JoinPoint joinPoint, Throwable exception) {
        System.out.println("afterThrowingGetStudentsLoggingAdvice: logging the exception " + exception);
    }
```
#### Advice @After
Allows to do some work after the method with the main logic despite on the result of the method.
But you can't get access to the exception or the returning value.
```
   @After("execution(* getStudents())")
    public void afterGetStudentsLoggingAdvice() {
        System.out.println("afterGetStudentsLoggingAdvice: logging finally");
    }
```

#### Advice @Around
The advice completes before and after the method with the main logic.
You need to initiate the work of target method and return or change the result of the target method.
```java
@Aspect
@Component
public class NewLoggingAspect {

    @Around("execution(public String returnBook())")
    public Object aroundReturnBookLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundReturnBookLoggingAdvice: try to return the book in the Library");
        Object targetMethodResult = joinPoint.proceed();
        System.out.println("aroundReturnBookLoggingAdvice: the book has returned to the Library");
        return targetMethodResult;
    }
}
``` 
Allows getting work with the exception:
- handle the exception;
- rethrow the exception.
```java
@Aspect
@Component
public class NewLoggingAspect {

    @Around("execution(public String returnBook())")
    public Object aroundReturnBookLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundReturnBookLoggingAdvice: try to return the book in the Library");
        Object targetMethodResult;
                try {
                    targetMethodResult = joinPoint.proceed();
                } catch (Throwable e) {
                    System.out.println("Catch the exception: " + e);
                    throw e;
                }
        System.out.println("aroundReturnBookLoggingAdvice: the book has returned to the Library");
        return targetMethodResult;
    }
}
``` 