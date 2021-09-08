package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView restaurantList;
    private ImageView map_view, profile;
    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static ArrayList<RestaurantsModel> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);


        map_view = (ImageView) findViewById(R.id.btn_map_view);
        map_view.setOnClickListener(this);

        profile = (ImageView) findViewById(R.id.btn_profile);
        profile.setOnClickListener(this);

        roulette_popup = new Dialog(this);

        roulette = (FloatingActionButton) findViewById(R.id.btn_roulette);
        roulette.setOnClickListener(this);

        restaurantList = findViewById(R.id.restaurant_list);

        setRestaurantData();


    }

    // Helper function to show the popup window for the roulette
    public void show_popup(View v) {
        ImageView close;
        CheckBox nearby, fav, high;
        Button spin;

        roulette_popup.setContentView(R.layout.roulette_popup);

        close = (ImageView) roulette_popup.findViewById(R.id.btn_close);
        nearby = (CheckBox) roulette_popup.findViewById(R.id.cb_nearby);
        fav = (CheckBox) roulette_popup.findViewById(R.id.cb_fav);
        high = (CheckBox) roulette_popup.findViewById(R.id.cb_high);
        spin = (Button) roulette_popup.findViewById(R.id.btn_spin);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roulette_popup.dismiss();
            }
        });

        roulette_popup.show();
    }

    // onClick functions
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map_view:
                Intent intent = new Intent(HomeMainActivity.this, HomeMapActivity.class);
                intent.putExtra("key", restaurants);
                startActivity(intent);
                break;
            case R.id.btn_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btn_roulette:
                show_popup(v);
                break;
        }
    }


    private void setRestaurantData() {
        // Get restaurants from Firestore db
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // If there are results
                if (task.isSuccessful()) {
                    // Add each restaurant to the restaurant ArrayList
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String inHours = document.getString("openHours");
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String rating = document.get("overallRating").toString();
                        String description = document.getString("restoDescription");
                        String name = document.getString("restoName");
                        String photo = document.getString("restoPhoto");

                        restaurants.add(new RestaurantsModel(inHours, latitude, longitude, rating, description, name, photo));
                    }
                } else
                    Log.d("query", "No Restaurants");
                setAdapter();
            }
        });
    }

    // Set adapter
    private void setAdapter(){
        RestaurantsAdapter adapter = new RestaurantsAdapter(restaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        restaurantList.setLayoutManager(layoutManager);
        restaurantList.setItemAnimator(new DefaultItemAnimator());
        restaurantList.setAdapter(adapter);
    }

    public static ArrayList<RestaurantsModel> getRestaurants() {
        return restaurants;
    }
}