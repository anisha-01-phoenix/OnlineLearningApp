package com.example.newproject.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newproject.MainActivity;
import com.example.newproject.R;
import com.example.newproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onStart() {

        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        getSupportActionBar().hide();
        activityLoginBinding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgot_Password.class));
            }
        });
        activityLoginBinding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
         auth=FirebaseAuth.getInstance();
         activityLoginBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text=activityLoginBinding.emailLogin.getText().toString();
                String pass_text=activityLoginBinding.passwordLogin.getText().toString();
                if (email_text.isEmpty()) {
                    activityLoginBinding.emailLogin.setError("Email ID is Required");
                    activityLoginBinding.emailLogin.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
                    activityLoginBinding.emailLogin.setError("Email ID is invalid");
                    activityLoginBinding.emailLogin.requestFocus();
                    return;
                }
                if (pass_text.isEmpty()) {
                    activityLoginBinding.passwordLogin.setError("Password is Required");
                    activityLoginBinding.passwordLogin.requestFocus();
                    return;
                }

                activityLoginBinding.progressBar2.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                activityLoginBinding.progressBar2.setVisibility(View.GONE);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Authentication failed!",Toast.LENGTH_SHORT).show();
                                activityLoginBinding.progressBar2.setVisibility(View.GONE);
                            }
                        }
                    });

            }
        });
    }
}