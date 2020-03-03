package com.example.notebook;

import android.app.Application;

import androidx.room.Room;

import com.example.notebook.database.AppDatabase;

public class NoteApp extends Application {
    public static NoteApp instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
    }

    public static NoteApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
