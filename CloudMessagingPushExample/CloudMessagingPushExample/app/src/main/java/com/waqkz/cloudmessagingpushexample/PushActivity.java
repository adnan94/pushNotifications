package com.waqkz.cloudmessagingpushexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PushActivity extends AppCompatActivity {

    private ListView listview;
    private HashMap<String, String> hashMap;
    private LogInForm logInForm;
    private DatabaseReference mDatabase;
    private ValueEventListener mUserListListener;
    private ArrayList<LogInForm> userList;
    private FirebaseUser currentUser;
    private UserAdapter userAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        Intent intent = new Intent(PushActivity.this, PushService.class);
        startService(intent);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Log.v("USER", "" + currentUser.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        listview = (ListView) findViewById(R.id.list);

        userList = new ArrayList<LogInForm>();

        FirebaseDatabase.getInstance().getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d("value", "" + dataSnapshot.getValue());

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    logInForm = data.getValue(LogInForm.class);

                   /* Log.d("value", "" + logInForm.getUUID());
                    Log.d("value", "" + logInForm.getEmail());*/

                    userList.add(new LogInForm(logInForm.getUUID(), logInForm.getUserName(), logInForm.getEmail(), logInForm.getPassword()));

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userAdapter = new UserAdapter(PushActivity.this, userList);
        listview.setAdapter(userAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Log.v("TAG", "" + position);

                LogInForm logInForm = userList.get(position);

                //Log.v("TAG", "" + logInForm.getUUID());
                if (logInForm.getUUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Toast.makeText(PushActivity.this, "Its you", Toast.LENGTH_SHORT).show();
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString("loginform", logInForm.getUUID());

                    DialogUser dialogUser = new DialogUser();

                    dialogUser.setArguments(bundle);
                    dialogUser.show(getFragmentManager(), "dialog_list");

                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(PushActivity.this, PushService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(PushActivity.this, PushService.class);
        startService(intent);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(PushActivity.this, PushService.class);
        startService(intent);
    }*/
}
