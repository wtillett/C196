package com.wtillett.c196project;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    }
}
