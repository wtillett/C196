package com.wtillett.ClassTracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wtillett.ClassTracker.database.AppDatabase;
import com.wtillett.ClassTracker.database.Course;
import com.wtillett.ClassTracker.database.Term;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class TermDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText termTitle;
    private TextView termStartDate, termEndDate;
    public static final String TERM_ID = "term_id";
    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.getDatabase(getApplicationContext());

        termTitle = findViewById(R.id.termTitle);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);
        ImageButton startDateButton = findViewById(R.id.startDateButton);
        ImageButton endDateButton = findViewById(R.id.endDateButton);

        Intent intent = getIntent();
        // If a new term is being added, termId will be set to -1
        int termId = intent.getIntExtra(TERM_ID, -1);

        if (termId != -1) {
            term = db.appDao().getTerm(termId);
            this.setTitle(R.string.edit_term);
            termTitle.setText(term.title);
            termStartDate.setText(term.startDate.toString());
            termEndDate.setText(term.endDate.toString());
        } else {
            term = new Term();
            this.setTitle(R.string.add_term);
        }

        setRecyclerView();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(termStartDate);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(termEndDate);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            deleteTerm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog(final TextView textView) {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);
                        textView.setText(localDate.toString());
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // Ensures recyclerview refreshes when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        ArrayList<Course> courses = new ArrayList<>(db.appDao().getCourses(term.id));
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
        term.startDate = LocalDate.parse(termStartDate.getText().toString());
        term.endDate = LocalDate.parse(termEndDate.getText().toString());

        if (db.appDao().getTerm(term.id) == null) {
            db.appDao().insertTerm(term);
        } else {
            db.appDao().updateTerms(term);
        }

        finish();
    }

    public void deleteTerm() {
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
