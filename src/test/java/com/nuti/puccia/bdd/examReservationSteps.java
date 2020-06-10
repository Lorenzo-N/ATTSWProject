package com.nuti.puccia.bdd;

import com.nuti.puccia.App;
import com.nuti.puccia.model.Exam;
import com.nuti.puccia.model.Student;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.junit.After;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;

import java.util.*;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertEquals;

public class examReservationSteps {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private FrameFixture window;

    private final String name1 = "Andrea";
    private final String surname1 = "Puccia";
    private final String name2 = "Lorenzo";
    private final String surname2 = "Nuti";
    private final String name3 = "Mario";
    private final String surname3 = "Rossi";
    private final String exam1 = "ATTSW";
    private final String exam2 = "Analisi";
    private final String exam3 = "Fisica";

    @BeforeStories
    public void setUpStories() {
        Map<String, String> settings = new HashMap<>();
        settings.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/attsw");
        settings.put("javax.persistence.jdbc.user", "root");
        settings.put("javax.persistence.jdbc.password", "");
//        entityManagerFactory = Persistence.createEntityManagerFactory("APP", settings);
        entityManager = App.getEntityManager(settings);
    }

    @AfterStories
    public void tearDownStories() {
//        entityManagerFactory.close();
        App.closeConnection();
    }

    @BeforeScenario
    public void setUp() {
        System.out.println("Prima di ogni scenario!");
//        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Exam e").executeUpdate();
        entityManager.createQuery("DELETE FROM Student s").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @AfterScenario
    public void tearDown() {
        if (window != null) {
//            window.close();
            window.cleanUp();
        }
//        entityManager.close();
    }

    private void addStudentToDataBase(Student student) {
        entityManager.getTransaction().begin();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
    }

    private void addExamToDataBase(Exam exam) {
        entityManager.getTransaction().begin();
        entityManager.persist(exam);
        entityManager.getTransaction().commit();
    }

    @Given("The Exam Reservation View is shown")
    public void givenExamReservationViewIsShown() {
        System.out.println("start!");
        application("com.nuti.puccia.App").start();
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return "Exam Reservations".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(BasicRobot.robotWithCurrentAwtHierarchy());
    }

    @When("User write")
    public void whenUserWrite() {
        window.textBox("ExamNameText").enterText("ciaooo");
    }

    @Then("Is written")
    public void thenIsWritten() {
        assertThat(window.textBox("ExamNameText").text()).isEqualTo("ciaooo");
    }

    @Given("The Database contains few students and few exams")
    public void givenDatabaseContainsFewStudentsAndFewExams() {
        System.out.println("background!");
        Student student1 = new Student(name1, surname1);
        Student student2 = new Student(name2, surname2);
        addStudentToDataBase(student1);
        addStudentToDataBase(student2);
        addExamToDataBase(new Exam(exam1, new ArrayList<>(Collections.singletonList(student1))));
        addExamToDataBase(new Exam(exam2, new ArrayList<>(Arrays.asList(student1, student2))));
    }

    @When("The user selects an Exam")
    @Given("The user selects an Exam")
    public void whenTheUserSelectsAnExam() {
        window.list("ExamList").selectItem(Pattern.compile(".*" + exam1 + ".*"));
    }

    @Then("Exam list contains exams info")
    public void thenExamListContainsExamsInfo() {
        assertThat(window.list("ExamList").contents())
                .anySatisfy(e -> assertThat(e).contains(exam1))
                .anySatisfy(e -> assertThat(e).contains(exam2));
    }

    @Then("Student list contains students info")
    public void thenStudentListContainsStudentsInfo() {
        assertThat(window.list("StudentList").contents())
                .anySatisfy(e -> assertThat(e).contains(name1, surname1))
                .anySatisfy(e -> assertThat(e).contains(name2, surname2));
    }

    @Then("Reservation list contains reservations info")
    public void thenReservationListContainsReservationsInfo() {
        assertThat(window.list("ReservationList").contents()).hasSize(1)
                .anySatisfy(e -> assertThat(e).contains(name1, surname1));
    }

    @Given("The user enter exam name")
    public void givenTheUserEnterExamName() {
        window.textBox("ExamNameText").enterText(exam3);
    }

    @When("The user clicks $buttonName button")
    public void whenTheUserClicksAButton(String buttonName) {
        window.button(buttonName).click();
    }

    @Then("Exam list contains new exam info")
    public void thenExamListContainsNewExamInfo() {
        assertThat(window.list("ExamList").contents()).hasSize(3)
                .anySatisfy(e -> assertThat(e).contains(exam3));
    }

    @Given("The user enter student name and surname")
    public void givenTheUserEnterStudentNameAndSurname() {
        window.textBox("StudentNameText").enterText(name3);
        window.textBox("StudentSurnameText").enterText(surname3);
    }

    @Then("Student list contains new student info")
    public void thenStudentListContainsNewStudentInfo() {
        assertThat(window.list("StudentList").contents()).hasSize(3)
                .anySatisfy(e -> assertThat(e).contains(name3, surname3));
    }

    @Given("The user selects a Student")
    public void givenTheUserSelectsAStudent() {
        window.list("StudentList").selectItem(Pattern.compile(".*" + surname2+" "+name2 + ".*"));
    }

    @Then("Reservation list contains selected student info")
    public void thenReservationListContainsSelectedStudentInfo() {
        assertThat(window.list("ReservationList").contents()).hasSize(2)
                .anySatisfy(e -> assertThat(e).contains(name2, surname2));
    }

    @Then("Student list not contains selected student info")
    public void thenStudentListNotContainsSelectedStudentInfo() {
        assertThat(window.list("StudentList").contents()).hasSize(1)
                .noneMatch(e -> e.contains(surname2+" "+name2));
    }
}
