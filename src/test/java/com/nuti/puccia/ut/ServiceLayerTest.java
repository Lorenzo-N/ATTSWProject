package com.nuti.puccia.ut;

import com.nuti.puccia.model.Exam;
import com.nuti.puccia.model.Student;
import com.nuti.puccia.repository.ExamRepository;
import com.nuti.puccia.repository.StudentRepository;
import com.nuti.puccia.service_layer.ServiceLayer;
import com.nuti.puccia.transaction_manager.TransactionFunction;
import com.nuti.puccia.transaction_manager.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.Mockito.*;


public class ServiceLayerTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TransactionManager transactionManager;
    @InjectMocks
    private ServiceLayer serviceLayer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(transactionManager.executeTransaction(any())).thenAnswer(
                answer((TransactionFunction<?> code) -> code.apply(examRepository, studentRepository)));
    }

    @Test
    public void findAllStudentsWhenTheyArePresent() {
        Student student = new Student("Andrea", "Puccia");
        when(studentRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(student)));
        assertThat(serviceLayer.findAllStudents()).containsExactly(student);
        verify(studentRepository).findAll();
        verify(transactionManager).executeTransaction(any());
    }

    @Test
    public void findAllExamsWhenTheyArePresent() {
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        when(examRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(exam)));
        assertThat(serviceLayer.findAllExams()).containsExactly(exam);
        verify(examRepository).findAll();
        verify(transactionManager).executeTransaction(any());
    }

    @Test
    public void addStudent() {
        Student student = new Student("Andrea", "Puccia");
        serviceLayer.addStudent(student);
        verify(studentRepository).addStudent(student);
        verify(transactionManager).executeTransaction(any());
    }

    @Test
    public void addExam() {
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        serviceLayer.addExam(exam);
        verify(examRepository).addExam(exam);
        verify(transactionManager).executeTransaction(any());
    }

    @Test
    public void deleteReservation() {
        Student student = new Student("Andrea", "Puccia");
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>(Collections.singletonList(student)));
        serviceLayer.deleteReservation(exam, student);
        verify(examRepository).deleteReservation(exam, student);
        verify(transactionManager).executeTransaction(any());
    }


    @Test
    public void deleteStudentAndHisReservationsWhenHeExists() {
        Student student = new Student("Andrea", "Puccia");
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> code.apply(examRepository, studentRepository)));
        serviceLayer.deleteStudent(student);
        verify(studentRepository).deleteStudent(student);
        verify(examRepository).deleteStudentReservations(student);
        verify(transactionManager).executeTransaction(any(), anyString());
    }

    @Test
    public void deleteStudentWhenHeDoesNotExist() {
        Student student = new Student("Andrea", "Puccia");
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> {
                    code.apply(examRepository, studentRepository);
                    throw new Error("Error message");
                }));
        assertThatThrownBy(() -> serviceLayer.deleteStudent(student))
                .isInstanceOf(Error.class).hasMessage("Error message");
        verify(studentRepository).deleteStudent(student);
        verify(examRepository).deleteStudentReservations(student);
        verify(transactionManager).executeTransaction(any(),
                eq("Student " + student.toString() + " does not exist!"));
    }


    @Test
    public void deleteExamWhenItExists() {
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> code.apply(examRepository, studentRepository)));
        serviceLayer.deleteExam(exam);
        verify(examRepository).deleteExam(exam);
        verify(transactionManager).executeTransaction(any(), anyString());
    }

    @Test
    public void deleteExamWhenItDoesNotExist() {
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> {
                    code.apply(examRepository, studentRepository);
                    throw new Error("Error message");
                }));
        assertThatThrownBy(() -> serviceLayer.deleteExam(exam)).isInstanceOf(Error.class).hasMessage("Error message");
        verify(examRepository).deleteExam(exam);
        verify(transactionManager).executeTransaction(any(), eq("Exam " + exam.toString() + " does not exist!"));
    }


    @Test
    public void addReservation() {
        Student student = new Student("Andrea", "Puccia");
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> code.apply(examRepository, studentRepository)));
        serviceLayer.addReservation(exam, student);
        verify(examRepository).addReservation(exam, student);
        verify(transactionManager).executeTransaction(any(), anyString());
    }

    @Test
    public void addReservationWhenItAlreadyExists() {
        Student student = new Student("Andrea", "Puccia");
        Exam exam = new Exam("ATTSW", new LinkedHashSet<>());
        when(transactionManager.executeTransaction(any(), anyString())).thenAnswer(
                answer((TransactionFunction<?> code) -> {
                    code.apply(examRepository, studentRepository);
                    throw new Error("Error message");
                }));
        assertThatThrownBy(() -> serviceLayer.addReservation(exam, student)).isInstanceOf(Error.class)
                .hasMessage("Error message");
        verify(examRepository).addReservation(exam, student);
        verify(transactionManager).executeTransaction(any(),
                eq("Student " + student.toString() + " already present in " + exam.toString() + "!"));
    }


}
