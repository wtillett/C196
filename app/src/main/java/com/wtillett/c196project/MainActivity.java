package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    // TODO: Add application title and icon
    // TODO: Programmatically add to the UI
    // TODO: Make landscape layout variants where necessary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(getApplicationContext());
    }

    public void launchTermActivity(View view) {
        startActivity(new Intent(this, TermActivity.class));
    }

    public void launchCourseActivity(View view) {
        startActivity(new Intent(this, CourseActivity.class));
    }

    public void launchAssessmentActivity(View view) {
        startActivity(new Intent(this, AssessmentActivity.class));
    }

    public void populateDB(View view) {
        db.appDao().insertTerm(new Term("Term 1",
                LocalDate.parse("2019-01-01"), LocalDate.parse("2019-06-30")));
        db.appDao().insertTerm(new Term("Term 2",
                LocalDate.parse("2019-07-01"), LocalDate.parse("2019-12-31")));
        Term term = new Term("Term 3",
                LocalDate.parse("2020-01-01"), LocalDate.parse("2020-06-30"));
        db.appDao().insertTerm(term);
        db.appDao().insertCourse(new Course(term.id, "Math",
                LocalDate.parse("2019-01-01"), LocalDate.parse("2019-06-30"),
                "in progress", "sucks"));
        db.appDao().insertCourse(new Course("English",
                LocalDate.parse("2019-07-01"), LocalDate.parse("2019-12-31"),
                "just started", "is great"));
    }

    public void emptyDB(View view) {
        db.appDao().deleteAllMentors();
        db.appDao().deleteAllAssessments();
        db.appDao().deleteAllCourses();
        db.appDao().deleteAllTerms();
    }
}
