package com.example.notedatabase.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.notedatabase.database.NoteDao;
import com.example.notedatabase.database.NoteDatabase;
import com.example.notedatabase.noteItem.Note;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    NoteDatabase noteDatabase;
    private static NoteDao noteDao;
    LiveData<List<Note>> notesList;

    public Repository(Context context){
        noteDatabase = NoteDatabase.getDatabase(context);
        noteDao = noteDatabase.noteDao();
        notesList = noteDao.getNotesList();
        Log.d("TAG", "Создаем экземпляр репозитория");
    }

    public LiveData<List<Note>> getNotesList(){
        return notesList;
    }
    public List<Note> getAllNotes(){
        try {
            return new GetAllNotes().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long insertNote(Note note){
        try {
            return new InsetNoteAsync().execute(note).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void deleteNote(Note note){
        new DeleteNoteAsync().execute(note);
    }
    public void updateNote(Note note){
        new UpdateNoteAsync().execute(note);
    }
    public void deleteAllNotes(){
        new DeleteAllNoteAsync().execute();
    }
    public Note getNoteById(String title) {
        try {
            return new GetNoteById().execute(title).get().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static class GetAllNotes extends AsyncTask<Void,Void,List<Note>>{

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return noteDao.getAllNotes();
        }
    }

    private static class GetNoteById extends AsyncTask<String,Void,List<Note>>{
        @Override
        protected List<Note> doInBackground(String... ints) {
            Log.d("TAG", "запустили поиск записи в БД");
            return noteDao.getNoteById(ints[0]);
        }
    }

    private static class InsetNoteAsync extends AsyncTask<Note, Void, Long>{

        @Override
        protected Long doInBackground(Note... notes) {

            return  noteDao.insert(notes[0]);
        }
    }

    private static class DeleteNoteAsync extends AsyncTask<Note, Void, Void>{

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsync extends AsyncTask<Note, Void, Void>{

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.updateNote(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAll();
            return null;
        }
    }

}
