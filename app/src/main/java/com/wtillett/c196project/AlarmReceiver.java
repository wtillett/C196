package com.wtillett.c196project;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String COURSE_CHANNEL_ID = "course_notification_channel";
    private static final String COURSE_ID = "course_id";
    private static final String ASSESSMENT_ID = "assessment_id";
    private Course course;
    private Assessment assessment;
    private AppDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        int courseId = intent.getIntExtra(COURSE_ID, -1);
        int assessmentId = intent.getIntExtra(ASSESSMENT_ID, -1);
        if (courseId != -1)
            course = db.appDao().getCourse(courseId);
        if (assessmentId != -1)
            assessment = db.appDao().getAssessment(assessmentId);
        deliverNotification(context);
    }

    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, CourseDetailActivity.class);

    }
}
