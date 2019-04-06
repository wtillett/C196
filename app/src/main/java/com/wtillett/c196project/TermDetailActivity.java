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
import android.widget.Toast;

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
        // If a new term is being added, termId will be set to -1
        int termId = intent.getIntExtra(TERM_ID, -1);

        if (termId != -1) {
            term = db.appDao().getTerm(termId);
            termDetailHeader.setText(R.string.edit_term);
            termTitle.setText(term.title);
            termStartDate.setText(term.startDate);
            termEndDate.setText(term.endDate);
        } else {
            term = new Term();
            termDetailHeader.setText(R.string.add_term);
        }

        setRecyclerView();
    }

    // Ensures recyclerview refreshes when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        List <Course> courses = db.appDao().getCourses(term.id);
        GenericAdapter adapter = new GenericAdapter(this, courses);
        adapter.setDb(db);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addCourse(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(TERM_ID, term.id);
        context.startActivity(intent);
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

        finish();
    }
    public void deleteTerm(View view) {
        // Check to see if term has courses assigned to it
        // If it does, don't allow delete and show toast
        if (db.appDao().getCourses(term.id).size() != 0) {
            Toast.makeText(this,
                    "Cannot delete a term with courses assigned to it.",
                    Toast.LENGTH_LONG).show();
        // If not, delete the term and return to the term list
        } else {
            db.appDao().deleteTerm(term);
            finish();
        }
    }

    public void cancelEdit(View view) {
        finish();
    }
}
