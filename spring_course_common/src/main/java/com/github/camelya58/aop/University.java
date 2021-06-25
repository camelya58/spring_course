package com.github.camelya58.aop;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class University {

    private List<Student> students = new ArrayList<>();

    public void addStudents() {
        Student st1 = new Student("Kamila", 5, 9.8);
        Student st2 = new Student("Igor", 1, 7.8);
        Student st3 = new Student("Tanya", 3, 8.8);
        students.add(st1);
        students.add(st2);
        students.add(st3);
    }

    public List<Student> getStudents() {
        System.out.println("The beginning of the method getStudents");
//        System.out.println(students.get(3));
        System.out.println("An information from the method getStudents: ");
        System.out.println(students);
        return students;
    }
}
