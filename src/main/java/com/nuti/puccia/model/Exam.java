package com.nuti.puccia.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Exam {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    @ManyToMany
    @OrderBy("surname, name")
//    private Collection<Student> students;
    private Set<Student> students;

    public Exam() {
    }

    public Exam(String name, Collection<Student> students) {
        this.name = name;
//        this.students = students;
    }

    public Exam(String name, Set<Student> students) {
        this.name = name;
        this.students = students;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id == exam.id &&
                name.equals(exam.name) &&
                students.equals(exam.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, students);
    }

    public void addStudent(Student student) {
//        if (students.contains(student))
//            throw new IllegalArgumentException("Student " + student.toString() +
//                    " already present in " + this.toString() + "!");
        students.add(student);
    }

    public void removeStudent(Student student) {
        if (!students.contains(student))
            throw new IllegalArgumentException("Student " + student.toString() +
                    " not present in " + this.toString() + "!");
        students.remove(student);
    }

    @Override
    public String toString() {
        return name;
    }
}
