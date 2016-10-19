package com.waqkz.cloudmessagingpushexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Waqas on 10/12/2016.
 */

public class UserAdapter extends ArrayAdapter<LogInForm> {

    public UserAdapter(Context context, ArrayList<LogInForm> logInForms) {
        super(context, 0, logInForms);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
        }

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView userNameText = (TextView) listItemView.findViewById(R.id.username_text_view);
        TextView emailText = (TextView) listItemView.findViewById(R.id.email_text_view);

        final LogInForm currentLogInForm = getItem(position);

        userNameText.setText(currentLogInForm.getUserName());
        emailText.setText(currentLogInForm.getEmail());

        return listItemView;
    }
}
