package com.asalazar.alex.sleepalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Salazar on 8/1/15.
 */
public class myDialogName extends DialogFragment {
    private SharedPreferences prefs;
    private EditText name;
    private TextView title;
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.dialog,null);
        name = (EditText)view.findViewById(R.id.editText);
        title = (TextView)view.findViewById(R.id.titleBar);
        title.setText("Enter your name: ");
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setHint("Type name here...");
        builder.setView(view);
        prefs = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        String savedName;
        try {
            savedName = prefs.getString("savedName", "");
        }
        catch (Exception ex){
            savedName = "";
        }
        if(!savedName.equals("")){
            name.setText(savedName);
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefs = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
                name = (EditText)view.findViewById(R.id.editText);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("savedName", name.getText().toString());
                editor.apply();
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}
