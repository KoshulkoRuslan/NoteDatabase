package com.example.notedatabase.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "Сработал ресивер после загрузки");
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(BootNotificationWorker.class)
                .build();
        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }
}
