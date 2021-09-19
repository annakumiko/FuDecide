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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_name, et_signup_email, et_signup_password, et_signup_password2;
    private Button registerUser, googleSignUp;
    private ProgressBar progressBar;

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        registerUser = (Button) findViewById(R.id.btn_signup);
        registerUser.setOnClickListener(this);

        googleSignUp = (Button) findViewById(R.id.google_signup);
        googleSignUp.setOnClickListener(this);

        et_name = (EditText) findViewById(R.id.et_name);
        et_signup_email = (EditText) findViewById(R.id.et_signup_email);
        et_signup_password = (EditText) findViewById(R.id.et_signup_password);
        et_signup_password2 = (EditText) findViewById(R.id.et_signup_password2);

        progressBar = (ProgressBar) findViewById(R.id.signup_loading);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                registerUser();
                break;
            case R.id.google_signup:
                googleRegister();
        }
    }

    private void googleRegister() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Main register function.
    private void registerUser() {
        String name = et_name.getText().toString().trim();
        String email = et_signup_email.getText().toString().trim();
        String password = et_signup_password.getText().toString().trim();
        String password2 = et_signup_password2.getText().toString().trim();

        // Basic input checking.
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

        // Creates entry in Firestore DB.
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.VISIBLE);
                            //UserModel user = new UserModel(name, email);

                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fs.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("bio", "Add your bio here!");
                            user.put("favorites", Arrays.asList("x"));
                            user.put("favorites", FieldValue.arrayRemove("x"));
                            documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignupActivity.this, "User registered.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignupActivity.this, "Failed to register.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
        });
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

    // Integrates Google Account with custom email in FirebaseAuth
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    progressBar.setVisibility(View.VISIBLE);
                    String fName = acct.getGivenName();
                    String lName = acct.getFamilyName();
                    String name = fName.concat(" ").concat(lName);
                    String email = acct.getEmail();
                    //UserModel user = new UserModel(name, email);

                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fs.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("bio", "Add your bio here!");
                    user.put("favorites", Arrays.asList("x"));
                    user.put("favorites", FieldValue.arrayRemove("x"));
                    documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignupActivity.this, "User registered", Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignupActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                })
                .addOnFailureListener(this, e -> Toast.makeText(SignupActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}