package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_email, et_login_password;
    private Button btn_login, btn_login_google;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_login_google = (Button) findViewById(R.id.btn_login_google);
        btn_login_google.setOnClickListener(this);

        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = et_login_email.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();

        if (email.isEmpty()) {
            et_login_email.setError("Please enter an email.");
            et_login_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_login_email.setError("Please provide a valid e-mail address.");
            et_login_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_login_password.setError("Please enter a password.");
            et_login_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            et_login_password.setError("Password should be at least 6 characters.");
            et_login_password.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, HomeMapActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login. Please re-check your login credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}