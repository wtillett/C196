package com.wtillett.c196project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;

import java.time.LocalDate;
import java.util.ArrayList;

public class AssessmentsInTheNextWeekActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_in_the_next_week);

        db = AppDatabase.getDatabase(getApplicationContext());
        ArrayList<Assessment> assessments = new ArrayList<>(db.appDao().getAllAssessments());
        assessments.removeIf(a -> !isThisWeek(a));

        setRecyclerView(assessments);
    }

    private void setRecyclerView(ArrayList<Assessment> assessments) {
        GenericAdapter adapter = new GenericAdapter(this, assessments);
        adapter.setDb(db);
        RecyclerView recyclerView = findViewById(R.id.aitnwRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean isThisWeek(Assessment a) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate oneWeekPlus = yesterday.plusDays(9);
        return a.goalDate.isBefore(oneWeekPlus) && a.goalDate.isAfter(yesterday);
    }
}