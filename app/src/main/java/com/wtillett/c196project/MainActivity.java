package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

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
        db.appDao().insertTerm(new Term("Term 1", "01/01/2019", "06/30/2019"));
        db.appDao().insertTerm(new Term("Term 2", "07/01/2019", "12/31/2019"));
        Term term = new Term("Term 3", "01/01/2020", "butt");
        db.appDao().insertTerm(term);
        db.appDao().insertCourse(new Course(term.id, "Math", "123", "456",
                "in progress", "sucks"));
        db.appDao().insertCourse(new Course("English", "234", "567",
                "just started", "is great"));
    }

    public void emptyDB(View view) {
        db.appDao().deleteAllMentors();
        db.appDao().deleteAllAssessments();
        db.appDao().deleteAllCourses();
        db.appDao().deleteAllTerms();
    }
}
