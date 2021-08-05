package com.example.newproject.Announcements;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.newproject.MainActivity;
import com.example.newproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCM_Messaging  extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification()!=null)
        {
            String title = remoteMessage.getNotification().getTitle();
            String body= remoteMessage.getNotification().getBody();
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=new NotificationChannel("notice","announcement",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);

            Intent intent=new Intent(this, MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder builder=new Notification.Builder(this,"notice")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setColor(Color.CYAN)
                    .setSound(uri);
            notificationManager.notify(0,builder.build());
        }
    }



}
