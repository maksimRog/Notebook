package com.example.notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.NoteApp;
import com.example.notebook.NoteBookAdapter;
import com.example.notebook.R;
import com.example.notebook.database.Note;


public class MainActivity extends AppCompatActivity {
    private NoteBookAdapter adapter;
    private String queryWord;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateNotesFromDB();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saveWord", queryWord);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            queryWord = savedInstanceState.getString("saveWord");
            if (queryWord != null) {
                adapter.searchNotesWithKeyword(queryWord);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.find_note);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        if (queryWord != null) {
            searchItem.expandActionView();
            searchView.setQuery(queryWord, false);
            adapter.searchNotesWithKeyword(queryWord);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    queryWord = null;
                    adapter.updateNotesFromDB();
                } else {
                    queryWord = newText;
                    adapter.searchNotesWithKeyword(newText);

                }
                return false;
            }
        });

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(this, NoteActivity.class);
                intent.putExtra("action", "add");
                startActivity(intent);
                return true;
            case R.id.sort_by_theme:
                adapter.sortByTheme();
                return true;

            case R.id.sort_by_date:
                adapter.sortByDate();
                return true;

            case R.id.delete_all:
                adapter.deleteAll();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            final int id = data.getIntExtra("id_note", -1);
            final int position = data.getIntExtra("position", -1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Note note = NoteApp.getInstance().getDatabase()
                            .noteDao().getById(id);
                    note.theme = data.getStringExtra("theme");
                    note.note = data.getStringExtra("note");
                    NoteApp.getInstance().getDatabase()
                            .noteDao().update(note);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateNote(position, note);
                        }
                    });
                }
            }).start();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new NoteBookAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                // NoteStorage.removeNote(MainActivity.this, notes.get(viewHolder.getAdapterPosition()));
                final int position = viewHolder.getAdapterPosition();
                adapter.deleteNote(position);

            }
        }).attachToRecyclerView(recyclerView);
    }


}

