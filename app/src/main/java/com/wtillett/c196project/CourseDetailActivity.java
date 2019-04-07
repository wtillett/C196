package com.wtillett.c196project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Mentor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class CourseDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private Course course = new Course();
    private EditText courseTitle, courseStartDate, courseEndDate, courseStatus, courseNotes;
    public static final String COURSE_ID = "course_id";

    // TODO: Implement alerts for start and end dates
    // TODO: Implement note sharing via e-mail or SMS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        db = AppDatabase.getDatabase(getApplicationContext());

        TextView courseDetailHeader = findViewById(R.id.courseDetailHeader);
        courseTitle = findViewById(R.id.courseTitle);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        courseNotes = findViewById(R.id.courseNotes);
        ImageButton startDateButton = findViewById(R.id.startDateButton);
        ImageButton endDateButton = findViewById(R.id.endDateButton);

        Intent intent = getIntent();
        // If a new course is being added, courseId will be set to -1
        int courseId = intent.getIntExtra(COURSE_ID, -1);
        int termId = intent.getIntExtra(TermDetailActivity.TERM_ID, -1);

        if (courseId != -1) {
            course = db.appDao().getCourse(courseId);
            courseDetailHeader.setText(R.string.edit_course);
            courseTitle.setText(course.title);
            courseStartDate.setText(course.startDate.toString());
            courseEndDate.setText(course.endDate.toString());
            courseStatus.setText(course.status);
            courseNotes.setText(course.notes);
        } else if (termId != -1) {
            course = new Course();
            course.termId = termId;
            courseDetailHeader.setText(R.string.add_course);
        }

        setRecyclerViews();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(courseStartDate);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(courseEndDate);
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);
                        editText.setText(localDate.toString());
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
        setRecyclerViews();
    }

    private void setRecyclerViews() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(COURSE_ID, -1);

        if (id != -1) {
            RecyclerView assessmentRecyclerView = findViewById(R.id.assessmentRecyclerView);
            ArrayList<Assessment> assessments = new ArrayList<>(db.appDao().getAssessments(course.id));
            GenericAdapter assessmentAdapter = new GenericAdapter(this, assessments);
            assessmentAdapter.setDb(db);
            assessmentRecyclerView.setAdapter(assessmentAdapter);
            assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            RecyclerView mentorRecyclerView = findViewById(R.id.mentorRecyclerView);
            ArrayList<Mentor> mentors = new ArrayList<>(db.appDao().getMentors(course.id));
            GenericAdapter mentorAdapter = new GenericAdapter(this, mentors);
            mentorAdapter.setDb(db);
            mentorRecyclerView.setAdapter(mentorAdapter);
            mentorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void addAssessment(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AssessmentDetailActivity.class);
        intent.putExtra(COURSE_ID, course.id);
        context.startActivity(intent);
    }

    public void addMentor(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MentorDetailActivity.class);
        intent.putExtra(COURSE_ID, course.id);
        context.startActivity(intent);
    }

    public void saveCourse(View view) {
        course.title = courseTitle.getText().toString();
        course.startDate = LocalDate.parse(courseStartDate.getText().toString());
        course.endDate = LocalDate.parse(courseEndDate.getText().toString());
        course.status = courseStatus.getText().toString();
        course.notes = courseNotes.getText().toString();

        if (db.appDao().getCourse(course.id) == null)
            db.appDao().insertCourse(course);
        else db.appDao().updateCourses(course);

        finish();
    }

    public void deleteCourse(View view) {
        // Check to see if course has assessments assigned to it
        if (db.appDao().getAssessments(course.id).size() != 0)
            // If it does, don't allow delete and show toast
            Toast.makeText(this,
                    "Cannot delete a course with assessments assigned to it.",
                    Toast.LENGTH_LONG).show();
        else
            // If not, delete the course and return to the appropriate activity
            db.appDao().deleteCourse(course);

        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }
}
