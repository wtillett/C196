package com.wtillett.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;

public class AssessmentDetailActivity extends AppCompatActivity {

    public static final String ASSESSMENT_ID = "assessment_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        TextView assessmentDetailHeader = findViewById(R.id.assessmentDetailHeader);
        EditText assessmentTitle = findViewById(R.id.assessmentTitle);
        EditText assessmentGoalDate = findViewById(R.id.assessmentGoalDate);
        Switch isObjectiveSwitch = findViewById(R.id.isObjectiveSwitch);

        Intent intent = getIntent();
        // If a new assessment is being added, id will be set to -1
        int id = intent.getIntExtra(ASSESSMENT_ID, -1);

        Assessment assessment;
        if (id != -1) {
            assessment = db.appDao().getAssessment(id);
            assessmentDetailHeader.setText(R.string.edit_assessment);
            assessmentTitle.setText(assessment.title);
            assessmentGoalDate.setText(assessment.goalDate);
            isObjectiveSwitch.setChecked(assessment.isObjective);
        } else {
            assessment = new Assessment();
            assessmentDetailHeader.setText(R.string.add_assessment);
        }
    }
}
