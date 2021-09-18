package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_email, et_login_password;
    private Button btn_login, btn_login_google;
    private ProgressBar progressBar;

    private static int googleSignIn = 0;

    private FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_login_google = (Button) findViewById(R.id.sign_in_button);
        btn_login_google.setOnClickListener(this);

        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);

        progressBar = (ProgressBar) findViewById(R.id.login_loading);

        String default_web_client_id = "1076202257799-08ostnbaibhfo685tvd7ie91gt0sua2b.apps.googleusercontent.com";

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Firebase Sign In
            case R.id.btn_login:
                progressBar.setVisibility(View.VISIBLE);
                userLogin();
                break;
            // Google Sign In
            case R.id.sign_in_button:;
                signIn();
                break;
        }
    }

    // Firebase Sign In functions + input checking/validation for editText fields
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

        // Sign in with Email and Password function for Firebase.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, HomeMainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login. Please re-check your login credentials.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    // Sign in with Google function
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    // Firebaseauth + Google integration
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    googleSignIn = 1;
                    startActivity(new Intent(LoginActivity.this, HomeMainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    public int getGoogleSignIn(){
        return googleSignIn;
    }
}