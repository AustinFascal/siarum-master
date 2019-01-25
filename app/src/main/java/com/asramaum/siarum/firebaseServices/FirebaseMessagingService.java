package com.asramaum.siarum.firebaseServices;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.asramaum.siarum.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by austi on 6/22/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if(componentInfo.getPackageName().equalsIgnoreCase("com.asramaum.attendance")){
                notification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
            else{
                //Activity Not Running
                //Generate Notification
                notification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }

            //notification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void notification(String title, String body) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        if(title.equals("Versi Baru Aplikasi")){
            Intent intentNewVersion = new Intent(Intent.ACTION_VIEW);
            intentNewVersion.setData(Uri.parse("https://www.google.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNewVersion,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.app_logo)
                    .setTicker("Attendance - Asrama Mahasiswa UM")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSound(soundUri)
                    .setColorized(true)
                    .setColor(this.getResources()
                                    .getColor(R.color.colorPrimary))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setContentIntent(pendingIntent)
                    .setContentInfo("Info")
                    .addAction(R.drawable.ic_update, "Perbarui", pendingIntent);
        }

        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.app_logo)
                .setTicker("Attendance - Asrama Mahasiswa UM")
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setColorized(true)
                .setColor(this.getResources()
                        .getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentInfo("Info");

       notificationManager.notify(0, notificationBuilder.build());

    }
}
