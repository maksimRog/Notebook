package com.example.notebook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        Button ok = findViewById(R.id.ok_button);
        final EditText theme = findViewById(R.id.theme);
        final EditText note = findViewById(R.id.note);

        String path = this.getFilesDir().getAbsolutePath();
        final File file = new File(path + "/data.txt");
        System.out.println(file);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    FileWriter fr = new FileWriter(file, true);
                    String line = "";
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                            Locale.getDefault());
                    line = Util.createLine(theme.getText().toString().trim(), formatter.format(new Date()),
                            note.getText().toString().replaceAll("\n", " ").trim());

                    fr.write(line + "\n");
                    fr.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                theme.getText().clear();
                note.getText().clear();
                Toast.makeText(getApplicationContext(), "Note added", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
