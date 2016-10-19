package com.waqkz.cloudmessagingpushexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private EditText emailSignIn;
    private EditText passwordSignIn;
    private Button signInButton;
    private LinearLayout signUpText;

    private String mEmail;
    private String mPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressDialog = new ProgressDialog(SignInActivity.this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailSignIn = (EditText) findViewById(R.id.email_sign_in);
        passwordSignIn = (EditText) findViewById(R.id.password_sign_in);
        signInButton = (Button) findViewById(R.id.sign_in);
        signUpText = (LinearLayout) findViewById(R.id.sign_up);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInButtonMethod();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpTextMethod();
            }
        });
    }

    private void signInButtonMethod() {

        mProgressDialog.setMessage("Signing in ...");
        mProgressDialog.show();

        mEmail = emailSignIn.getText().toString();
        mPassword = passwordSignIn.getText().toString();


        if (TextUtils.isEmpty(mEmail)) {
            Toast.makeText(SignInActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(SignInActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (mPassword.length() < 6) {
                                mProgressDialog.dismiss();
                                passwordSignIn.setError(getString(R.string.password_error));
                            } else {
                                mProgressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(SignInActivity.this, PushActivity.class);
                            startActivity(intent);
                            mProgressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Signed in successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void signUpTextMethod() {

        Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }
}
