package com.example.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Util {
    public static final int REQUEST_CODE_UPDATE=100;
    public static String createLine(String theme, String date, String note) {
        return theme + "__" + date + "__" + note;
    }
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault());
        return formatter.format(new Date());
    }
    public static List<Note> getNotes(File file) {
        List<Note> notes = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner != null) {
            while (scanner.hasNext()) {
                String dataArray[] = scanner.nextLine().split("__");

                if (dataArray.length == 3) {
                    notes.add(new Note(dataArray[0].toUpperCase(), dataArray[1], dataArray[2]));
                }


            }
        }

        return notes;
    }
}
