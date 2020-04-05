package com.wtillett.ClassTracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wtillett.ClassTracker.database.AppDatabase;
import com.wtillett.ClassTracker.database.Assessment;
import com.wtillett.ClassTracker.database.Course;
import com.wtillett.ClassTracker.database.Mentor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;

public class CourseDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final String COURSE_ID = "course_id";
    private static final String NOTIFICATION_ID = "notification_id";
    private static final String START_DATE_FLAG = "start_date_flag";
    private static final String COURSE_CHANNEL_ID = "course_notification_channel";
    PendingIntent notifyStartDatePendingIntent;
    PendingIntent notifyEndDatePendingIntent;
    int startNotificationID;
    int endNotificationID;
    Intent startDateNotifyIntent;
    Intent endDateNotifyIntent;
    private AppDatabase db;
    private Course course;
    private EditText courseTitle, courseStartDate, courseEndDate, courseNotes;
    private Spinner courseStatus;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.getDatabase(getApplicationContext());

        courseTitle = findViewById(R.id.courseTitle);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        courseNotes = findViewById(R.id.courseNotes);
        ImageButton startDateButton = findViewById(R.id.startDateButton);
        ImageButton endDateButton = findViewById(R.id.endDateButton);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmToggle.setChecked(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatus.setAdapter(adapter);
        courseStatus.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        // If a new course is being added, courseId will be set to -1
        int courseId = intent.getIntExtra(COURSE_ID, -1);
        int termId = intent.getIntExtra(TermDetailActivity.TERM_ID, -1);

        if (courseId != -1) {
            course = db.appDao().getCourse(courseId);
            this.setTitle(R.string.edit_course);
            courseTitle.setText(course.title);
            courseStartDate.setText(course.startDate.toString());
            courseEndDate.setText(course.endDate.toString());
            courseStatus.setSelection(getPosition(course.status));
            courseNotes.setText(course.notes);
            alarmToggle.setEnabled(true);
        } else {
            course = new Course();
            if (termId != -1)
                course.termId = termId;
            this.setTitle(R.string.add_course);
            alarmToggle.setEnabled(false);
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        startDateNotifyIntent = new Intent(this, AlarmReceiver.class);
        endDateNotifyIntent = new Intent(this, AlarmReceiver.class);
        startDateNotifyIntent.putExtra(COURSE_ID, course.id);
        endDateNotifyIntent.putExtra(COURSE_ID, course.id);

        if (course.startDate == null)
            startNotificationID = -1;
        else
            startNotificationID = course.id + course.startDate.getDayOfYear();
        if (course.endDate == null)
            endNotificationID = -1;
        else
            endNotificationID = course.id + course.endDate.getDayOfYear();
        startDateNotifyIntent.putExtra(NOTIFICATION_ID, startNotificationID);
        startDateNotifyIntent.putExtra(START_DATE_FLAG, true);
        endDateNotifyIntent.putExtra(NOTIFICATION_ID, endNotificationID);

        boolean alarmUp = (PendingIntent.getBroadcast(this, startNotificationID,
                startDateNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmToggle.setOnCheckedChangeListener(this);

        createNotificationChannel();

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

    private int getPosition(String status) {
        int index = 0;
        for (int i = 0; i < courseStatus.getCount(); i++) {
            if (courseStatus.getItemAtPosition(i).equals(status))
                index = i;
        }
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            shareNotes();
            return true;
        } else if (id == R.id.action_delete) {
            deleteCourse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareNotes() {
        String text = course.title + ":\n" + course.notes;
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Share course notes with: ")
                .setText(text)
                .startChooser();
    }

    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    COURSE_CHANNEL_ID,
                    "Course notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setDescription("Notifies on the start and end date of a course");
            notificationManager.createNotificationChannel(channel);
        }
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
        course.notes = courseNotes.getText().toString();

        if (course.startDate == course.endDate) {
            Toast.makeText(this, "Start date must be before end date.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (db.appDao().getCourse(course.id) == null)
            db.appDao().insertCourse(course);
        else db.appDao().updateCourses(course);

        finish();
    }

    public void deleteCourse() {
        // Check to see if course has assessments assigned to it
        if (db.appDao().getAssessments(course.id).size() != 0)
            // If it does, don't allow delete and show toast
            Toast.makeText(this,
                    "Cannot delete a course with assessments assigned to it.",
                    Toast.LENGTH_LONG).show();
        else
            // If not, delete the course and any notifications and return to the appropriate
            // activity
            if (alarmManager != null) {
                alarmManager.cancel(notifyStartDatePendingIntent);
                alarmManager.cancel(notifyEndDatePendingIntent);
                notificationManager.cancel(startNotificationID);
                notificationManager.cancel(endNotificationID);
            }
        db.appDao().deleteCourse(course);

        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        course.status = ((String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        course.status = "";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            notifyStartDatePendingIntent = PendingIntent.getBroadcast
                    (CourseDetailActivity.this, startNotificationID,
                            startDateNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notifyEndDatePendingIntent = PendingIntent.getBroadcast
                    (CourseDetailActivity.this, endNotificationID,
                            endDateNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            long startDateMillis = course.startDate.atStartOfDay()
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            long endDateMillis = course.endDate.atStartOfDay()
                    .toInstant(ZoneOffset.UTC).toEpochMilli();
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    startDateMillis,
                    notifyStartDatePendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    endDateMillis,
                    notifyEndDatePendingIntent);
        } else {
            if (alarmManager != null) {
                alarmManager.cancel(notifyStartDatePendingIntent);
                alarmManager.cancel(notifyEndDatePendingIntent);
                notificationManager.cancel(startNotificationID);
                notificationManager.cancel(endNotificationID);
                startDateNotifyIntent = null;
                endDateNotifyIntent = null;
            }
        }
    }
}
