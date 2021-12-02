# spring_course_spring_data_rest

Simple project with Spring Boot, Spring Data Jpa, Spring Data Rest and Spring Boot Actuator.

Spring Data Rest provides an automatic mechanism of REST API based on the entity type.

### REST API Standards
![Image alt](https://i.ibb.co/1mk3D0N/2021-12-02-16-28-22.png)

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
Add dependency to a pom file.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-rest</artifactId>
        </dependency>
```
This step allows creating rest api (controller endpoints) based on every entity in every repository:
- it will see EmployeeRepository;
- find first generic in JpaRepository - Employee;
- map it to 'employees';
- create rest api based on best practice (see the table - REST API Standards).

Now you don't need services or controllers.

## Step 6
Run the project.
Go to: http://localhost:8080/employees.
```json
{
    "_embedded": {
        "employees": [
            {
                "name": "Zaur",
                "surname": "Tregulov",
                "department": "IT",
                "salary": 700,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/1"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/1"
                    }
                }
            },
            {
                "name": "Oleg",
                "surname": "Ivanov",
                "department": "Sales",
                "salary": 700,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/2"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/2"
                    }
                }
            },
            {
                "name": "Nina",
                "surname": "Sidorova",
                "department": "HR",
                "salary": 700,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/3"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/3"
                    }
                }
            },
            {
                "name": "Inna",
                "surname": "Orlova",
                "department": "Sales",
                "salary": 550,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/5"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/5"
                    }
                }
            },
            {
                "name": "Artem",
                "surname": "Orlov",
                "department": "IT",
                "salary": 1100,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/8"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/8"
                    }
                }
            },
            {
                "name": "Anna",
                "surname": "Ivanova",
                "department": "HR",
                "salary": 600,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/9"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/9"
                    }
                }
            },
            {
                "name": "Ivan",
                "surname": "Andreev",
                "department": "Sales",
                "salary": 800,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/10"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/10"
                    }
                }
            },
            {
                "name": "Oleg",
                "surname": "Sidorov",
                "department": "IT",
                "salary": 1200,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/11"
                    },
                    "employee": {
                        "href": "http://localhost:8080/employees/11"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/employees"
        },
        "profile": {
            "href": "http://localhost:8080/profile/employees"
        },
        "search": {
            "href": "http://localhost:8080/employees/search"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 8,
        "totalPages": 1,
        "number": 0
    }
}
```
## Step 7
Note that for PUT method you need to add the id or the new employee will be created.
'/employee/{employeeId}'.

## Step 8
We can monitor our services with Spring Boot Actuator.

![Image alt](https://i.ibb.co/9Tm4gzL/2021-12-02-16-52-33.png)

Add to a pom file.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

To add some info about application use application.properties:
```properties
info.name=Spring Data Rest
info.description=Spring Boot REST Application.\
  This app is used for CRUD operations with employees of our company
info.author=Kamila Mescheryakova
```

You need to add properties to see all endpoints:
```properties
management.endpoints.web.exposure.include=*
```

If you want to hide the secure information about your application you need to add Spring Security.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```
```properties
spring.security.user.name=user
spring.security.user.password=password
```

 
