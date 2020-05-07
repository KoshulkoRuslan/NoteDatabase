package com.example.notedatabase.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.notedatabase.noteItem.Note;

@Database(entities = {Note.class},version = 1, exportSchema = false)
 public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    private static NoteDatabase instance;

    public static NoteDatabase getDatabase(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context,NoteDatabase.class,"NOTE_DATABASE_3")
                    .addCallback(callback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitDB().execute();
        }
    };

    public static class InitDB extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            instance.noteDao().insert(new Note("Заголовок напоминания","Описание заметки","18/04/2020", "14:15", 2));
            return null;
        }
    }
}
