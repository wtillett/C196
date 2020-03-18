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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private AppDatabase db;
    private CharSequence currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText searchText = findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            private static final String TAG = "THIS_IS_A_TAG";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentSearch = charSequence;
                setRecyclerView(charSequence);
                Log.d(TAG, "onTextChanged: " + currentSearch);
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
            Context context = CourseActivity.this;
            Intent intent = new Intent(context, CourseDetailActivity.class);
            context.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView(CharSequence search) {
        ArrayList<Course> allCourses = new ArrayList<>(db.appDao().getAllCourses());
        ArrayList<Course> filteredCourses;
        if (search.equals("")) {
            filteredCourses = allCourses;
        } else {
            filteredCourses = (ArrayList<Course>) allCourses.clone();
        }
        filteredCourses.removeIf(c -> !c.title.toLowerCase().contains(search.toString().toLowerCase()));
        GenericAdapter adapter = new GenericAdapter(this, filteredCourses);
        adapter.setDb(db);
        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
