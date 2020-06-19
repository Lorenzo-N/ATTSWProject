package com.nuti.puccia.service_layer;

import com.nuti.puccia.model.Exam;
import com.nuti.puccia.model.Student;
import com.nuti.puccia.repository.ExamRepository;
import com.nuti.puccia.repository.StudentRepository;
import com.nuti.puccia.transaction_manager.TransactionManager;

import java.util.List;

public class ServiceLayer {

    private final TransactionManager transactionManager;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public ServiceLayer(TransactionManager manger) {
        transactionManager = manger;
        studentRepository = null;
        examRepository = null;
    }

    //    TODO delete this constructor after all
    public ServiceLayer(StudentRepository studentRepository, ExamRepository examRepository) {
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        transactionManager = null;
    }

    public List<Student> findAllStudents() {
        return transactionManager.executeTransaction((
                (examRepository, studentRepository) -> studentRepository.findAll()));
    }

    public List<Exam> findAllExams() {
        return transactionManager.executeTransaction((
                (examRepository, studentRepository) -> examRepository.findAll()));
    }

    public void addStudent(Student student) {
            transactionManager.executeTransaction((
                    (examRepository, studentRepository) -> {
                        studentRepository.addStudent(student);
                        return null;
                    }));
    }

    public void addExam(Exam exam) {
        transactionManager.executeTransaction((
                (examRepository, studentRepository) -> {
                    examRepository.addExam(exam);
                    return null;
                }));
    }

    public void deleteReservation(Exam exam, Student student) {
        transactionManager.executeTransaction((
                (examRepository, studentRepository) -> {
                    examRepository.deleteReservation(exam,student);
                    return null;
                }));
    }

//    TODO update this methods ↓
//    All methods can throw an Exception that must be catch



    public void deleteStudent(Student student) {
        if (studentRepository.findById(student.getId()) == null)
            throw new IllegalArgumentException("Student " + student.toString() + " does not exist!");
        examRepository.deleteStudentReservations(student);
        studentRepository.deleteStudent(student);
    }


    public void deleteExam(Exam exam) {
        if (examRepository.findById(exam.getId()) == null)
            throw new IllegalArgumentException("Exam " + exam.toString() + " does not exist!");
        examRepository.deleteExam(exam);
    }


    public void addReservation(Exam exam, Student student) {
        examRepository.addReservation(exam, student);
    }


}
