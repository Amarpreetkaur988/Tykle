package com.tykle.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tykle.R;
import com.tykle.activity.DialogsActivity;
import com.tykle.activity.MatchesRequestsActivity;
import com.tykle.activity.PeopleActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;
    NotificationChannel mChannel;
    Notification notification;
    Uri defaultSound;
    Intent[] intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        final String title = remoteMessage.getData().get("title");
        final String message = remoteMessage.getData().get("message");
        final String type = remoteMessage.getData().get("type");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setOreoNotification(title, message, type);
        } else {
            showNotification(title, message, type);
        }

    }

    private void showNotification(String title, String message, String type) {

        if (type.equalsIgnoreCase("tykle")) {

            intent = new Intent[]{new Intent(this, MatchesRequestsActivity.class)};

        } else if (type.equalsIgnoreCase("match")) {

            intent = new Intent[]{new Intent(this, MatchesRequestsActivity.class)};

        } else if (type.equalsIgnoreCase("message")) {

            intent = new Intent[]{new Intent(this, DialogsActivity.class)};
        }




        PendingIntent pendingIntent = PendingIntent.getActivities(this, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification = new NotificationCompat.Builder(this)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.app_icon)
                .setSound(defaultSound)
                .setAutoCancel(true)
                .build();


//        Notification notification = new Notification.InboxStyle(new Notification.Builder(this)
//                .setTicker(message)
//                .setSmallIcon(R.drawable.app_icon)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(title)
//                .setNumber(4)
//                .setContentIntent(pendingIntent))
//                .addLine(message)
//                .addLine("Second Message")
//                .addLine("Third Message")
//                .addLine("Fourth Message")
//                .setBigContentTitle("Here Your Messages")
//                .setSummaryText("+3 more")
//                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);


    }


    private void setOreoNotification(String title, String message, String type) {

        if (type.equalsIgnoreCase("tykle")) {

            intent = new Intent[]{new Intent(this, MatchesRequestsActivity.class)};

        } else if (type.equalsIgnoreCase("match")) {

            intent = new Intent[]{new Intent(this, MatchesRequestsActivity.class)};

        } else if (type.equalsIgnoreCase("message")) {

            intent = new Intent[]{new Intent(this, DialogsActivity.class)};
        }

        PendingIntent pendingIntent = PendingIntent.getActivities(this, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Sets an ID for the notification, so it can be updated.
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "tykle";// The user-visible name of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.app_icon)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
        }


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

// Issue the notification.
        mNotificationManager.notify(NOTIFICATION_ID, notification);


    }
}
