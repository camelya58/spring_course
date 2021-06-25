## Acquaintance with Hibernate
The framework for work with Databases.

**Advantages**:
* Provide ORM (Object-to-Relational Mapping).
* Regulate SQL-queries.
* Reduce the amount of code.

### Way 1
Add framework support as Hibernate to your project and set the driver to your database.

Create or complete hibernate.cfg.xml.
```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="connection.url">jdbc:mysql://localhost:3306/my_db?useSSL=false&amp;serverTimezone=UTC</property>
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.username">bestuser</property>
        <property name="connection.password">bestuser</property>

        <property name="current_session_context_class">thread</property>
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="show_sql">true</property>

    </session-factory>
</hibernate-configuration>
```
### Way 2
Add dependencies to pom.xml.
```xml
<dependencies>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.4.32.Final</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.common</groupId>
            <artifactId>hibernate-commons-annotations</artifactId>
            <version>5.1.2.Final</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.25</version>
        </dependency>
</dependencies>
```

Create the same file hibernate.cfg.xml.

Install [MySql for Windows](https://dev.mysql.com/downloads/installer/).
Install [MySQL Connector](https://dev.mysql.com/downloads/connector/j/).

For MacOS:
```
brew install mysql

mysql.server start

mysql -u root

mysql> flush privileges;
mysql> alter user 'root'@'localhost' identified by 'root';

mysql> create database my_db;
mysql> create user 'bestuser'@'localhost' identified by 'bestuser';
mysql> grant all on my_db.* to 'bestuser'@'localhost';
mysql> exit
```
If you use DBeaver and will get the error "Unable to load authentication plugin 'caching_sha2_password'.".
You need to do the following:
```
mysql> alter user 'root'@'localhost' identified with mysql_native_password by 'root';

mysql> alter user 'bestuser'@'localhost' identified with mysql_native_password by 'bestuser';
```

Create new connection.

#### Connection between java objects and DB table
Create an entity using jpa annotations: @Entity, @Table, @Id, @Column.
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

// no-args, all-args constructor
// getters, setters, toString
}
```

#### Save java objects to DB
Create SessionFactory, which can read from hibernate.cfg.xml and create a session for DB.

Then create and save the object using transaction.
```java
public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();
        
        Session session = factory.getCurrentSession();
        try {
            Employee emp = new Employee("Olga", "Petrova", "IT", 500);
            session.beginTransaction();
            session.save(emp);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }
}
``` 

#### Get object from DB
We can save and get java object from DB using session get(class, id) method in 1 or 2 transactions.
```java
public class Test2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            Employee emp = new Employee("Elena", "Ivanova", "Sales", 400);
            session.beginTransaction();
            session.save(emp);
            session.getTransaction().commit();

            int myId = emp.getId();
            session = factory.getCurrentSession();
            session.beginTransaction();
            Employee emp1 = session.get(Employee.class, myId);
            session.getTransaction().commit();

            System.out.println(emp1);
        }
    }
}
```

If we want to get objects not only by the id we need to use HQL(Hibernate query language).
We use the name of the class,not the name of the table.
```java
public class Test3 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            session.beginTransaction();
            List<Employee> emps = session.createQuery("from Employee "
            + "where salary >= 500")
                    .getResultList();

            emps.forEach(System.out::println);
            session.getTransaction().commit();
        }
    }
}
```

#### Update object in DB
We can update 1 object by id or several objects using query.
```java
public class Test4 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            session.beginTransaction();
            Employee emp = session.get(Employee.class, 1);
            emp.setSalary(550);
            session.createQuery("update Employee set salary = 700" +
                    "where department = 'HR'").executeUpdate();

            session.getTransaction().commit();
        }
    }
}
``` 

#### Delete objects from DB
We can delete 1 object by id or several objects using query.
```java
public class Test5 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.getCurrentSession();
            session.beginTransaction();
            Employee emp = session.get(Employee.class, 4);
            session.delete(emp);
            session.createQuery("delete Employee " +
                    "where name = 'Aleksandr'").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
```
### Hibernate association
The hibernate association classified into *One-to-One*, *One-to-Many/Many-to-One* and *Many-to-Many*.
* The direction of a relationship can be either **bidirectional** or **unidirectional**.
* A bidirectional relationship has both an owning side and an inverse side.
* A unidirectional relationship has only an owning side.
 The owning side of a relationship determines how the Persistence run time makes updates to the relationship in the database.

#### One-to-One
##### Uni-directional
Unidirectional is a relation where one side does not know about the relation.

Create 2 tables.
```sql
CREATE TABLE my_db.details (
  id int NOT NULL AUTO_INCREMENT,
  city varchar(15),
  phone_number varchar(25),
  email varchar(30), 
  PRIMARY KEY (id)
);

CREATE TABLE my_db.employees (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  surname varchar(25),
  department varchar(20), 
  salary int, 
  details_id int,  
  PRIMARY KEY (id), 
  FOREIGN KEY (details_id) REFERENCES my_db.details(id));
```

Create class Detail.
```java
@Entity
@Table(name = "details")
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    private int id;

    @Column("city")
    private String city;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;
    
    // no-args, all-args constructor
    // getters, setters, toString
}
```

Create class Employee. We just need to use @OneToOne and @JoinColumn to explain the connection in DB.
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private Detail empDetail;
    
    // no-args, all-args constructor
    // getters, setters, toString
}
```

CascadeType.ALL use in One-To-One relationship, when you want to delete associated details with the employee.

Create Test class.
```java
public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        try (factory; session) {
            Session session = factory.getCurrentSession();
            Employee emp1 = new Employee("Olga", "Petrova", "IT", 500);
            Detail detail1 = new Detail("Moscow", "1234567", "olga@mymail.ru");
            emp1.setEmpDetail(detail1);
            session.beginTransaction();
            session.save(emp1);

            Employee emp = session.get(Employee.class, emp1.getId());
            session.delete(emp);
            session.getTransaction().commit();
        }
    }
}

``` 
If we want to delete the employee, the details also would be deleted.

##### Bi-directional
Bidirectional relationship provides navigational access in both directions, 
so that you can access the other side without explicit queries.

We just need to add in class Detail. That means that we've already had a connection
in class Employee field empDetail.
```
    @OneToOne(mappedBy = "empDetail", cascade = CascadeType.ALL)
    private Employee employee;
```

Create Test class.
```java
public class Test2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Detail.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        
        try (factory; session) {
            Employee emp1 = new Employee("Misha", "Sidorov", "Sales", 400);
            Detail detail1 = new Detail("Penza", "4654221", "misha@mymail.ru");
            emp1.setEmpDetail(detail1);
            detail1.setEmployee(emp1);
            session.beginTransaction();
            session.save(detail1);
            session.getTransaction().commit();
        }
    }
}
```
If we change a cascade type of Details to "cascade = {CascadeType.REFRESH, CascadeType.MERGE}",
we will not be able to delete the details.

We need to break the connection between details and employee.
```
detail.getEmployee().setEmpDetail(null);
```

Now we can delete only details without the employee.

#### One-to-Many
##### Bi-directional
Create 2 tables.
```sql
CREATE TABLE my_db.departments (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  max_salary int,
  min_salary int,
  PRIMARY KEY (id)
);

CREATE TABLE my_db.employees (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  surname varchar(25),
  salary int,
  department_id int,
  PRIMARY KEY (id),
  FOREIGN KEY (department_id) REFERENCES my_db.departments(id));
```

Create class Employee. Be carefully with cascade type.
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

    @Column(name = "salary")
    private int salary;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "department_id")
    private Department department;

    // no-args, all-args constructor
    // getters, setters, toString
}
```

Create class Department. Be carefully with cascade type.
```java
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String depName;

    @Column(name = "max_salary")
    private int maxSalary;

    @Column(name = "min_salary")
    private int minSalary;

    @OneToMany(mappedBy = "department", 
    cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Employee> empList;

    public void addEmployeeToDepartment(Employee employee) {
        if (empList == null) {
            empList = new ArrayList<>();
        }
        empList.add(employee);
        employee.setDepartment(this);
    }

    // no-args, all-args constructor
    // getters, setters, toString
}
```

Create Test class. If you don't use cascade type ALL, you need to use the method persist to save the object.
```java
public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {
            Department department = new Department("IT", 400, 1000);
            Employee emp1 = new Employee("Oleg", "Ivanov", 600);
            Employee emp2 = new Employee("Olga", "Petrova", 800);
            department.addEmployeeToDepartment(emp1);
            department.addEmployeeToDepartment(emp2);
            session.beginTransaction();
            session.persist(department);
            session.getTransaction().commit();
        }
    }
}
```

##### Uni-directional

Delete all information about Department from Employee class.

Change Department class.
```
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "department_id")
    private List<Employee> empList;

    public void addEmployeeToDepartment(Employee employee) {
        if (empList == null) {
            empList = new ArrayList<>();
        }
        empList.add(employee);
    }

```

#### Data loading type

* **Eager**  - data loading happens at the time of their parent is fetched.
* **Lazy** - associated data loads only when we explicitly call getter or size method.

Default fetch type:
* One-to-One and Many-to-One - Eager;
* One-to-Many and Many-to-Many - Lazy.

```
 @OneToMany(fetch = FetchType.LAZY)
```
When we use Lazy loading, hibernate loads only information about parent type.
And if you need the information about associated class hibernates executes another select.

When we use Eager loading, hibernate executes one select to get all information.

#### Many-to-Many

Create 3 tables.
```sql
CREATE TABLE my_db.children (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  age int,
  PRIMARY KEY (id)
);

CREATE TABLE my_db.section (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(15),
  PRIMARY KEY (id)
);

CREATE TABLE my_db.child_section (
  child_id int NOT NULL,
  section_id int NOT NULL,
  PRIMARY KEY (child_id, section_id),
  FOREIGN KEY (child_id) REFERENCES my_db.children(id),
  FOREIGN KEY (section_id) REFERENCES my_db.section(id));
``` 

Create class Child.
```java
@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "child_section",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private List<Section> sections;

    public void addSectionToChild(Section section) {
        if (sections == null) {
            sections = new ArrayList<>();
        }
        sections.add(section);
    }

    // no-args, all-args constructor
    // getters, setters, toString
}
```

Create class Section.
```java
@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "child_section",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Child> children;

    public void addChildToSection(Child child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    // no-args, all-args constructor
    // getters, setters, toString
}
```

Create class Test.
```java
public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Child.class)
                .addAnnotatedClass(Section.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try (factory; session) {

            Section section1 = new Section("football");
            Child child1 = new Child("Oleg", 5);
            Child child2 = new Child("Iliya", 6);
            section1.addChildToSection(child1);
            section1.addChildToSection(child2);

            Section section2 = new Section("music");
            Child child3 = new Child("Olga", 7);
            section2.addChildToSection(child2);
            section2.addChildToSection(child3);
            session.beginTransaction();

            session.persist(section1);
            session.persist(section2);
            session.getTransaction().commit();
        }
    }
}
```