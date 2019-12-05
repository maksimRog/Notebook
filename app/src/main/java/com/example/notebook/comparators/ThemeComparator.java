package com.example.notebook.comparators;

import com.example.notebook.Note;

import java.util.Comparator;

public class ThemeComparator implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        return o1.getTheme().compareTo(o2.getTheme());
    }
}
