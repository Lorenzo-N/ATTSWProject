Narrative:
Stories to test ExamReservations application



Scenario: Initial state of student and exam list
Given The Database contains few students and few exams
And The Exam Reservation View is shown
When The user selects an Exam
Then Exam list contains exams info
And Student list contains students info
And Reservation list contains reservations info

Scenario: Add exam
Given The Database contains few students and few exams
And The Exam Reservation View is shown
And The user enter exam name
When The user clicks AddExam button
Then Exam list contains new exam info
