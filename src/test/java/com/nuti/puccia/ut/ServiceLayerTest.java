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
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.Mockito.*;


public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private ExamRepository examRepository;
    private StudentRepository studentRepository;
    private TransactionManager transactionManager;


    @Before
    public void setUp() {
        examRepository = mock(ExamRepository.class);
        studentRepository = mock(StudentRepository.class);
        transactionManager = mock(TransactionManager.class);
        when(transactionManager.executeTransaction(any())).thenAnswer(
                answer((TransactionFunction<?> code) -> code.apply(examRepository, studentRepository)));
        serviceLayer = new ServiceLayer(transactionManager);
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
        Exam exam = new Exam("ATTSW", new ArrayList<>());
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
        Exam exam = new Exam("ATTSW", new ArrayList<>());
        serviceLayer.addExam(exam);
        verify(examRepository).addExam(exam);
        verify(transactionManager).executeTransaction(any());
    }

    @Test
    public void deleteReservation() {
        Student student = new Student("Andrea", "Puccia");
        Exam exam = new Exam("ATTSW", new ArrayList<>(Collections.singletonList(student)));
        serviceLayer.deleteReservation(exam, student);
        verify(examRepository).deleteReservation(exam, student);
        verify(transactionManager).executeTransaction(any());
    }



//    @Mock
//    private StudentRepository studentRepository;
//    @Mock
//    private ExamRepository examRepository;
//    @InjectMocks
//    private ServiceLayer serviceLayer;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//

//
//    @Test
//    public void deleteStudentAndHisReservationsWhenHeExists() {
//        Student student = new Student("Andrea", "Puccia");
//        when(studentRepository.findById(0)).thenReturn(student);
//        serviceLayer.deleteStudent(student);
//        InOrder inOrder = inOrder(studentRepository, examRepository);
//        inOrder.verify(examRepository).deleteStudentReservations(student);
//        inOrder.verify(studentRepository).deleteStudent(student);
//    }
//
//    @Test
//    public void deleteStudentWhenHeDoesNotExist() {
//        Student student = new Student("Andrea", "Puccia");
//        when(studentRepository.findById(0)).thenReturn(null);
//        assertThatThrownBy(() -> serviceLayer.deleteStudent(student))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Student " + student.toString() + " does not exist!");
//        verifyNoMoreInteractions(ignoreStubs(studentRepository));
//    }
//

//

//
//    @Test
//    public void deleteExamWhenItExists() {
//        Exam exam = new Exam("ATTSW", new ArrayList<>());
//        when(examRepository.findById(0)).thenReturn(exam);
//        serviceLayer.deleteExam(exam);
//        verify(examRepository).deleteExam(exam);
//    }
//
//    @Test
//    public void deleteExamWhenItDoesNotExist() {
//        Exam exam = new Exam("ATTSW", new ArrayList<>());
//        when(examRepository.findById(0)).thenReturn(null);
//        assertThatThrownBy(() -> serviceLayer.deleteExam(exam))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Exam " + exam.toString() + " does not exist!");
//        verifyNoMoreInteractions(ignoreStubs(examRepository));
//    }
//
//

//
//    @Test
//    public void addReservation() {
//        Student student = new Student("Andrea", "Puccia");
//        Exam exam = new Exam("ATTSW", new ArrayList<>());
//        serviceLayer.addReservation(exam, student);
//        verify(examRepository).addReservation(exam, student);
//    }
//



}
