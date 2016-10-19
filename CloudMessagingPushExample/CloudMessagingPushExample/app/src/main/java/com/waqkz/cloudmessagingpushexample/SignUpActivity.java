package com.waqkz.cloudmessagingpushexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private HashMap<String, String> hashMap = new HashMap<>();

    private EditText userName;
    private EditText email;
    private EditText password;

    String mUsername;
    String mEmail;
    String mPassword;

    private Button signUpButton;

    private FirebaseUser user;

    private DatabaseReference mDatabase;
    private LogInForm logInForm;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mProgressDialog = new ProgressDialog(SignUpActivity.this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        userName = (EditText) findViewById(R.id.username_edit_text);
        email = (EditText) findViewById(R.id.email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);

        signUpButton = (Button) findViewById(R.id.sign_up_button);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpButtonMethod();
            }
        });


    }

    private void signUpButtonMethod() {

        mProgressDialog.setMessage("Signing up ...");
        mProgressDialog.show();

        mUsername = userName.getText().toString();
        mEmail = email.getText().toString();
        mPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail)) {

            Toast.makeText(SignUpActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(SignUpActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        logInForm = new LogInForm(user.getUid().toString(), mUsername, mEmail, mPassword);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            mDatabase.child("User").child(user.getUid()).setValue(logInForm);

                            /*FirebaseDatabase.getInstance().getReference().child("userUUID").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    ArrayList<String> userUUID = new ArrayList<>((ArrayList) dataSnapshot.getValue());

                                    userUUID.add(user.getUid());

                                    mDatabase.child("userUUID").setValue(userUUID);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });*/

                           /* ArrayList<String> userUUID = new ArrayList<String>();
                            userUUID.add(user.getUid());

                            mDatabase.child("userUUID").setValue(userUUID);*/

                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            mProgressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Signed up successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference().child("userUUID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> userUUID = new ArrayList<>((ArrayList) dataSnapshot.getValue());

                userUUID.add(user.getUid());

                mDatabase.child("userUUID").setValue(userUUID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
