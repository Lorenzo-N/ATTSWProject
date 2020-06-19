package com.nuti.puccia.it;

import com.nuti.puccia.model.Exam;
import com.nuti.puccia.model.Student;
import com.nuti.puccia.repository.mysql.ExamRepositoryMysql;
import com.nuti.puccia.repository.mysql.StudentRepositoryMysql;
import com.nuti.puccia.transaction_manager.mysql.TransactionManagerMysql;
import org.junit.*;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transaction;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TransactionManagerMysqlIT {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private TransactionManagerMysql transactionManager;

    private final Student student1 = new Student("Andrea", "Puccia");
    private final Student student2 = new Student("Lorenzo", "Nuti");
    private final Student student3 = new Student("Mario", "Rossi");

    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TESTS");
    }

    @Before
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Exam e").executeUpdate();
        entityManager.createQuery("DELETE FROM Student s").executeUpdate();
        entityManager.getTransaction().commit();

        transactionManager = new TransactionManagerMysql(entityManager);
    }

    @After
    public void tearDown() {
        entityManager.close();
    }

    @AfterClass
    public static void tearDownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void executeTransaction() {
        Student student = new Student("Andrea", "Puccia");
        Object result = transactionManager.executeTransaction((examRepository, studentRepository) -> {
            assertThat(examRepository).isInstanceOf(ExamRepositoryMysql.class);
            assertThat(studentRepository).isInstanceOf(StudentRepositoryMysql.class);
            entityManager.persist(student);
            return student;
        });
        assertThat(entityManager.getTransaction().isActive()).isFalse();
        assertThat(result).isEqualTo(student);
        assertThat(getStudentsFromDataBase()).containsExactly(student);
    }

    @Test
    public void executeTransactionWhenExceptionThrown() {
        Student student = new Student("Andrea", "Puccia");
        assertThatThrownBy(() -> transactionManager.executeTransaction((examRepository, studentRepository) -> {
            assertThat(examRepository).isInstanceOf(ExamRepositoryMysql.class);
            assertThat(studentRepository).isInstanceOf(StudentRepositoryMysql.class);
            entityManager.persist(student);
            throw new RuntimeException("Exception");
        })).isInstanceOf(RuntimeException.class).hasMessage("Transaction rolled back");
        assertThat(entityManager.getTransaction().isActive()).isFalse();
        assertThat(getStudentsFromDataBase()).isEmpty();
    }

    private List<Student> getStudentsFromDataBase() {
        return entityManager.createQuery("select s from Student s", Student.class).getResultList();
    }
}
