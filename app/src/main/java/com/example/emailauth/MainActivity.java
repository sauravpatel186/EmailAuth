package com.example.emailauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText uname, password;
    Button login;
    TextView signup, fpass;
    private FirebaseAuth mAuth;
    String user, pass, url;
    ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        login = (Button) findViewById(R.id.login_btn);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Home.class);
                    finish();
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (uname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    uname.setError("Email Cannot Be Empty");
                } else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    password.setError("Password Cannot Be Empty");
                } else {
                    user = uname.getText().toString();
                    pass = password.getText().toString();
                    progressDialog.setTitle("Validating Details...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();

                            } else {


                                startActivity(new Intent(MainActivity.this, Home.class));
                                progressDialog.dismiss();
                                finish();

                            }
                        }
                    });
//                    background bg=new background();
//                    bg.execute(user,pass);

                    //    new InsertData().execute();

                }

            }
        });

        signup = (TextView) findViewById(R.id.txtsignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Registration.class);
                startActivity(i);
            }
        });



        }
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthStateListener);
    }


}