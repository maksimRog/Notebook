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

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainActivityView {
    private NoteBookAdapter adapter;
    public static final String APP_PREF = "main pref";
    MainActivityPresenter presenter;

    @Override
    protected void onStart() {
        super.onStart();
        presenter.updateNotesFromDB();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.find_note);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        String queryWord = getSharedPreferences(APP_PREF, MODE_PRIVATE).getString("query", null);
        if (queryWord != null) {
            searchItem.expandActionView();
            searchView.setQuery(queryWord, false);
        }
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getSharedPreferences(APP_PREF, MODE_PRIVATE).edit().putString("query", null).apply();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.updateNotes(adapter.searchNotesWithKeyword(newText));
                getSharedPreferences(APP_PREF, MODE_PRIVATE).edit().putString("query", newText).apply();
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
                presenter.deleteAll();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                presenter.updateNoteFromIntent(data);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }


    @Override
    public void deleteAll() {
        adapter.deleteAll();
    }

    @Override
    public void deleteNote(int position) {
        adapter.deleteNote(position);
    }

    @Override
    public void updateNote(int position, Note note) {
        adapter.updateNote(position, note);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter=null;
        getSharedPreferences(APP_PREF, MODE_PRIVATE).edit().putString("query","").apply();

    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new NoteBookAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        presenter = new MainActivityPresenter(this);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                presenter.deleteNote(adapter.getByPosition(position), position);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void updateNotes(List<Note> notes) {
        adapter.updateNotes(notes);
    }
}

