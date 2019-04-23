package com.wtillett.c196project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class AssessmentDetailActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String ASSESSMENT_ID = "assessment_id";
    private static final String ASSESSMENT_CHANNEL_ID = "assessment_notification_channel";
    Intent notifyIntent;
    PendingIntent notifyPendingIntent;
    AlarmManager alarmManager;
    private AppDatabase db;
    private Assessment assessment;
    private EditText assessmentTitle, assessmentGoalDate;
    private Switch isObjectiveSwitch;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.getDatabase(getApplicationContext());

        assessmentTitle = findViewById(R.id.assessmentTitle);
        assessmentGoalDate = findViewById(R.id.assessmentGoalDate);
        isObjectiveSwitch = findViewById(R.id.isObjectiveSwitch);
        ImageButton goalDateButton = findViewById(R.id.goalDateButton);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmToggle.setChecked(false);

        Intent intent = getIntent();
        // If a new assessment is being added, assessmentId will be set to -1
        int assessmentId = intent.getIntExtra(ASSESSMENT_ID, -1);
        int courseId = intent.getIntExtra(CourseDetailActivity.COURSE_ID, -1);

        if (assessmentId != -1) {
            assessment = db.appDao().getAssessment(assessmentId);
            this.setTitle(R.string.edit_assessment);
            assessmentTitle.setText(assessment.title);
            assessmentGoalDate.setText(assessment.goalDate.toString());
            isObjectiveSwitch.setChecked(assessment.isObjective);
            alarmToggle.setEnabled(true);
        } else {
            assessment = new Assessment();
            if (courseId != -1)
                assessment.courseId = courseId;
            this.setTitle(R.string.add_assessment);
            alarmToggle.setEnabled(false);
        }

        goalDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(assessmentGoalDate);
            }
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyIntent = new Intent(this, AlarmReceiver.class);
        notifyIntent.putExtra(ASSESSMENT_ID, assessment.id);

        boolean alarmUp = (PendingIntent.getBroadcast(this, assessment.id, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmToggle.setOnCheckedChangeListener(this);

        createNotificationChannel();
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
            deleteAssessment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ASSESSMENT_CHANNEL_ID,
                    "Assessment Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setDescription("Notifies on the goal date of an assessment");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog dialog = new DatePickerDialog(
                AssessmentDetailActivity.this,
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

    public void saveAssessment(View view) {
        assessment.title = assessmentTitle.getText().toString();
        assessment.goalDate = LocalDate.parse(assessmentGoalDate.getText().toString());
        assessment.isObjective = isObjectiveSwitch.isChecked();

        if (db.appDao().getAssessment(assessment.id) == null)
            db.appDao().insertAssessment(assessment);
        else
            db.appDao().updateAssessments(assessment);

        finish();
    }

    public void deleteAssessment() {
        db.appDao().deleteAssessment(assessment);
        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            notifyPendingIntent = PendingIntent.getBroadcast
                    (this, assessment.id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            LocalDateTime dt = assessment.goalDate.atStartOfDay();
            long millis =
                    dt.toInstant(ZoneOffset.UTC).toEpochMilli();
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    millis,
                    notifyPendingIntent);
        } else {
            if (alarmManager != null) {
                alarmManager.cancel(notifyPendingIntent);
                notificationManager.cancel(assessment.id);
                notifyPendingIntent = null;
            }
        }
    }
}
