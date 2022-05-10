package com.rajasahabacademy.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.activity.CourseDetailActivity;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.job_alert.activity.JobAlertActivity;
import com.rajasahabacademy.activity.notification.activity.NotificationActivity;
import com.rajasahabacademy.activity.quiz.activity.QuizReadyForTestActivity;
import com.rajasahabacademy.api.Constants;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    RemoteMessage remoteMessage;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
        sendNotification(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        try {
            Map<String, String> map = remoteMessage.getData();
            String courseId = "";
            String quizId = "";
            String duration = "";
            String image = "";
            Bitmap bitmap;
            String title = map.get("title");
            image = map.get("image");
            String type = map.get("is_type");
            if (type.equalsIgnoreCase("course")) {
                courseId = map.get("course_id");
            } else if (type.equalsIgnoreCase("quiz")) {
                quizId = map.get("quiz_id");
                duration = map.get("duration");
            }


            NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
            notiStyle.bigText(title);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "order")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(title)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(R.drawable.ic_app_icon)
                    .setStyle(notiStyle)
                    .setGroupSummary(true)
                    .setGroup("Group");
            Intent resultIntent;
            if (type.equalsIgnoreCase("course")) {
                resultIntent = new Intent(this, CourseDetailActivity.class);
                resultIntent.putExtra(Constants.Course.COURSE_ID, courseId);
                resultIntent.putExtra(Constants.Course.FROM_WHERE, Constants.Course.FROM_WHERE_VALUE);
            } else if (type.equalsIgnoreCase("quiz")) {
                resultIntent = new Intent(this, QuizReadyForTestActivity.class);
                resultIntent.putExtra(Constants.Params.TEST_ID, quizId);
                resultIntent.putExtra(Constants.Params.TEST_DURATION, duration);
                resultIntent.putExtra(Constants.Quiz.FROM_WHERE, Constants.Quiz.FROM_WHERE_VALUE);
            } else if (type.equalsIgnoreCase("manual")) {
                resultIntent = new Intent(this, NotificationActivity.class);
            } else if (type.equalsIgnoreCase("job alert")) {
                resultIntent = new Intent(this, JobAlertActivity.class);
            } else resultIntent = new Intent(this, HomeActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("order",
                        getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId("order");
                mNotificationManager.notify(1, mBuilder.build());
            }

            if (image != null && !image.equalsIgnoreCase("")) {
                Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (resource != null) {
                                    mBuilder.setLargeIcon(resource);
                                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
                                    Notification notification = mBuilder.build();
                                    mNotificationManager.notify(1, mBuilder.build());
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            } else
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background));

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = remoteMessage.getData();
            NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
            notiStyle.bigText(map.get("title"));

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "order")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(map.get("title"))
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(R.drawable.ic_app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background))
                    .setStyle(notiStyle)
                    .setGroupSummary(true)
                    .setGroup("Group");
            Intent resultIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("order",
                        getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId("order");
                mNotificationManager.notify(1, mBuilder.build());
            }
        }
    }
}

