package com.wtillett.ClassTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wtillett.ClassTracker.database.AppDatabase;

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
