package com.example.notedatabase.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notedatabase.MainActivity;
import com.example.notedatabase.R;
import com.example.notedatabase.noteItem.Note;
import com.example.notedatabase.repository.Repository;
import static com.example.notedatabase.notifications.App.CHANNEL_1_ID;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title;
        String description;
        int notificationId;
        Repository repository;
        Note note;

        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        notificationId = intent.getIntExtra("requestCode",-1);
        pushNotification(context,intent,notificationId);


    }
    private void pushNotification(Context context, Intent intent,int notificationId){
        Intent intent_activity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent_activity,0);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_access_alarm)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("description"))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {1000,1000,1000})
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(notificationId,notification);
    }

}
