package com.example.notebook;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle data =getArguments();
      final String theme= data.getString("theme");
        String note= data.getString("note");
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.update_dialog);
        final EditText themeEd=dialog.findViewById(R.id.dialog_theme);
        final EditText noteEd=dialog.findViewById(R.id.dialog_note);
        Button dialogOk=dialog.findViewById(R.id.dialog_ok);
        themeEd.setText(theme);
        noteEd.setText(note);

        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getContext()
                        .getSharedPreferences("DialogRes", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("theme",themeEd.getText().toString())
                        .putString("note",noteEd.getText().toString())
                        .apply();
                dismiss();
            }
        });

        return dialog;

    }
}
