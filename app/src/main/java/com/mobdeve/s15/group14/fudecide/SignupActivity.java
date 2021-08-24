package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_name, et_signup_email, et_signup_password, et_signup_password2;
    private Button registerUser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btn_signup);
        registerUser.setOnClickListener(this);

        et_name = (EditText) findViewById(R.id.et_name);
        et_signup_email = (EditText) findViewById(R.id.et_signup_email);
        et_signup_password = (EditText) findViewById(R.id.et_signup_password);
        et_signup_password2 = (EditText) findViewById(R.id.et_signup_password2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = et_name.getText().toString().trim();
        String email = et_signup_email.getText().toString().trim();
        String password = et_signup_password.getText().toString().trim();
        String password2 = et_signup_password2.getText().toString().trim();

        if (name.isEmpty()) {
            et_name.setError("Please enter a name.");
            et_name.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            et_signup_email.setError("Please enter an e-mail address.");
            et_signup_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_signup_email.setError("Please provide a valid e-mail address.");
            et_signup_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_signup_password.setError("Please enter a password.");
            et_signup_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            et_signup_password.setError("Password should be at least 6 characters.");
            et_signup_password.requestFocus();
        }

        if (password2.isEmpty()) {
            et_signup_password2.setError("Please re-enter your password.");
            et_signup_password2.requestFocus();
            return;
        }
        if (et_signup_password.getText().toString().equals(et_signup_password2.getText().toString())) {

        } else {
            et_signup_password2.setError("Passwords don't match.");
            et_signup_password2.requestFocus();
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel user = new UserModel(name, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "User registered.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        Toast.makeText(SignupActivity.this, "Failed to register.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });


    }
}