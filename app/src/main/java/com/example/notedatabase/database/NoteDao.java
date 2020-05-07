package com.example.notedatabase.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.notedatabase.noteItem.Note;
import java.util.List;


@Dao
public interface NoteDao {

    @Query("SELECT * from Note ORDER BY id ASC")
    LiveData<List<Note>> getNotesList();
    @Query("SELECT * from Note ORDER BY id ASC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM Note WHERE title =:title")
    List<Note> getNoteById(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Note note);

    @Query("DELETE FROM Note")
    void deleteAll();

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

}
