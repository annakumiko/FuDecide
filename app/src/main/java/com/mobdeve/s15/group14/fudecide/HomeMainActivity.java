package com.mobdeve.s15.group14.fudecide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView restaurantList;
    private ImageView map_view, profile;
    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Get DB instance
//    private ArrayList<RestaurantsModel> restaurants = new ArrayList<>(); // Restaurants

    // RecyclerView stuff
//    private ArrayList<MenuModel> menu;
//    private RecyclerView restaurant_list;
//    private RestaurantsAdapter resto_adapter;


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

//        // Restaurants RecyclerView stuff
//        this.restaurant_list = findViewById(R.id.restaurant_list);
//        this.resto_adapter = new RestaurantsAdapter(restaurants);
//        this.restaurant_list.setAdapter(resto_adapter);
//        this.restaurant_list.setLayoutManager(new LinearLayoutManager(this));

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
                startActivity(new Intent(this, HomeMapActivity.class));
                break;
            case R.id.btn_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btn_roulette:
                show_popup(v);
                break;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // get restaurants from firestore
//        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            // When the query is complete
//
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                restaurants.clear();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    restaurants.add(document.toObject(RestaurantsModel.class));
//                }
//            }
//        });
//
//    }
}