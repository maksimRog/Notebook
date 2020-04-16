package com.example.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.activities.MainActivity;
import com.example.notebook.activities.NoteActivity;
import com.example.notebook.comparators.DateComparator;
import com.example.notebook.comparators.ThemeComparator;
import com.example.notebook.database.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.MViewHolder> {
    private List<Note> notes;
    private List<Note> allNotes = new ArrayList<>();
    private Context context;

    public class MViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_theme, tv_date, tv_note;

        public MViewHolder(final View view) {
            super(view);
            tv_theme = view.findViewById(R.id.output_theme);
            tv_date = view.findViewById(R.id.output_date);
            tv_note = view.findViewById(R.id.output_note);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, NoteActivity.class);
                    intent.putExtra("action", "update");
                    Note note = notes.get(getAdapterPosition());
                    intent.putExtra("theme", note.theme);
                    intent.putExtra("note", note.note);
                    intent.putExtra("id_note", note.id);
                    intent.putExtra("position", getAdapterPosition());
                    ((MainActivity) context).startActivityForResult(intent, 100);

                    return true;
                }
            });

        }
    }


    public void updateNotes(List<Note> newNotes) {
        notes = newNotes;
        String query = context.getSharedPreferences(MainActivity.APP_PREF, MODE_PRIVATE)
                .getString("query", null);
        if (query != null && !query.equals("")) {
            allNotes.clear();
            allNotes.addAll(notes);
            notes = searchNotesWithKeyword(query);
        }
        notifyDataSetChanged();
    }

    public void updateNote(int position, Note note) {
        notes.set(position, note);
        notifyItemChanged(position);
    }

    public void sortByTheme() {
        Collections.sort(notes, new ThemeComparator());
        notifyDataSetChanged();
    }

    public void sortByDate() {
        Collections.sort(notes, new DateComparator());
        notifyDataSetChanged();
    }

    public void deleteAll() {
        notes.clear();
        notifyDataSetChanged();
    }

    public List<Note> searchNotesWithKeyword(String keyword) {
        if (keyword == null || keyword.equals("")) {
            return allNotes;
        }
        List<Note> buffer = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.note.toLowerCase().contains(keyword.toLowerCase())) {
                buffer.add(note);
            }
        }
        return buffer;
    }

    public void deleteNote(final int position) {
        notes.remove(position);
        notifyItemRemoved(position);

    }

    public Note getByPosition(int position) {
        return notes.get(position);
    }

    public NoteBookAdapter(Context context) {
        this.context = context;
    }


    @Override
    public NoteBookAdapter.MViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteBookAdapter.MViewHolder mViewHolder, int i) {
        Note note = notes.get(i);
        mViewHolder.tv_theme.setText(note.theme);
        mViewHolder.tv_date.setText(note.date);
        mViewHolder.tv_note.setText(note.note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
