package com.waqkz.cloudmessagingpushexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Waqas on 10/13/2016.
 */

public class DialogUser extends DialogFragment {

    LayoutInflater inflater;
    View view;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("SEND MESSAGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle bundle = getArguments();
                String UUID = bundle.getString("loginform");

                Log.v("TAGS", "" + UUID);

                EditText toDoText = (EditText) view.findViewById(R.id.element_edit_text);

                HashMap<String, String> result = new HashMap<>();
                result.put("uid", UUID);
                result.put("pushId", mDatabase.child("Notifications").child(UUID).push().getKey());
                result.put("message", toDoText.getText().toString());

                mDatabase.child("Notifications").child(UUID).child(result.get("pushId")).setValue(result);

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
