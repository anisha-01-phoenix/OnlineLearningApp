package com.example.newproject.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newproject.MainActivity;
import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference myRef;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        EditText nameEdtTxt=findViewById(R.id.name);
        EditText usernameEdtTxt=findViewById(R.id.username);
        EditText mailEdtTxt=findViewById(R.id.email);
        EditText passwordEdtTxt=findViewById(R.id.password);
        EditText confirmEdtTxt=findViewById(R.id.confirmPassword);
        findViewById(R.id.gotologin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        auth=FirebaseAuth.getInstance();
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdtTxt.getText().toString();
                String userName = usernameEdtTxt.getText().toString();
                String email = mailEdtTxt.getText().toString();
                String confirmPassword = confirmEdtTxt.getText().toString();
                String password = passwordEdtTxt.getText().toString();
                if (name.isEmpty()) {
                    nameEdtTxt.setError("Name is Required");
                    nameEdtTxt.requestFocus();
                    return;
                }
                if (userName.isEmpty()) {
                    usernameEdtTxt.setError("User Name is Required");
                    usernameEdtTxt.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    mailEdtTxt.setError("Email ID is Required");
                    mailEdtTxt.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mailEdtTxt.setError("Email ID is invalid");
                    mailEdtTxt.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordEdtTxt.setError("Password is Required");
                    passwordEdtTxt.requestFocus();
                    return;
                }
                else if (password.length()<6)
                {
                    passwordEdtTxt.setError("Password should contain minimum 6 characters!");
                    passwordEdtTxt.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    confirmEdtTxt.setError("Confirm your Password!");
                    confirmEdtTxt.requestFocus();
                    return;
                }
                else if (password.length()<6)
                {
                    confirmEdtTxt.setError("Password should contain minimum 6 characters!");
                    confirmEdtTxt.requestFocus();
                    return;
                }
                else if(password.equals(confirmPassword)== false)
                {
                    confirmEdtTxt.setError("Does not match the Password!");
                    confirmEdtTxt.requestFocus();
                    return;
                }
                RegisterNow(email,password,userName);
                bar=findViewById(R.id.progressBar);
                bar.setVisibility(View.VISIBLE);
            }

        });


    }

    private void RegisterNow(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            myRef= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this,"Check your email and verify your email address!",Toast.LENGTH_SHORT).show();
                                        Intent i= new Intent(SignUpActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        bar.setVisibility(View.GONE);
                                        finish();
                                    }
                                }
                            });
                        }
                    });

                }else
                {
                    Toast.makeText(SignUpActivity.this,"Invalid!",Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.GONE);
                }
            }
        });


    }
}