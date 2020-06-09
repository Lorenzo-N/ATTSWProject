Narrative:
Stories to test ExamReservations application

!-- Scenario: The initial state of student list in the view
!-- Given The database contains the students with the following values
!-- |Name|Surname|
!-- |Andrea|Puccia|
!-- |Lorenzo|Nuti|
!-- When The ExamReservation View is shown
!-- Then The student list contains elements with the following values
!-- |Text|
!-- |Nuti Lorenzo (id:1)|
!-- |Puccia Andrea (id:1)|


Scenario: The initial state of student list in the view
Given The ExamReservation View is shown
When User write
Then Is written