package com.mobdeve.s15.group14.fudecide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView home, logout;
    private TextView userNameTextView, userBio, edit_bio;
    private Dialog bio_popup;
    private Button save;

    private FirebaseUser user;
    private DatabaseReference dbRef;

    private String userID;

    private int googleSignIn;

    GoogleSignInClient mGoogleSignInClient;

    LoginActivity la = new LoginActivity();

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        googleSignIn = la.getGoogleSignIn();
        Toast.makeText(ProfileActivity.this, "googleSignIn = " + googleSignIn, Toast.LENGTH_LONG).show();

        home = (ImageView) findViewById(R.id.btn_home);
        home.setOnClickListener(this);

        logout = (ImageView) findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);

        edit_bio = (TextView) findViewById(R.id.tv_editbio);
        edit_bio.setOnClickListener(this);

        bio_popup = new Dialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        userNameTextView = (TextView) findViewById(R.id.tv_user);
        userBio = (TextView) findViewById(R.id.tv_bio);
        save = (Button) bio_popup.findViewById(R.id.btn_save);


        // OnCreate if Firebase Auth is used
        if (googleSignIn == 0){
            userID = mAuth.getCurrentUser().getUid();
            refreshData();

            // OnCreate if Google Sign In is used
        } else if (googleSignIn == 1){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            // Set tv_user to name
            if (acct != null) {
                String name = acct.getGivenName();
                userNameTextView.setText("Hello, " + name);
            }
        }
    }

    private void refreshData() {
        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userNameTextView.setText("Hello, " + document.getString("name"));
                        userBio.setText(document.getString("bio"));
                    } else {
                        Log.d("query-zz", "No such document");
                    }
                } else {
                    Log.d("query-zz", "get failed with ", task.getException());
                }
            }
        });
    }

    // Helper function to show the popup window for the bio
    public void show_popup(View v) {
        ImageView close;
        Button save;
        EditText et_bio;

        bio_popup.setContentView(R.layout.bio_popup);

        et_bio = bio_popup.findViewById(R.id.et_bio);
        close = (ImageView) bio_popup.findViewById(R.id.btn_close2);
        save = (Button) bio_popup.findViewById(R.id.btn_save);

        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                et_bio.setText(documentSnapshot.getString("bio"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBio();
                refreshData();
                bio_popup.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bio_popup.dismiss();
            }
        });

        bio_popup.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.btn_logout:
                Toast.makeText(ProfileActivity.this, "Signing out", Toast.LENGTH_LONG).show();
                signOut();
            case R.id.tv_editbio:
                show_popup(v);
                break;
        }
    }

    private void updateBio() {
        EditText et_bio = bio_popup.findViewById(R.id.et_bio);
        String newBio = et_bio.getText().toString().trim();

        if (newBio.isEmpty()) {
            et_bio.setError("Please enter a new bio.");
            et_bio.requestFocus();
            return;
        }

        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.update("bio", newBio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("query-zz", "Successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("query-zz", "Error updating document", e);
                    }
                });
    }

    // signOut function
    private void signOut() {
        //Intent intent = new Intent(ProfileActivity.this, LandingActivity.class);

        // signOut for Firebase
        if (googleSignIn == 0){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
            finish();

            // signOut for Google
        } else if (googleSignIn == 1){
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
                            finish();
                        }
                    });
        }
    }
}