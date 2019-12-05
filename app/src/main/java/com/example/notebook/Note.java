package com.example.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Note {


    private String theme;
    private String date;
    private String note;

    public Note(String theme, String date, String note) {
        this.theme = theme;
        this.date = date;
        this.note = note;

    }

    @Override
    public String toString() {
        return "Note{" +
                "theme='" + theme + '\'' +
                ", date='" + date + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
