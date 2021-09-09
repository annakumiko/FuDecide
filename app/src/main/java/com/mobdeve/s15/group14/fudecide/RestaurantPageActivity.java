package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
    TASKS:
        [/] Pass intent from HomeMainActivity (recycler view item to solo page)
        [ ] Print all data properly
        [ ] Fetch and print menu items
        [ ] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [ ] Check if store is open/closed
 */
public class RestaurantPageActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantPage";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView menuList;
    private static ArrayList<MenuModel> menu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        menuList = findViewById(R.id.rv_menu);

        getIncomingIntent();
//        collectMenu();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String restoNameTv = getIntent().getStringExtra("restoNameTv");
            String ratingTv = getIntent().getStringExtra("ratingTv");
            String openHoursTv = getIntent().getStringExtra("openHoursTv");
            String descTv = getIntent().getStringExtra("descTv");
            // location
            // photo

            setDetails(restoNameTv, ratingTv, openHoursTv, descTv);
        }
    }

    private void setDetails(String restoNameTv, String ratingTv, String openHoursTv, String descTv){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView restoName = findViewById(R.id.restoNameTv);
        restoName.setText(restoNameTv);

        TextView rating = findViewById(R.id.ratingTv);
        rating.setText(ratingTv);

        TextView openHours = findViewById(R.id.openHoursTv);
        openHours.setText(openHoursTv);

        TextView desc = findViewById(R.id.descTv);
        desc.setText(descTv);

        // location
        // photo
    }

    private void collectMenu() {
        // Get remaining restaurant data from db
        String restoName = getIntent().getStringExtra("restoNameTv");
        db.collection("restaurants").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){

                        menu.add(new MenuModel());
                    }
                }
                else Log.d(TAG, "No menu items");

                setAdapter();
            }
        });
    }

    // Set adapter
    private void setAdapter(){
//        MenuAdapter adapter = new MenuAdapter(this, menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        menuList.setLayoutManager(layoutManager);
        menuList.setItemAnimator(new DefaultItemAnimator());
//        menuList.setAdapter(adapter);
    }
}