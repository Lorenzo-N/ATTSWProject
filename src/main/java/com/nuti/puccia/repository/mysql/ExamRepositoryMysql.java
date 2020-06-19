package com.nuti.puccia.repository.mysql;

import com.nuti.puccia.model.Exam;
import com.nuti.puccia.model.Student;
import com.nuti.puccia.repository.ExamRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ExamRepositoryMysql implements ExamRepository {
    EntityManager entityManager;

    public ExamRepositoryMysql(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteExam(Exam exam) {
        entityManager.remove(exam);
    }

    @Override
    public void addExam(Exam exam) {
        entityManager.persist(exam);
    }

    @Override
    public void addReservation(Exam exam, Student student) {
        exam.addStudent(student);
    }

    @Override
    public void deleteReservation(Exam exam, Student student) {
        exam.removeStudent(student);
    }

    @Override
    public void deleteStudentReservations(Student student) {
        TypedQuery<Exam> query = entityManager.createQuery("select e from Exam e where :student member of e.students", Exam.class);
        query.setParameter("student", student);
        List<Exam> exams = query.getResultList();
        exams.forEach(exam -> exam.removeStudent(student));

//        Query query = entityManager.createQuery("delete from Exam.students s where :student = s");
//        query.setParameter("student", student);
//        query.executeUpdate();
    }

    @Override
    public List<Exam> findAll() {
        return entityManager.createQuery("select e from Exam e order by e.name", Exam.class).getResultList();
    }

    @Override
    public Exam findById(long id) {
        TypedQuery<Exam> query = entityManager.createQuery("select e from Exam e where e.id = :id", Exam.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
