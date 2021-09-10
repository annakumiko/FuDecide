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
import java.util.List;

/*
    TASKS:
        [/] Pass intent from HomeMainActivity (recycler view item to solo page)
        [/] Print all restaurant details properly
        [ ] Fetch and print menu items
        [ ] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [ ] Go back to HomeMain/HomeMap --> necessary?
        [ ] Add Review button
        [ ] See More reviews button
 */
public class RestaurantPageActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantPage";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView menuList;
    private static ArrayList<MenuModel> menu = new ArrayList<>();
//    private static Object currResto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        menuList = findViewById(R.id.rv_menu);

        getIncomingIntent(); // get resto name from selected row
        findRestaurant(); // match resto name from db and collect details
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String restoNameTv = getIntent().getStringExtra("restoNameTv");
            setName(restoNameTv);
        }
    }

    private void setName(String restoNameTv){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView restoName = findViewById(R.id.restoNameTv);
        restoName.setText(restoNameTv);
    }

    private void findRestaurant() {
        String restoName = getIntent().getStringExtra("restoNameTv");
        // Get remaining restaurant data from db
        db.collection("restaurants").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String inHours = document.getString("openHours");
                        // compute distance from current location
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String rating = document.get("overallRating").toString();
                        String description = document.getString("restoDescription");
                        String name = document.getString("restoName");
                        String photo = document.getString("restoPhoto");

                        //collect menu
//                        List<menu> menu = [];


                        setDetails(rating, inHours, description);

//                        currResto = new RestaurantsModel(inHours, latitude, longitude, rating, description, name, photo);
                    }
                }
                else Log.d(TAG, "Restaurant not found");

                setAdapter();
            }
        });
    }

    private void setDetails(String rating, String inHours, String description){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView rate = findViewById(R.id.ratingTv);
        rate.setText(rating);

        TextView openHours = findViewById(R.id.openHoursTv);
        openHours.setText(inHours);

        // location

        TextView desc = findViewById(R.id.descTv);
        desc.setText(description);
    }

    // Set adapter
    private void setAdapter(){
        MenuAdapter adapter = new MenuAdapter(this, menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        menuList.setLayoutManager(layoutManager);
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setAdapter(adapter);
    }
}