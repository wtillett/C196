package com.wtillett.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    // TODO: Add application title and icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(getApplicationContext());

        // Add a button to the UI programmatically
        addAssessmentButton();
    }

    private void addAssessmentButton() {
        ConstraintLayout constraintLayout = findViewById(R.id.mainConstraintLayout);
        Button courseButton = findViewById(R.id.courseButton);
        Button button = new Button(this);
        button.setId(View.generateViewId());
        button.setText(R.string.assessment_button_text);
        constraintLayout.addView(button);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(button.getId(), ConstraintSet.LEFT,
                constraintLayout.getId(), ConstraintSet.LEFT, 28);
        constraintSet.connect(button.getId(), ConstraintSet.RIGHT,
                constraintLayout.getId(), ConstraintSet.RIGHT, 28);
        constraintSet.connect(button.getId(), ConstraintSet.TOP,
                courseButton.getId(), ConstraintSet.BOTTOM, 56);
        constraintSet.constrainWidth(button.getId(),
                ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.applyTo(constraintLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAssessmentActivity(v);
            }
        });
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
                "In progress", "sucks"));
        db.appDao().insertCourse(new Course("English",
                LocalDate.parse("2019-07-01"), LocalDate.parse("2019-12-31"),
                "Completed", "is great"));
    }

    public void emptyDB(View view) {
        db.appDao().deleteAllMentors();
        db.appDao().deleteAllAssessments();
        db.appDao().deleteAllCourses();
        db.appDao().deleteAllTerms();
    }
}
