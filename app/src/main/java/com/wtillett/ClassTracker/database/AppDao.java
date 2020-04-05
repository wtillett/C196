package com.wtillett.ClassTracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AppDao {

    // Term methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table WHERE id = :id")
    Term getTerm(Integer id);

    @Query("SELECT * FROM term_table WHERE title = :title")
    Term getTerm(String title);

    @Query("SELECT * FROM term_table")
    List<Term> getAllTerms();

    @Update
    void updateTerms(Term... terms);

    // Course methods
    @Insert
    void insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM course_table")
    void deleteAllCourses();

    @Query("SELECT * FROM course_table WHERE id = :id")
    Course getCourse(Integer id);

    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course_table WHERE termId = :termId")
    List<Course> getCourses(Integer termId);

    @Update
    void updateCourses(Course... courses);

    // Assessment methods
    @Insert
    void insertAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE courseId = :courseId")
    List<Assessment> getAssessments(Integer courseId);

    @Query("SELECT * FROM assessment_table WHERE id = :id")
    Assessment getAssessment(Integer id);

    @Update
    void updateAssessments(Assessment... assessments);

    // Mentor methods
    @Insert
    void insertMentor(Mentor mentor);

    @Delete
    void deleteMentor(Mentor mentor);

    @Query("DELETE FROM mentor_table")
    void deleteAllMentors();

    @Query("SELECT * FROM mentor_table WHERE id = :id")
    Mentor getMentor(Integer id);

    @Query("SELECT * FROM mentor_table WHERE courseId = :courseId")
    List<Mentor> getMentors(Integer courseId);

    @Update
    void updateMentors(Mentor... mentors);

}
