package com.example.notedatabase.viewModels;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notedatabase.notifications.NotificationReceiver;
import com.example.notedatabase.noteItem.Note;
import com.example.notedatabase.repository.Repository;

import java.util.Calendar;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<Note>> allNotes;
    int requestCode = 0;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        allNotes = repository.getNotesList();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }


    public void insertNote(Note note){
        int requestCode = (int) repository.insertNote(note);
        Log.d("TAG", "Note id" + requestCode);
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
            AlarmManager am = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplication().getApplicationContext(), NotificationReceiver.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("description", note.getDescription());
            intent.putExtra("requestCode", requestCode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication().getApplicationContext(), requestCode, intent, 0);
            am.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }

    }

    public void updateNote(Note note){
        repository.updateNote(note);
    }

    public void deleteNote(Note note){
        repository.deleteNote(note);
        int requestCode = note.getId();

        AlarmManager am = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplication().getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("requestCode", requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication().getApplicationContext(), requestCode, intent, 0);
        am.cancel(pendingIntent);
    }

}
