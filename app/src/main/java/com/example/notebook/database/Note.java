package com.example.notebook.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "notes")
public class Note  {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String theme;
    public String date;

    public String note;



}
