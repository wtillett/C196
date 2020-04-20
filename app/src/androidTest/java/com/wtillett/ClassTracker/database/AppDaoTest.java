package com.wtillett.ClassTracker.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppDaoTest {
    private AppDao dao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = db.appDao();
//
//        dao.insertTerm(new Term(
//                "The First Term", LocalDate.now(), LocalDate.now().plusMonths(6)));
//        dao.insertTerm(new Term(
//                "The Second Term", LocalDate.now().plusMonths(6), LocalDate.now().plusMonths(12)));
//        dao.insertTerm(new Term(
//                "The Third Term", LocalDate.now().plusMonths(12), LocalDate.now().plusMonths(18)));
//        dao.insertCourse(new Course(
//                1, "Math", LocalDate.now(), LocalDate.now().plusMonths(3),
//                "Just begun.", ""));
//        dao.insertCourse(new Course(
//                2, "Spanish", LocalDate.now().plusMonths(6), LocalDate.now().plusMonths(12),
//                "Not yet started", ""));
//        dao.insertCourse(new Course(
//                3, "Physics", LocalDate.now().plusMonths(12), LocalDate.now().plusMonths(18),
//                "Not yet started", ""));
//        dao.insertAssessment(new Assessment(
//                1, "Final", LocalDate.now().minusMonths(6).minusDays(1), true));
//        dao.insertAssessment(new Assessment(
//                2, "Final", LocalDate.now().plusMonths(11), false));
//        dao.insertAssessment(new Assessment(
//                3, "Final", LocalDate.now().plusMonths(16), true));
    }

    @After
    public void deleteDb() throws IOException {
        db.close();
    }

    @Test
    public void insertTerm() {
        int beforeInsert = dao.getAllTerms().size();
        Term newTerm = new Term("Test term", LocalDate.now(), LocalDate.now().plusMonths(6));

        dao.insertTerm(newTerm);
        int afterInsert = dao.getAllTerms().size();

        assertEquals(afterInsert, beforeInsert + 1);
    }

    @Test
    public void deleteTerm() {
        Term newTerm = new Term("Delete me", LocalDate.now(), LocalDate.now().plusMonths(6));
        dao.insertTerm(newTerm);

        dao.deleteTerm(newTerm);

        assertEquals(dao.getAllTerms().size(), 0);
    }

    @Test
    public void deleteAllTerms() {
    }

    @Test
    public void getTerm() {
    }

    @Test
    public void testGetTerm() {
    }

    @Test
    public void getAllTerms() {
    }

    @Test
    public void updateTerms() {
    }

    @Test
    public void insertCourse() {
    }

    @Test
    public void deleteCourse() {
    }

    @Test
    public void deleteAllCourses() {
    }

    @Test
    public void getCourse() {
    }

    @Test
    public void getAllCourses() {
    }

    @Test
    public void getCourses() {
    }

    @Test
    public void updateCourses() {
    }

    @Test
    public void insertAssessment() {
    }

    @Test
    public void deleteAssessment() {
    }

    @Test
    public void deleteAllAssessments() {
    }

    @Test
    public void getAllAssessments() {
    }

    @Test
    public void getAssessments() {
    }

    @Test
    public void getAssessment() {
    }

    @Test
    public void updateAssessments() {
    }

    @Test
    public void insertMentor() {
    }

    @Test
    public void deleteMentor() {
    }

    @Test
    public void deleteAllMentors() {
    }

    @Test
    public void getMentor() {
    }

    @Test
    public void getMentors() {
    }

    @Test
    public void updateMentors() {
    }
}