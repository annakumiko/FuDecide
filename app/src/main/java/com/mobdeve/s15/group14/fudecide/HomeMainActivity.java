package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView restaurantList;

    private ImageView map_view, profile;

//    List<RestaurantsModel> data;
    private FirestoreRecyclerAdapter adapter;
//    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        map_view = (ImageView) findViewById(R.id.btn_map_view);
        map_view.setOnClickListener(this);

        profile = (ImageView) findViewById(R.id.btn_profile);
        profile.setOnClickListener(this);

//        Toast.makeText(HomeMainActivity.this, "Firebase loaded successfully", Toast.LENGTH_LONG).show();

        firebaseFirestore = firebaseFirestore.getInstance();
        restaurantList = findViewById(R.id.restaurantList);
//        restaurantList = setLayoutManager(new LinearLayoutManager(this));

//        firebaseDatabase = firebaseDatabase.getInstance();
//        dbRef = firebaseDatabase.getReference("restaurants");

//        Query query = firebaseFirestore.collection("restaurants");
//        FirestoreRecyclerOptions<RestaurantsModel> options = new FirestoreRecyclerOptions.Builder<RestaurantsModel>()
//                .setQuery(query, RestaurantsModel.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<RestaurantsModel, RestViewHolder>(options) {
//
//            @NonNull
//            @Override
//            public RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
//                return new RestViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull RestViewHolder holder, int position, @NonNull RestaurantsModel model) {
//                holder.resto_name.setText(model.getRestoName());
//                holder.resto_rating.setText(model.getOverallRating());
////                holder.resto_time.setText(model.getOpenHour()); // don't know how to do this again
//                holder.resto_loc.setText(model.getLatitude() + "," + model.getLongitude());
//            }
//        };
//
//        restaurantList.setHasFixedSize(true);
//        restaurantList.setLayoutManager(new LinearLayoutManager(this));
//        restaurantList.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map_view:
                startActivity(new Intent(this, HomeMapActivity.class));
                break;
            case R.id.btn_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = firebaseFirestore.collection("restaurants");

        FirestoreRecyclerOptions<RestaurantsModel> options = new FirestoreRecyclerOptions.Builder<RestaurantsModel>()
                .setQuery(query, RestaurantsModel.class)
                .build();

        FirestoreRecyclerAdapter<RestaurantsModel, RestViewHolder> adapter =
                new FirestoreRecyclerAdapter<RestaurantsModel, RestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RestViewHolder holder, int position, @NonNull RestaurantsModel model) {
                        holder.resto_name.setText(model.getRestoName());
                    }

                    @NonNull
                    @Override
                    public RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.restaurant_item, parent, false);

                        return new RestViewHolder(view);
                    }
                };


//        adapter = new FirestoreRecyclerAdapter<RestaurantsModel, RestViewHolder>(options) {
//
//            @NonNull
//            @Override
//            public RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.restaurant_item, parent, false);
//                return new RestViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull RestViewHolder holder, int position, @NonNull RestaurantsModel model) {
//                holder.setResto_name(model.getRestoName());
//                holder.resto_rating.setText(model.getOverallRating());
////                holder.resto_time.setText(model.getOpenHour()); // don't know how to do this again
//                holder.resto_loc.setText(model.getLatitude() + "," + model.getLongitude());
//            }
//        };

        adapter.startListening();
        restaurantList.setHasFixedSize(true);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
        restaurantList.setAdapter(adapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}