package com.example.notebook.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;


@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    List<Note> getAll();

    @Update
    void update(Note note);

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);
    @Query("DELETE FROM notes")
    void deleteAll();
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getById(int noteId);
}
