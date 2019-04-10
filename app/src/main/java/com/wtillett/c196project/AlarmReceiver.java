package com.wtillett.c196project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final String ASSESSMENT_ID = "assessment_id";
    private static final String ASSESSMENT_CHANNEL_ID = "assessment_notification_channel";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_CHANNEL_ID = "course_notification_channel";
    private Assessment assessment;
    private Course course;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        int assessmentID = intent.getIntExtra(ASSESSMENT_ID, -1);
        int courseID = intent.getIntExtra(COURSE_ID, -1);
        AppDatabase db = AppDatabase.getDatabase(context);

        if (assessmentID != -1) {
            assessment = db.appDao().getAssessment(assessmentID);
            deliverAssessmentNotification(context);
        } else {
            course = db.appDao().getCourse(courseID);
            deliverCourseNotification(context);
        }
    }

    private void deliverAssessmentNotification(Context context) {
        Intent contentIntent = new Intent(context, AssessmentDetailActivity.class);
        contentIntent.putExtra(ASSESSMENT_ID, assessment.id);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context, assessment.id, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, ASSESSMENT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fab_date)
                .setContentTitle("Assessment goal date")
                .setContentText("Your goal to finish " + assessment.title + " is today!")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(assessment.id, builder.build());
    }

    private void deliverCourseNotification(Context context) {
        Intent contentIntent = new Intent(context, CourseDetailActivity.class);
        contentIntent.putExtra(COURSE_ID, course.id);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context, course.id, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, COURSE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fab_date)
                .setContentTitle("Course ending today")
                .setContentText(course.title + " is ending today!")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(course.id, builder.build());
    }
}
