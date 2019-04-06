package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;

public class AssessmentDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private Assessment assessment;
    private EditText assessmentTitle, assessmentGoalDate;
    private Switch isObjectiveSwitch;
    public static final String ASSESSMENT_ID = "assessment_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        db = AppDatabase.getDatabase(getApplicationContext());

        TextView assessmentDetailHeader = findViewById(R.id.assessmentDetailHeader);
        assessmentTitle = findViewById(R.id.assessmentTitle);
        assessmentGoalDate = findViewById(R.id.assessmentGoalDate);
        isObjectiveSwitch = findViewById(R.id.isObjectiveSwitch);

        Intent intent = getIntent();
        // If a new assessment is being added, assessmentId will be set to -1
        int assessmentId = intent.getIntExtra(ASSESSMENT_ID, -1);
        int courseId = intent.getIntExtra(CourseDetailActivity.COURSE_ID, -1);

        if (assessmentId != -1) {
            assessment = db.appDao().getAssessment(assessmentId);
            assessmentDetailHeader.setText(R.string.edit_assessment);
            assessmentTitle.setText(assessment.title);
            assessmentGoalDate.setText(assessment.goalDate);
            isObjectiveSwitch.setChecked(assessment.isObjective);
        } else if (courseId != -1) {
            assessment = new Assessment();
            assessment.courseId = courseId;
            assessmentDetailHeader.setText(R.string.add_assessment);
        }
    }

    public void saveAssessment(View view) {
        assessment.title = assessmentTitle.getText().toString();
        assessment.goalDate = assessmentGoalDate.getText().toString();
        assessment.isObjective = isObjectiveSwitch.isChecked();

        if (db.appDao().getAssessment(assessment.id) == null)
            db.appDao().insertAssessment(assessment);
        else
            db.appDao().updateAssessments(assessment);

        finish();
    }

    public void deleteAssessment(View view) {
        db.appDao().deleteAssessment(assessment);
        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }
}
