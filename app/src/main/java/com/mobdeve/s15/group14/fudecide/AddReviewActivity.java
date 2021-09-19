package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddReview";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference dbRef;

    // temporary variables to store resto name and review details
    private String reviewID, userNameVar, restoNameVar, reviewVar;
    private Float ratingVar;
    private String datePosted;
    private Boolean liked = false;

    // dummy variables for generating reviewID
    private int dummyInt;
    private Random dummyRandom;

    private Button btn_post_review;
    private ImageView rev_home, rev_like;
    private RatingBar rev_rating;
    private EditText rev_review;

    private int googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    LoginActivity la = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        googleSignIn = la.getGoogleSignIn();

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // generate random ID for reviews
        dummyRandom = new Random(System.currentTimeMillis());
        dummyInt = 10000 + dummyRandom.nextInt(20000);
        reviewID = "RV" + String.valueOf(dummyInt);

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
                userNameVar = name;
            }
        }

        restoNameVar = getIncomingIntent();
        rev_rating = findViewById(R.id.rev_rating);
        rev_review = findViewById(R.id.rev_review);

        this.rev_home = findViewById(R.id.rev_home);
        rev_home.setOnClickListener(this);

        this.rev_like = findViewById(R.id.rev_like);
        rev_like.setOnClickListener(this);

        this.btn_post_review = findViewById(R.id.btn_post_review);
        btn_post_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postReview(restoNameVar, userNameVar, reviewID);
//                Log.d(TAG, "onClick: Post will be added for: " + restoNameVar + userNameVar);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rev_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.rev_like:
                likeRestaurant();
                break;
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
                        String name = document.getString("name");
                        userNameVar  = name;
                    } else {
                        Log.d("query-zz", "No such document");
                    }
                } else {
                    Log.d("query-zz", "failed");
                }
            }
        });
    }

    private void likeRestaurant() {

        if(liked == false){
            rev_like.setImageResource(R.drawable.liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayUnion(restoName));

            Toast.makeText(AddReviewActivity.this, "Added to Favorites", Toast.LENGTH_LONG).show();
            Log.d("query-zz", "Adding " + restoName + "into favorites of " + userID);
            liked = true;
        }
        else{
            rev_like.setImageResource(R.drawable.not_liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayRemove(restoName));

            Toast.makeText(AddReviewActivity.this, "Removed from Favorites", Toast.LENGTH_LONG).show();
            Log.d("query-zz", "Removing " + restoName + "from favorites of " + userID);
            liked = false;
        }
    }

    private String getIncomingIntent(){
        if(getIntent().hasExtra("rev_restoName")){
//            Log.d(TAG, "getIncomingIntent: found intent extras");

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
    private void postReview(String restoNameVar, String userNameVar, String reviewID) {
        // Get values from inputs
        reviewVar = rev_review.getText().toString();
        ratingVar = rev_rating.getRating();
        datePosted = getCurrentDate();

        //save to db (reviews collection)
        CollectionReference reviewsDB = db.collection("reviews");

        // put data fields into one object
        Map<String, Object> reviewObj = new HashMap<>();
        reviewObj.put("reviewID", reviewID);
        reviewObj.put("datePosted", datePosted);
        reviewObj.put("name", userNameVar);
        reviewObj.put("rating", ratingVar);
        reviewObj.put("restoName", restoNameVar);
        reviewObj.put("reviewText", reviewVar);

        // add object to reviews collection
        reviewsDB.add(reviewObj)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Review posted!");

                            finish();
//                            startActivity(new Intent(AddReviewActivity.this, RestaurantPageActivity.class));
                            Toast.makeText(AddReviewActivity.this, "Review posted", Toast.LENGTH_LONG).show();
                        }
                        else Log.d(TAG, "Posting of review failed.");
                    }
                });
    }

    // get current date and time
    public static String getCurrentDate(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
            String today = dateFormat.format(new Date());

            return today;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}