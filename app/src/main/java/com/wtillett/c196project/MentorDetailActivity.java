package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Mentor;

public class MentorDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText mentorName, mentorEmail, mentorPhone;
    private Mentor mentor;
    public static final String MENTOR_ID = "mentor_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.getDatabase(getApplicationContext());

        TextView mentorDetailHeader = findViewById(R.id.mentorDetailHeader);
        mentorName = findViewById(R.id.mentorName);
        mentorEmail = findViewById(R.id.mentorEmail);
        mentorPhone = findViewById(R.id.mentorPhone);

        Intent intent = getIntent();
        // If a new mentor is being added, mentorId will be set to -1
        int mentorId = intent.getIntExtra(MENTOR_ID, -1);
        int courseId = intent.getIntExtra(CourseDetailActivity.COURSE_ID, -1);

        if (mentorId != -1) {
            mentor = db.appDao().getMentor(mentorId);
            mentorDetailHeader.setText(R.string.edit_mentor);
            mentorName.setText(mentor.name);
            mentorEmail.setText(mentor.email);
            mentorPhone.setText(mentor.phone);
        } else if (courseId != -1) {
            mentor = new Mentor();
            mentor.courseId = courseId;
            mentorDetailHeader.setText(R.string.add_mentor);
        }
    }

    public void saveMentor(View view) {
        mentor.name = mentorName.getText().toString();
        mentor.email = mentorEmail.getText().toString();
        mentor.phone = mentorPhone.getText().toString();

        if (db.appDao().getMentor(mentor.id) == null)
            db.appDao().insertMentor(mentor);
        else
            db.appDao().updateMentors(mentor);

        finish();
    }

    public void deleteMentor(View view) {
        db.appDao().deleteMentor(mentor);
        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }
}
