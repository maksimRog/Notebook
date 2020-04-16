package com.example.notebook;

import android.content.Context;

import com.example.notebook.activities.MainActivity;
import com.example.notebook.database.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteStorage {
    private static List<Note> notes = new ArrayList<>();
    public static final String APP_PREF = "app pref";

    public static List<Note> getNotes(Context context) {
        if (notes.isEmpty()) {
            Set<String> noteSet = getNotesFromPrefs(context);
            System.out.println(noteSet);
            Gson gson = new Gson();
            for (String note : noteSet) {
                notes.add(gson.fromJson(note, Note.class));

            }
        }
        return notes;
    }

    public static Set<String> getNotesFromPrefs(Context context) {
        return new HashSet<>(
                context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
                        .getStringSet("notes", new HashSet<String>()));
    }

    public static void clear(Context context) {
        context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
                .edit().putStringSet("notes", new HashSet<String>()).apply();
        notes.clear();
    }

    public static void addNote(Context context, Note note) {
        notes.add(note);
        saveNote(context, note);
    }

    public static void removeNote(Context context, Note note) {
        notes.remove(note);
        Gson gson = new Gson();
        Set<String> noteSet = getNotesFromPrefs(context);
        noteSet.remove(gson.toJson(note));
        context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
                .edit().putStringSet("notes", noteSet).apply();
    }

    public static void saveNotes(Context context, List<Note> notes) {
        Gson gson = new Gson();
        String notesJson = gson.toJson(notes);
        context.getSharedPreferences(MainActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit().putString("notes", notesJson).apply();
    }

    public static List<Note> getNotesFromPref(Context context) {
        Gson gson = new Gson();
        Type itemsListType = new TypeToken<List<Note>>() {
        }.getType();
        String notesJson = context.getSharedPreferences(MainActivity.APP_PREF, Context.MODE_PRIVATE)
                .getString("notes", "");
        return gson.fromJson(notesJson, itemsListType);
    }

    public static void saveNote(Context context, Note note) {
        Gson gson = new Gson();
        Set<String> noteSet = getNotesFromPrefs(context);
        noteSet.add(gson.toJson(note));

        context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
                .edit().putStringSet("notes", noteSet).apply();
        System.out.println(getNotesFromPrefs(context));
    }

}
