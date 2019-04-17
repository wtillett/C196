package com.wtillett.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wtillett.c196project.database.AppDatabase;

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
}
