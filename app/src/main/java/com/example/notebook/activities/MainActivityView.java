package com.example.notebook.activities;

import com.example.notebook.database.Note;

import java.util.List;

public interface MainActivityView {
    void deleteAll();

    void deleteNote(int position);

    void updateNote(int position, Note note);

    void initViews();

    void updateNotes(List<Note> notes);
}
