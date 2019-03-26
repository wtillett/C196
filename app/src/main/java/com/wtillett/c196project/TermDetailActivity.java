package com.wtillett.c196project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

import java.util.List;

public class TermDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText termTitle, termStartDate, termEndDate;
    public static final String TERM_ID = "term_id";
    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        db = AppDatabase.getDatabase(getApplicationContext());

        TextView termDetailHeader = findViewById(R.id.termDetailHeader);
        termTitle = findViewById(R.id.termTitle);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);

        Intent intent = getIntent();
        // If a new term is being added, id will be set to -1
        int id = intent.getIntExtra(TERM_ID, -1);

        if (id != -1) {
            term = db.appDao().getTerm(id);
            termDetailHeader.setText(R.string.edit_term);
            termTitle.setText(term.title);
            termStartDate.setText(term.startDate);
            termEndDate.setText(term.endDate);
        } else {
            term = new Term();
            termDetailHeader.setText(R.string.add_term);
        }

        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        List<Course> courses = db.appDao().getCourses(term.id);
        GenericAdapter adapter = new GenericAdapter(this, courses);
        adapter.setDb(db);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addCourse(View view) {

    }

    public void saveTerm(View view) {
        term.title = termTitle.getText().toString();
        term.startDate = termStartDate.getText().toString();
        term.endDate = termEndDate.getText().toString();

        if (db.appDao().getTerm(term.id) == null) {
            db.appDao().insertTerm(term);
        } else {
            db.appDao().updateTerms(term);
        }

        launchTermActivity(view);
    }

    public void deleteTerm(View view) {
    }

    public void cancelEdit(View view) {
        finish();
    }

    private void launchTermActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, TermActivity.class);
        context.startActivity(intent);
    }
}
