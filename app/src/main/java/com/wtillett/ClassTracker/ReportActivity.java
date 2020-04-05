package com.wtillett.ClassTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }

    public void launchCoursesPerTermReport(View view) {
        startActivity(new Intent(this, CoursesPerTermActivity.class));
    }

    public void launchAssessmentsInTheNextWeekReport(View view) {
        startActivity(new Intent(this, AssessmentsInTheNextWeekActivity.class));
    }
}
