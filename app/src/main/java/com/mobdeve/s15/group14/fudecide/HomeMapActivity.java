package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s15.group14.fudecide.databinding.ActivityHomeMapBinding;

import java.util.ArrayList;

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityHomeMapBinding binding;

    private ImageView list_view, profile;

    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    public static Location currentLocation;
    private float distance;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean firstRun;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private RestaurantsModel closestResto;
    private ArrayList<RestaurantsModel> restaurants = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<RestaurantsModel> troy = (ArrayList<RestaurantsModel>) getIntent().getSerializableExtra("key");
        this.restaurants = troy;
        Log.d("query-robert", restaurants.get(0).getRestoName());
        Log.d("query-robert", restaurants.get(1).getRestoName());
        Log.d("query-robert", restaurants.get(2).getRestoName());

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

        roulette_popup = new Dialog(this);

        roulette = (FloatingActionButton) findViewById(R.id.btn_roulette2);
        roulette.setOnClickListener(this);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            firstRun = true;
        }

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        distance = 0;
        closestResto = new RestaurantsModel();

        mMap.setMyLocationEnabled(true);
        currentLocation = mMap.getMyLocation();
//        double currLat = mMap.getMyLocation().getLatitude();
//        double currLong = mMap.getMyLocation().getLongitude();
//        Log.d("query-current", mMap.getMyLocation().getLatitude() + mMap.getMyLocation().getLongitude() + "");

        Log.d("query-frommapready", restaurants.size() + "");

        for (RestaurantsModel restaurant : restaurants) {
            LatLng point = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

            // Computes the distance of the restaurant to the user
            float[] results = new float[1];
            Location.distanceBetween(restaurant.getLatitude(), restaurant.getLongitude(), 14.4445, 120.9938, results);
            if (distance == 0 || results[0] < distance) {
                distance = results[0];
                closestResto = restaurant;
            }

            // Add a marker to the map
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(restaurant.getRestoName()));
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list_view:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.btn_profile2:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btn_roulette2:
                show_popup(v);
                Log.d("query-zz", "Latitude = " + mMap.getMyLocation().getLatitude() + "Longitude = " + mMap.getMyLocation().getLongitude());
                double userLat = getUserLat();
                double userLong = getUserLong();
                Log.d("query-zzz", "Lat = " + userLat + " Long = " + userLong);
                break;
        }
    }

    public double getUserLat(){
        double currLat = 0;

        currLat = mMap.getMyLocation().getLatitude();
        return currLat;
    }

    public double getUserLong(){
        double currLong = 0;

        currLong = mMap.getMyLocation().getLongitude();
        return currLong;
    }


}