package com.wtillett.c196project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;

import java.util.ArrayList;
import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.getDatabase(getApplicationContext());

        setRecyclerView();
    }

    // Ensures recyclerview refreshes when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Context context = AssessmentActivity.this;
            Intent intent = new Intent(context, AssessmentDetailActivity.class);
            context.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        ArrayList<Assessment> assessments = new ArrayList<>(db.appDao().getAllAssessments());
        GenericAdapter adapter = new GenericAdapter(this, assessments);
        adapter.setDb(db);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
