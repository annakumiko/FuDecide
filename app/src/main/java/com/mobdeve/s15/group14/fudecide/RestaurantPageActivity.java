package com.mobdeve.s15.group14.fudecide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/*
    TASKS:
        [ ] Pass intent from HomeMainActivity (recycler view item to solo page)
        [ ] Print all data properly
        [ ] Fetch and print menu items
        [ ] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [ ] Check if store is open/closed
 */
public class RestaurantPageActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantPage";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String restoNameTv = getIntent().getStringExtra("restoNameTv");

            setDetails(restoNameTv);
        }
    }

    private void setDetails(String restoNameTv){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView restoName = findViewById(R.id.restoNameTv);
        restoName.setText(restoNameTv);
    }
}