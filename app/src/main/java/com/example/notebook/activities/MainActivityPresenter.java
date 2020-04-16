package com.example.notebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.example.notebook.NoteApp;
import com.example.notebook.database.Note;
import com.example.notebook.database.NoteDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter {
    private MainActivityView view;
    private NoteDao noteDao = NoteApp.getInstance().getDatabase().noteDao();
    private Handler handler = new Handler(Looper.getMainLooper());

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }

    public void updateNotesFromDB() {
        final List<Note> notes = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                notes.addAll(noteDao.getAll());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.updateNotes(notes);
                    }
                });
            }
        }).start();

    }

    public void deleteAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.deleteAll();
                    }
                });
            }
        }).start();

    }

    public void updateNoteFromIntent(final Intent data) {
        final int id = data.getIntExtra("id_note", -1);
        final int position = data.getIntExtra("position", -1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Note note = noteDao.getById(id);
                note.theme = data.getStringExtra("theme");
                note.note = data.getStringExtra("note");
                note.setCreationDate();
                noteDao.update(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.updateNote(position, note);
                    }
                });
            }
        }).start();
    }

    public void deleteNote(final Note note, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.deleteNote(position);
                    }
                });
            }
        }).start();

    }
}
