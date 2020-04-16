package com.example.notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.NoteApp;
import com.example.notebook.database.Note;
import com.example.notebook.databinding.NoteActivityBinding;

public class NoteActivity extends AppCompatActivity {
    private NoteActivityBinding binding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoteActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Intent intent = getIntent();
        String action = intent.getStringExtra("action");

        switch (action) {
            case "update":
                this.setTitle("Update note");
                binding.theme.setText(intent.getStringExtra("theme"));
                binding.note.setText(intent.getStringExtra("note"));
                binding.okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(NoteActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        data.putExtra("id_note", intent.getIntExtra("id_note", -1))
                                .putExtra("position", intent.getIntExtra("position", -1))
                                .putExtra("theme",  binding.theme.getText().toString().trim())
                                .putExtra("note",    binding.note.getText().toString().trim());


                        setResult(RESULT_OK, data);
                        finish();

                    }
                });
                break;
            case "add":
                this.setTitle("Add note");
                binding.okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Note note=new Note();
                        note.setCreationDate();
                        note.note = binding.note.getText().toString().trim();
                        note.theme = binding.theme.getText().toString().trim();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NoteApp.getInstance().getDatabase().noteDao().insert(note);
                            }
                        }).start();
                        binding.theme.getText().clear();
                        binding.note.getText().clear();
                        Toast.makeText(NoteActivity.this, "Note added", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
        }


    }


}
