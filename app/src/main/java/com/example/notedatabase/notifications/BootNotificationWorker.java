package com.example.notedatabase.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.notedatabase.notifications.NotificationReceiver;
import com.example.notedatabase.noteItem.Note;
import com.example.notedatabase.repository.Repository;
import java.util.Calendar;
import java.util.List;


public class BootNotificationWorker extends Worker {
    Repository repository;
    List<Note> list;

    public BootNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("TAG", "Создан myWorker ");
        repository = new Repository(getApplicationContext());
        list = repository.getAllNotes();
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        for (Note note:list){
            int requestCode = note.getId();
            Log.d("TAG", "Устанавливаем аларм на элемент с id  " + requestCode);
            //Устанавливаем время
            Calendar c = Calendar.getInstance();
            String[] dates = note.getDate().split("/");
            String[] time = note.getTime().split(":");
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[0]));
            c.set(Calendar.MONTH, Integer.parseInt(dates[1])-1);
            c.set(Calendar.YEAR, Integer.parseInt(dates[2]));
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            c.set(Calendar.SECOND,0);
            //Устанавливаем время
            if (c.after(Calendar.getInstance())){
                Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("description", note.getDescription());
                intent.putExtra("requestCode", requestCode);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);
                am.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
            }
        }


        return Result.success();
    }
}
