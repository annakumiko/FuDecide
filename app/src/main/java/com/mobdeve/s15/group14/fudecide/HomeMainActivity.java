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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TAG = "HomeMainActivity";

    // Component declarations
    private RecyclerView restaurantList;
    private ImageView map_view, profile;

    // Database declarations
    private FirebaseAuth mAuth; // Firebase authentication
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        map_view = (ImageView) findViewById(R.id.btn_map_view);
        map_view.setOnClickListener(this);

        profile = (ImageView) findViewById(R.id.btn_profile);
        profile.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        // get current user

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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}