package com.example.mysocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class passwordforgot extends AppCompatActivity {

    FirebaseAuth auth;
    Button submit;
    TextInputEditText editTextForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordforgot);
        auth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.buttonSubmit);
        editTextForget = findViewById(R.id.editTextForget);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextForget.getText().toString();
                if(!email.equals("")) {
                    passwordReset(email);
                }
                else{
                    Toast.makeText(passwordforgot.this, "Please enter your email id.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void passwordReset(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(passwordforgot.this, "Please Check your email.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(passwordforgot.this, "There is some problem.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}