package com.example.notebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.MViewHolder> {
    private List<Note> notes;
    private LayoutInflater inflater;
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
                    Intent intent=new Intent(context,NoteActivity.class);
                    intent.putExtra("action","update");
                    Note note=notes.get(getAdapterPosition());
                    intent.putExtra("theme",note.getTheme());
                    intent.putExtra("note",note.getNote());
                    intent.putExtra("id",getAdapterPosition());
                    ((AppCompatActivity)context).startActivityForResult(intent,Util.REQUEST_CODE_UPDATE);

                    return true;
                }
            });

        }
    }

    public NoteBookAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.context=context;
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
