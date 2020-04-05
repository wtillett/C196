package com.wtillett.ClassTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wtillett.ClassTracker.database.AppDatabase;
import com.wtillett.ClassTracker.database.Assessment;

import java.time.LocalDate;
import java.util.ArrayList;

public class AssessmentsInTheNextWeekActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_in_the_next_week);

        TextView noAssessmentsFound = findViewById(R.id.noAssessmentsFound);

        db = AppDatabase.getDatabase(getApplicationContext());
        ArrayList<Assessment> assessments = new ArrayList<>(db.appDao().getAllAssessments());

        if (assessments.size() == 0) {
            noAssessmentsFound.setVisibility(View.VISIBLE);
        } else {
            assessments.removeIf(a -> !isThisWeek(a));
            setRecyclerView(assessments);
        }
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
