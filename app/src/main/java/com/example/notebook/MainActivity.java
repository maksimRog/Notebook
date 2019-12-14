package com.example.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.notebook.comparators.DateComparator;
import com.example.notebook.comparators.ThemeComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Note> notes = new ArrayList<>();
    private NoteBookAdapter adapter;
    private File file;
    private String saveWord;

    @Override
    protected void onStart() {
        super.onStart();
        updateNotes(Util.getNotes(file));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
            System.out.println("updating");
            int id = data.getIntExtra("id", 0);
            Note updateNote = new Note(data.getStringExtra("theme")
                    , Util.getDate()
                    , data.getStringExtra("note"));
            notes.set(id, updateNote);

            adapter.notifyItemChanged(id);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saveWord", saveWord);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            saveWord = savedInstanceState.getString("saveWord");
            if (saveWord != null) {
                searchNotesWithKeyword(saveWord);
            }

        }
    }

    public void updateNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        adapter.notifyDataSetChanged();
    }

    public void searchNotesWithKeyword(String keyword) {
        List<Note> buffer = new ArrayList<>();
        for (Note note : notes) {

            if (note.getNote().contains(keyword)) {
                buffer.add(note);
            }
        }
        updateNotes(buffer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.find_note);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        if (saveWord != null) {
            searchItem.expandActionView();
            searchView.setQuery(saveWord, false);
            searchNotesWithKeyword(saveWord);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    saveWord = null;
                    updateNotes(Util.getNotes(file));
                } else {
                    saveWord = newText;
                    searchNotesWithKeyword(newText);

                }
                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_all:
                updateNotes(Util.getNotes(file));

                return true;
            case R.id.add_note:
                Intent intent = new Intent(this, NoteActivity.class);
                intent.putExtra("action", "add");
                startActivity(intent);
                return true;
            case R.id.sort_by_theme:

                Collections.sort(notes, new ThemeComparator());
                adapter.notifyDataSetChanged();
                return true;
            case R.id.save_changes:

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    for (Note note : notes) {
                        String line = Util.createLine(note.getTheme(), note.getDate(), note.getNote());
                        fileWriter.write(line + "\n");

                    }
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.sort_by_date:
                Collections.sort(notes, new DateComparator());
                adapter.notifyDataSetChanged();
                return true;

            case R.id.delete_all:
                notes.clear();
                adapter.notifyDataSetChanged();
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file = new File(getFilesDir().getAbsolutePath() + "/data.txt");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NoteBookAdapter(this, notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                notes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }


}

