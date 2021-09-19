package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class EditReviewActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EditReview";

    private String reviewID, revText;
    private Float rate;

    private TextView update_restoName, update_ReviewText;
    private RatingBar update_rating;
    private ImageView reve_home;

    private Button btn_update_review;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Boolean liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        getIncomingIntent(); // get resto name from selected row

        update_restoName = findViewById(R.id.update_restoName);
        update_ReviewText = findViewById(R.id.update_reviewText);
        update_rating = findViewById(R.id.update_rating);

        btn_update_review = findViewById(R.id.btn_update_review);
        btn_update_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReview(reviewID);
            }
        });

        this.reve_home = findViewById(R.id.reve_home);
        reve_home.setOnClickListener(this);

    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restauName")){
            // fetch data from intent
            String revID = getIntent().getStringExtra("reviewID");
            String restName = getIntent().getStringExtra("restauName");
            String reviewTxt = getIntent().getStringExtra("reviewText");
            Double rate = getIntent().getDoubleExtra("rating", 0);

            Log.d(TAG, "getIncomingIntent: " + restName + reviewTxt + rate);

            // set data to view
            TextView updRestoName = findViewById(R.id.update_restoName);
            updRestoName.setText(restName);

            TextView updRevText = findViewById(R.id.update_reviewText);
            updRevText.setText(reviewTxt);

            RatingBar updRating = findViewById(R.id.update_rating);
            updRating.setRating(rate.floatValue());

            reviewID = revID;
        }
    }

    // update review
    private void updateReview(String reviewID){
        // get values from inputs
        revText = update_ReviewText.getText().toString();
        rate = update_rating.getRating();

        Log.d(TAG, "getIncomingIntent: " + revText + rate);

        db.collection("reviews").whereEqualTo("reviewID", reviewID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String docID = document.getId();

                        DocumentReference docRef = db.collection("reviews").document(docID);
                        docRef.update("reviewText", revText)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: Updated review text");
                                    }
                                });

                        docRef.update("rating", rate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: Updated review rating");
                                    }
                                });

                        finish();
                        Toast.makeText(EditReviewActivity.this, "Review updated", Toast.LENGTH_LONG).show();
                    }
                } else
                    Log.d(TAG, "No review found");

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rev_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
        }
    }

}