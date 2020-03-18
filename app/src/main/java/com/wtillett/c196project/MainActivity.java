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

    public void launchReportActivity(View view) {
        startActivity(new Intent(this, ReportActivity.class));
    }
}
