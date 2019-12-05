package com.example.notebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.MViewHolder> {
    private List<Note> notes;
    private LayoutInflater inflater;


    public class MViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_theme, tv_date, tv_note;

        public MViewHolder(final View view) {
            super(view);
            tv_theme = view.findViewById(R.id.output_theme);
            tv_date = view.findViewById(R.id.output_date);
            tv_note = view.findViewById(R.id.output_note);

        }
    }

    public NoteBookAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteBookAdapter.MViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = inflater.inflate(R.layout.item, viewGroup, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteBookAdapter.MViewHolder mViewHolder, int i) {
        Note note = notes.get(i);
        mViewHolder.tv_theme.setText(note.getTheme());
        mViewHolder.tv_date.setText(note.getDate());
        mViewHolder.tv_note.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
