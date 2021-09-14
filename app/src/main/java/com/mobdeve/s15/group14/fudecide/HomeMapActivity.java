package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    private RestaurantsModel closestResto;
    private ArrayList<RestaurantsModel> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get restaurants
        ArrayList<RestaurantsModel> restaurants_key = (ArrayList<RestaurantsModel>) getIntent().getSerializableExtra("RESTAURANTS_KEY");
        this.restaurants = restaurants_key;

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

        roulette_popup = new Dialog(this);

        roulette = (FloatingActionButton) findViewById(R.id.btn_roulette2);
        roulette.setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        distance = 0;
        closestResto = new RestaurantsModel();
        // LatLng currPoint = new LatLng(latitude, longitude);

        Log.d("query-resto-size", restaurants.size() + "");
        mMap.setMyLocationEnabled(true);

        for (RestaurantsModel restaurant : restaurants) {
            LatLng point = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

            // Computes the distance of the restaurant to the user
            float[] results = new float[1];
            Location.distanceBetween(restaurant.getLatitude(), restaurant.getLongitude(), latitude, longitude, results);
            if (distance == 0 || results[0] < distance) {
                distance = results[0];
                closestResto = restaurant;
                Log.d("query-zz", closestResto.getRestoName() + "");
            }

            // Add a marker to the map
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(restaurant.getRestoName()));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
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
            case R.id.btn_roulette2:
                show_popup(v);
                break;
            case R.id.btn_favorites2:
                // change ui
                tab_favorites.setTextColor(Color.parseColor("#48D8BF"));
                tab_nearby.setTextColor(Color.parseColor("#333333"));

                break;
            case R.id.btn_nearby2:
                // change ui
                tab_nearby.setTextColor(Color.parseColor("#48D8BF"));
                tab_favorites.setTextColor(Color.parseColor("#333333"));

                break;
        }
    }


}