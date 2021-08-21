package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.PopupWindow;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView restaurantList;

    private FirestoreRecyclerAdapter adapter;

    private ImageView map_view, profile;

    private Dialog roulette_popup;
    private FloatingActionButton roulette;

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
//        Toast.makeText(MainActivity.this, "Firebase loaded successfully", Toast.LENGTH_LONG).show();
//
//        firebaseFirestore = firebaseFirestore.getInstance();
//        restaurantList = findViewById(R.id.restaurant_list);
//
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
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurantview, parent, false);
//                return new RestViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull RestViewHolder holder, int position, @NonNull RestaurantsModel model) {
//                holder.restName.setText(model.getRestName());
//                holder.desc.setText(model.getDesc());
//            }
//        };
//
//        restaurantList.setHasFixedSize(true);
//        restaurantList.setLayoutManager(new LinearLayoutManager(this));
//        restaurantList.setAdapter(adapter);
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
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}