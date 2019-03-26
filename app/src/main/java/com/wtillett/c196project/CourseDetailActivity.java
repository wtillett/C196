package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    public static final String COURSE_ID = "course_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        TextView courseDetailHeader = findViewById(R.id.courseDetailHeader);
        EditText courseTitle = findViewById(R.id.courseTitle);
        EditText courseStartDate = findViewById(R.id.courseStartDate);
        EditText courseEndDate = findViewById(R.id.courseEndDate);
        EditText courseStatus = findViewById(R.id.courseStatus);
        EditText courseNotes = findViewById(R.id.courseNotes);

        Intent intent = getIntent();
        // If a new course is being added, id will be set to -1
        int id = intent.getIntExtra(COURSE_ID, -1);

        Course course;
        if (id != 1) {
            course = db.appDao().getCourse(id);
            courseDetailHeader.setText(R.string.edit_course);
            courseTitle.setText(course.title);
            courseStartDate.setText(course.startDate);
            courseEndDate.setText(course.endDate);
            courseStatus.setText(course.status);
            courseNotes.setText(course.notes);
        } else {
            course = new Course();
            courseDetailHeader.setText(R.string.add_course);
        }

        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        List<Assessment> assessments = db.appDao().getAssessments(course.id);
        GenericAdapter adapter = new GenericAdapter(this, assessments);
        adapter.setDb(db);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
