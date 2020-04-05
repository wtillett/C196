package com.wtillett.ClassTracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.wtillett.ClassTracker.database.AppDatabase;
import com.wtillett.ClassTracker.database.Assessment;

import java.util.ArrayList;

public class AssessmentActivity extends AppCompatActivity {

    private AppDatabase db;
    private CharSequence currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText searchText = findViewById(R.id.assessmentSearchText);
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentSearch = charSequence;
                setRecyclerView(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        db = AppDatabase.getDatabase(getApplicationContext());

        setRecyclerView(currentSearch);
    }

    // Ensures recyclerview refreshes when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView(currentSearch);
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

    private void setRecyclerView(CharSequence search) {
        ArrayList<Assessment> allAssessments = new ArrayList<>(db.appDao().getAllAssessments());
        ArrayList<Assessment> filteredAssessments =
                (ArrayList<Assessment>) allAssessments.clone();
        filteredAssessments.removeIf(
                a -> !a.title.toLowerCase().contains(search.toString().toLowerCase())
        );
        GenericAdapter adapter = new GenericAdapter(this, filteredAssessments);
        adapter.setDb(db);
        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
