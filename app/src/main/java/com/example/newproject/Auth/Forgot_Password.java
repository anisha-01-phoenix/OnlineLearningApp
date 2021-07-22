package com.example.newproject.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        emailEditText=(EditText)findViewById(R.id.emailFP);
        Button resetPasswordButton=findViewById(R.id.resetPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword()
    {
        String email=emailEditText.getText().toString().trim();
        if(email.isEmpty())
        {
            emailEditText.setError("Email is Required!");
            emailEditText.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email ID is Invalid!");
            emailEditText.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(Forgot_Password.this, LoginActivity.class));
                    Toast.makeText(Forgot_Password.this, "Check your Email to reset your Password!",Toast.LENGTH_LONG ).show();
                    return;
                }
                else
                {
                    Toast.makeText(Forgot_Password.this, "Try Again!",Toast.LENGTH_LONG ).show();
                    return;
                }
            }
        });


    }
}