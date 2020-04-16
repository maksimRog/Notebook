package com.example.notebook.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notes")
public class Note  {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String theme;
    public String date;

    public String note;


    public void setCreationDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault());
        date=formatter.format(new Date());
    }
}
