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

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.MViewHolder> {
    private List<Note> notes;
    private LayoutInflater inflater;
    private Context context;
    private Handler handler = new Handler(Looper.getMainLooper());

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

    public void updateNotesFromDB() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                notes = NoteApp.getInstance().getDatabase().noteDao().getAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void updateNotes(List<Note> newNotes) {
        notes = newNotes;
        System.out.println("adapter:" + notes + "\n" + newNotes);
        notifyDataSetChanged();
    }

    public void updateNote( int position,Note note) {
        notes.set(position,note);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteApp.getInstance().getDatabase().noteDao().deleteAll();
                notes.clear();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();

                    }
                });
            }
        }).start();

    }

    public void searchNotesWithKeyword(String keyword) {
        System.out.println("asfasfasfasf");
        List<Note> buffer = new ArrayList<>();
        for (Note note : notes) {

            if (note.note.toLowerCase().contains(keyword.toLowerCase())) {
                buffer.add(note);
            }
        }
        updateNotes(buffer);
    }

    public void deleteNote(final int position) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(notes.get(position).note);
                NoteApp.getInstance().getDatabase().noteDao().delete(notes.get(position));
                notes.remove(position);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRemoved(position);

                    }
                });
            }
        }).start();
    }

    public NoteBookAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public NoteBookAdapter.MViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = inflater.inflate(R.layout.item, viewGroup, false);
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
