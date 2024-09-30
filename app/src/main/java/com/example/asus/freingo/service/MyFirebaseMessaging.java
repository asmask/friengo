package com.example.asus.freingo.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.asus.freingo.activities.NavigationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.asus.freingo.R;
import java.util.ArrayList;

import androidx.core.app.NotificationCompat;

/**
 * Created by ASUS on 29/03/2019.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {

    public static ArrayList<com.example.asus.freingo.models.Notification> notifications = new ArrayList<>();


  @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {

            showNotification(remoteMessage.getNotification());
            com.example.asus.freingo.models.Notification notification=new com.example.asus.freingo.models.Notification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
            notifications.add(notification);

        }
    }

    private void showNotification(RemoteMessage.Notification notification) {

        Intent intent=new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        setDefaults("NotifTitle",notification.getTitle(),this);
        setDefaults("NotifTexte",notification.getBody(),this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }

   /* @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null){
           showNotification(remoteMessage);
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
                //.setContentIntent(pendingIntent);
        notificationManager.notify(0,builder.build());
        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    }*/
   public static void setDefaults(String key, String value, Context context) {
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
       SharedPreferences.Editor editor = preferences.edit();
       editor.putString(key, value);
       editor.commit();
   }
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
