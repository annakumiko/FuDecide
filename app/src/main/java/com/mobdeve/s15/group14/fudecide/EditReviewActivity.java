package com.mobdeve.s15.group14.fudecide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class EditReviewActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EditReview";

    private Button btn_update_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        getIncomingIntent(); // get resto name from selected row

//        btn_update_review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateReview();
//            }
//        });
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restauName")){
            // fetch data from intent
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
        }
    }

    @Override
    public void onClick(View v) {

    }
}