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

    private ImageView list_view, profile;

    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;
    private float distance;
    private boolean firstRun;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private RestaurantsModel closestResto;
    private ArrayList<RestaurantsModel> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<RestaurantsModel> troy = (ArrayList<RestaurantsModel>) getIntent().getSerializableExtra("key");
        this.restaurants = troy;

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getCurrentLocation();

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
        LatLng currPoint = new LatLng(latitude, longitude);

        Log.d("query-resto-size", restaurants.size() + "");

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

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(HomeMapActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(HomeMapActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if(locationManager == null) {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomeMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(HomeMapActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(HomeMapActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                        Log.d("query-zzz", "Lat = " + latitude);
                                        Log.d("query-zzz", "Long = " + longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
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

    // onclick functions
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
        }
    }


}