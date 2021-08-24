package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s15.group14.fudecide.databinding.ActivityHomeMapBinding;

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityHomeMapBinding binding;

    private ImageView list_view, profile;

    private Dialog roulette_popup;
    private FloatingActionButton roulette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney, Australia."));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                break;
        }
    }
}