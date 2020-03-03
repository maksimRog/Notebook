package com.example.notebook.comparators;

import com.example.notebook.database.Note;

import java.util.Comparator;

public class ThemeComparator implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        return o1.theme.toLowerCase()
                .compareTo(o2.theme.toLowerCase());
    }
}
