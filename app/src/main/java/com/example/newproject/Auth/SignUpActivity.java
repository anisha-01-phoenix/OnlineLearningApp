package com.example.newproject.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newproject.MainActivity;
import com.example.newproject.R;
import com.example.newproject.databinding.ActivitySignUpBinding;
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
    ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        getSupportActionBar().hide();
        activitySignUpBinding.gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        auth = FirebaseAuth.getInstance();
        activitySignUpBinding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = activitySignUpBinding.name.getText().toString();
                String userName = activitySignUpBinding.username.getText().toString();
                String email = activitySignUpBinding.email.getText().toString();
                String confirmPassword = activitySignUpBinding.confirmPassword.getText().toString();
                String password = activitySignUpBinding.password.getText().toString();
                if (name.isEmpty()) {
                    activitySignUpBinding.name.setError("Name is Required");
                    activitySignUpBinding.name.requestFocus();
                    return;
                }
                if (userName.isEmpty()) {
                    activitySignUpBinding.username.setError("User Name is Required");
                    activitySignUpBinding.username.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    activitySignUpBinding.email.setError("Email ID is Required");
                    activitySignUpBinding.email.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    activitySignUpBinding.email.setError("Email ID is invalid");
                    activitySignUpBinding.email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    activitySignUpBinding.password.setError("Password is Required");
                    activitySignUpBinding.password.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    activitySignUpBinding.password.setError("Password should contain minimum 6 characters!");
                    activitySignUpBinding.password.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    activitySignUpBinding.confirmPassword.setError("Confirm your Password!");
                    activitySignUpBinding.confirmPassword.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    activitySignUpBinding.confirmPassword.setError("Password should contain minimum 6 characters!");
                    activitySignUpBinding.confirmPassword.requestFocus();
                    return;
                } else if (password.equals(confirmPassword) == false) {
                    activitySignUpBinding.confirmPassword.setError("Does not match the Password!");
                    activitySignUpBinding.confirmPassword.requestFocus();
                    return;
                }
                RegisterNow(email, password, userName, name);
                activitySignUpBinding.progressBar.setVisibility(View.VISIBLE);
            }

        });


    }

    private void RegisterNow(String email, String password, String username, String name) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            myRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("name", name);
                            hashMap.put("username", username);
                            hashMap.put("imageurl", "https://tse2.mm.bing.net/th?id=OIP.NYi0ibx-GnoFsgKhwj8WfgHaLZ&pid=Api&P=0&w=300&h=300");

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Check your email and verify your email address!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        activitySignUpBinding.progressBar.setVisibility(View.GONE);
                                        finish();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid!", Toast.LENGTH_SHORT).show();
                    activitySignUpBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}