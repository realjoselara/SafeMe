package com.latinocodes.safeme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String Title = remoteMessage.getNotification().getTitle();
        String bodytext = remoteMessage.getNotification().getBody();

        dispNotify(getApplicationContext(), Title, bodytext);
    }

    //Building Notification Message
    public static void dispNotify(Context context, String messageTitle, String messageBody){
        NotificationCompat.Builder messagebuild =
                new NotificationCompat.Builder(context, SafeMeActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.safemelogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //Build Message & Notify
        NotificationManagerCompat notfimanager = NotificationManagerCompat.from(context);
        notfimanager.notify(1, messagebuild.build());

    }
}


