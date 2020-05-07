package com.example.notedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notedatabase.noteItem.Note;
import com.example.notedatabase.view.NoteAdapter;
import com.example.notedatabase.view.NoteAddActivity;
import com.example.notedatabase.viewModels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    NoteViewModel viewModel;
    NoteAdapter adapter;
    RecyclerView recyclerView;
    NoteAddActivity noteAddActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new NoteAdapter();
        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                if (!noteAddActivity.isVisible()) {
                    noteAddActivity = new NoteAddActivity();
                    noteAddActivity.show(getSupportFragmentManager(), "EDIT_NOTE");
                    noteAddActivity.setNote(adapter.getNoteByPosition(position));
                }

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note swipedNote = adapter.getNoteByPosition(viewHolder.getAdapterPosition());
                viewModel.deleteNote(swipedNote);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        noteAddActivity = new NoteAddActivity();
        recyclerView.setAdapter(adapter);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

    }

    public void addNote(View view) {
        if (!noteAddActivity.isVisible()) {
            noteAddActivity = new NoteAddActivity();
            noteAddActivity.show(getSupportFragmentManager(), "ADD_NOTE");
        }

    }

}


