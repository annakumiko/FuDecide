package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AddReviewActivity extends AppCompatActivity {

    private static final String TAG = "AddReview";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;
    private String userID;

    private String userNameVar, restoNameVar, reviewVar; // to store resto name and review
    private Float ratingVar; // to store rating value
    private String datePosted;

    private Button btn_post_review;
    private RatingBar rev_rating;
    private EditText rev_review;
    private TextView userName;

    private int googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    LoginActivity la = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        restoNameVar = getIncomingIntent();
        userName = findViewById(R.id.rev_userName);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        googleSignIn = la.getGoogleSignIn();

        rev_rating = findViewById(R.id.rev_rating);

        // OnCreate if Firebase Auth is used
        if (googleSignIn == 0){
            userID = mAuth.getCurrentUser().getUid();
            getUName();
            userNameVar = userName.getText().toString();
            Log.d(TAG, "User Name: " + userNameVar);

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
                userNameVar = name;
                Log.d(TAG, "User Name: " + userNameVar);
            }
        }

        this.btn_post_review = findViewById(R.id.btn_post_review);
        btn_post_review.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { postReview(restoNameVar, userNameVar); }
        });

    }

    private void getUName() {
        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String uname = document.getString("name");
                        userName.setText(uname);
                    } else {
                        Log.d("query-zz", "No such document");
                    }
                }
            }
        });
    }

    private String getIncomingIntent(){
        if(getIntent().hasExtra("rev_restoName")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            // fetch data from intent
            String restoNameTv = getIntent().getStringExtra("rev_restoName");

            // set text to view
            TextView restoName = findViewById(R.id.rev_restoName);
            restoName.setText(restoNameTv);

            return restoNameTv;
        }
        else return null;
    }

    // save review to db
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postReview(String restoNameVar, String userName) {
        // Get values from inputs
        reviewVar = rev_review.getText().toString();
        ratingVar = rev_rating.getRating();
        datePosted = getCurrentTimeStamp();

        Log.d(TAG, "postReview Values: " + restoNameVar + userName + reviewVar + ratingVar + datePosted);
    }

    // get current date and time
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY.MM.dd.HH:mm.ss");
            String currentTimeStamp = dateFormat.format(new Date());

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}