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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

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
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgot_Password.class));
            }
        });
        findViewById(R.id.gotosignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        EditText emailETLogin=findViewById(R.id.emailLogin);
        EditText passETLogin=findViewById(R.id.passwordLogin);
        Button loginBtn=findViewById(R.id.login);
        ProgressBar progressBar=findViewById(R.id.progressBar2);

        auth=FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text=emailETLogin.getText().toString();
                String pass_text=passETLogin.getText().toString();
                if (email_text.isEmpty()) {
                    emailETLogin.setError("Email ID is Required");
                    emailETLogin.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
                    emailETLogin.setError("Email ID is invalid");
                    emailETLogin.requestFocus();
                    return;
                }
                if (pass_text.isEmpty()) {
                    passETLogin.setError("Password is Required");
                    passETLogin.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Authentication failed!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

            }
        });
    }
}