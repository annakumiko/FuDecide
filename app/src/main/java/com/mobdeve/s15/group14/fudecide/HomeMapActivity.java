package com.mobdeve.s15.group14.fudecide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s15.group14.fudecide.databinding.ActivityHomeMapBinding;

import java.util.ArrayList;

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityHomeMapBinding binding;
    private float distance;
    private double latitude, longitude;

    private ImageView list_view, profile;
    private TextView tab_nearby, tab_favorites;

    private ArrayList<RestaurantDist> restaurants = new ArrayList<>();
    private ArrayList<RestaurantDist> favorites = new ArrayList<>();
    private ArrayList<RestaurantDist> data = new ArrayList<>(); // data to be passed for markers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get restaurants
        ArrayList<RestaurantDist> restaurants_key = (ArrayList<RestaurantDist>) getIntent().getSerializableExtra("RESTAURANTS_DIST_KEY");
        this.restaurants = restaurants_key;
        this.data.addAll(restaurants);

        // get favorites
        ArrayList<RestaurantDist> favorites_key = (ArrayList<RestaurantDist>) getIntent().getSerializableExtra("FAVORITES_KEY");
        this.favorites = favorites_key;

        // get current location
        this.latitude = (Double) getIntent().getSerializableExtra("LATITUDE_KEY");
        this.longitude = (Double) getIntent().getSerializableExtra("LONGITUDE_KEY");

        Log.d("query-main", "Lat = " + latitude);
        Log.d("query-main", "Long = " + longitude);

        binding = ActivityHomeMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        list_view = (ImageView) findViewById(R.id.btn_list_view);
        list_view.setOnClickListener(this);

        profile = (ImageView) findViewById(R.id.btn_profile2);
        profile.setOnClickListener(this);

        tab_nearby = (TextView) findViewById(R.id.btn_nearby2);
        tab_nearby.setOnClickListener(this);

        tab_favorites = (TextView) findViewById(R.id.btn_favorites2);
        tab_favorites.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        distance = 0;

        mMap.setMyLocationEnabled(true);

        setMapData(googleMap); // puts markers on map

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markerTitle = marker.getTitle();
                Log.d("query-map", "Clicked on " + markerTitle);

                Intent intent = new Intent(HomeMapActivity.this, RestaurantPageActivity.class);
                intent.putExtra("restoNameTv", markerTitle);
                startActivity(intent);
                return false;
            }
        });
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {

            Log.d("MyMap", "setUpMapIfNeeded");
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView))
                    .getMapAsync(this);
        }
    }

    private void setMapData(@NonNull GoogleMap googleMap) {
        for (RestaurantDist restaurant : data) {
            LatLng point = new LatLng(restaurant.getRestaurant().getLatitude(), restaurant.getRestaurant().getLongitude());

            // Computes the distance of the restaurant to the user
            float[] results = new float[1];
            Location.distanceBetween(restaurant.getRestaurant().getLatitude(), restaurant.getRestaurant().getLongitude(), latitude, longitude, results);
            if (distance == 0 || results[0] < distance) {
                distance = results[0];
            }

            // Add a marker to the map
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(restaurant.getRestaurant().getRestoName()));
        }
    }

    // Updates map markers onClick nearby and favorite tabs.
    public void updateMarkers(ArrayList<RestaurantDist> r) {
        mMap.clear();
        data.clear();
        data.addAll(r);
        setUpMapIfNeeded();
        setMapData(mMap);
    }

    // onClick functions from Home Map
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list_view:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.btn_profile2:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btn_favorites2:
                // change ui
                tab_favorites.setTextColor(Color.parseColor("#48D8BF"));
                tab_nearby.setTextColor(Color.parseColor("#333333"));
                // change markers
                updateMarkers(favorites);
                break;
            case R.id.btn_nearby2:
                // change ui
                tab_nearby.setTextColor(Color.parseColor("#48D8BF"));
                tab_favorites.setTextColor(Color.parseColor("#333333"));
                // change markers
                updateMarkers(restaurants);
                break;
        }
    }
}