package com.example.notebook.comparators;

import com.example.notebook.Note;

import java.util.Comparator;

public class DateComparator implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
      String array1[]= o1.getDate().split("/");
        String array2[]= o2.getDate().split("/");

            if(array1[1].compareTo(array2[1])==0){
                return array1[0].compareTo(array2[0]);
            }else{
                return array1[1].compareTo(array2[1]);
            }

    }
}
