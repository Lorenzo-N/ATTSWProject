package com.nuti.puccia.bdd;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.junit.After;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertEquals;

public class examReservationSteps {


    private FrameFixture window;

    @AfterScenario
    public void tearDown() {
        if (window != null)
            window.cleanUp();
    }

    @When("The ExamReservation View is shown")
    public void the_Student_View_is_shown() {
        // start the Swing application
        application("com.nuti.puccia.App").start();
        // get a reference of its JFrame
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return "Exam Reservations".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(BasicRobot.robotWithCurrentAwtHierarchy());
    }

    @BeforeScenario
    public void beforeEachScenario() {
        System.out.println("Prima di ogni scenario!");
    }

//    @Given("a calculator with value <value>")
//    public void givenACalculatorWithValueValue(@Named("value") int value) {
//        c = new Calculator(value);
//    }
//
//    @When("add the number <number>")
//    public void whenAddTheNumberNumber(@Named("number") int number) {
//        c.add(number);
//    }
//
//    @Then("the result is <result>")
//    public void thenTheResultIsResult(@Named("result") int result) {
//        assertEquals(c.getResult(), result);
//    }
//
//    @When("minus the number <number>")
//    public void whenMinusTheNumberNumber(@Named("number") int number) {
//        c.minus(number);
//    }
//
//    @Given("a calculator with value $integer1")
//    public void givenACalculatorWithValue(int integer1) {
//        c = new Calculator(integer1);
//    }
//
//    @When("add the number $integer1")
//    public void whenAddTheNumber(int integer1) {
//        c.add(integer1);
//    }
//
//    @Then("the result is $integer1")
//    public void thenTheResultIs(int integer1) {
//        assertEquals(c.getResult(), integer1);
//    }
//
//    @Given("a calculator with this values:$examplesTable")
//    public void givenACalculatorWithThisValues(ExamplesTable examplesTable) {
//        System.out.println("ciaoooooo");
//        for (Parameters row : examplesTable.getRowsAsParameters()) {
//            int a = row.valueAs("a", int.class);
//            int b = row.valueAs("b", int.class);
//            float c = row.valueAs("c", float.class);
//            boolean d = row.valueAs("d", boolean.class);
//            System.out.println((a+b));
//            System.out.println((c+1.2));
//            if(d){
//                System.out.println("Success!");
//            }
//        }
//    }

    @Given("The database contains the students with the following values$examplesTable")
    public void givenTheDatabaseContainsTheStudentsWithTheFollowingValues(ExamplesTable examplesTable) {

    }

    @Then("The student list contains elements with the following values$examplesTable")
    public void thenTheStudentListContainsElementsWithTheFollowingValues(ExamplesTable examplesTable) {
        window.textBox("ExamNameText").enterText("ciaooo");
        assertThat(window.textBox("ExamNameText").text()).isEqualTo("ciaooo");

    }
}
