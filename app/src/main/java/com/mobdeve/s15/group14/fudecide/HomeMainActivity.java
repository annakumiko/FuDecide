package com.mobdeve.s15.group14.fudecide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class HomeMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView restaurantList;
    private ImageView map_view, profile;
    private TextView tab_nearby, tab_favorites;
    private Dialog roulette_popup;
    private FloatingActionButton roulette;
    private SearchView search;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID;

    private ArrayList<RestaurantsModel> restaurants = new ArrayList<>();
    private ArrayList<RestaurantDist> rouletteArray = new ArrayList<>();
    private ArrayList<RestaurantDist> restaurantDistArray = new ArrayList<>();
    private ArrayList<RestaurantDist> favoriteRestaurants = new ArrayList<>();
    private ArrayList<RestaurantDist> data = new ArrayList<>(); // data being passed to RestaurantsAdapter

    private ArrayList<String> favorites;

    // recycler view
    private RestaurantsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // location
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;
    private boolean firstRun;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        progressBar = (ProgressBar) findViewById(R.id.main_loading);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        map_view = (ImageView) findViewById(R.id.btn_map_view);
        map_view.setOnClickListener(this);

        profile = (ImageView) findViewById(R.id.btn_profile);
        profile.setOnClickListener(this);

        tab_nearby = (TextView) findViewById(R.id.btn_nearby);
        tab_nearby.setOnClickListener(this);

        tab_favorites = (TextView) findViewById(R.id.btn_favorites);
        tab_favorites.setOnClickListener(this);

        roulette_popup = new Dialog(this);
        roulette = (FloatingActionButton) findViewById(R.id.btn_roulette);
        roulette.setOnClickListener(this);

        search = (SearchView) findViewById(R.id.sv_search);

        restaurantList = findViewById(R.id.restaurant_list);

        // location
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getCurrentLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            firstRun = true;
        }

        // search
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    // Turns on GPS on first run. (Called in getCurrentLocation)
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
                    Toast.makeText(HomeMainActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(HomeMainActivity.this, 2);
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

    // Checks if GPS is enabled. (Called in getCurrentLocation)
    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if(locationManager == null) {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    // Gets current location of the user's device
    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomeMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(HomeMainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(HomeMainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                        Log.d("query-map", "Lat = " + latitude);
                                        Log.d("query-map", "Long = " + longitude);

                                        setRestaurantData(); // get all restaurants
                                        getStringFavorites(); // get user favorites
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

    // Computes distance between current location and restaurants.
    public void computeDist(RestaurantsModel restaurant) {
        float[] results = new float[1];
        Log.d("LATLNG", "lat = " + latitude + ", long = " + longitude);
        Location.distanceBetween(restaurant.getLatitude(), restaurant.getLongitude(), latitude, longitude, results);

        RestaurantDist restroomDist = new RestaurantDist(results[0], restaurant);
        restaurantDistArray.add(restroomDist);
    }

    // Helper function to show the popup window for the roulette
    public void show_popup(View v) {
        ImageView close;
        Button spin;

        roulette_popup.setContentView(R.layout.roulette_popup);

        close = (ImageView) roulette_popup.findViewById(R.id.btn_close);
        spin = (Button) roulette_popup.findViewById(R.id.btn_spin);

        roulette_popup.show();

        // show result
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView restoName;
                ImageView restoPhoto, close;
                Button spin_again;

                RestaurantDist randomResto;
                String randomRestoName, randomRestoPhoto;
                Random rand = new Random();

                updateRoulette();
                randomResto = rouletteArray.get(rand.nextInt(rouletteArray.size()));
                randomRestoName = randomResto.getRestaurant().getRestoName();
                randomRestoPhoto = randomResto.getRestaurant().getRestoIcon();

                roulette_popup.setContentView(R.layout.roulette_result);

                restoName = roulette_popup.findViewById(R.id.tv_restoName);
                restoName.setText(randomRestoName);

                restoPhoto = roulette_popup.findViewById(R.id.iv_restoPhoto);
                Picasso.get().load(randomRestoPhoto).into(restoPhoto);

                spin_again = roulette_popup.findViewById(R.id.btn_spin_again);
                spin_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_popup(v);
                    }
                });

                close = roulette_popup.findViewById(R.id.close_result);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        roulette_popup.dismiss();
                    }
                });
            }
        });

        // close the popup
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roulette_popup.dismiss();
            }
        });
    }

    // fill roulette with restaurants
    private void updateRoulette() {
        CheckBox nearby, fav, high;
        ArrayList<RestaurantDist> nearbyResto = new ArrayList<>();
        ArrayList<RestaurantDist> highlyRated = new ArrayList<>();

        // checkboxes
        nearby = (CheckBox) roulette_popup.findViewById(R.id.cb_nearby);
        nearby.setOnClickListener(this);
        fav = (CheckBox) roulette_popup.findViewById(R.id.cb_fav);
        fav.setOnClickListener(this);
        high = (CheckBox) roulette_popup.findViewById(R.id.cb_high);
        high.setOnClickListener(this);

        if (nearby.isChecked()) {
            Log.d("updateRoulette ", "nearby - checked");
            for (RestaurantDist resto : restaurantDistArray) {
                if ((resto.getDistance()/1000) <= 500)
                    nearbyResto.add(resto);
            }
            rouletteArray.addAll(nearbyResto);
        }

        if (fav.isChecked()) {
            Log.d("updateRoulette ", "fav - checked");
            rouletteArray.addAll(favoriteRestaurants);
        }

        if (high.isChecked()) {
            Log.d("updateRoulette ", "high - checked");
            for (RestaurantDist resto : restaurantDistArray) {
                if (Integer.parseInt(resto.getRestaurant().getOverallRating()) >= 4)
                    highlyRated.add(resto);
            }
            rouletteArray.addAll(highlyRated);
        }
    }

    // Collects restaurant collection from Firestore DB
    private void setRestaurantData() {
        // Get restaurants from Firestore DB
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // If there are results
                if (task.isSuccessful()) {
                    restaurants.clear();
                    restaurantDistArray.clear();
                    // Add each restaurant to the restaurant ArrayList
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String inHours = document.getString("openHours");
                        double restoLat = document.getDouble("latitude");
                        double restoLong = document.getDouble("longitude");
                        String rating = document.get("overallRating").toString();
                        String description = document.getString("restoDescription");
                        String name = document.getString("restoName");
                        String photo = document.getString("restoPhoto");
                        String icon = document.getString("restoIcon");

                        // add each restaurant to restaurants array
                        restaurants.add(new RestaurantsModel(inHours, restoLat, restoLong, rating, description, name, photo, icon));
                        Log.d("query-not-sorted", "Restaurants: " + name);
                    }

                    // compute the distance for each restaurant
                    for (RestaurantsModel restaurant : restaurants) {
                        computeDist(restaurant);
                    }

                    // check distance
                    for (RestaurantDist r : restaurantDistArray)
                        Log.d("query-resto-dist",  "Results: name - " + r.getRestaurant().getRestoName() + "// dist - " + r.getDistance());

                    // sort the restaurants according to distance
                    Collections.sort(restaurantDistArray, new Comparator<RestaurantDist>() {
                        @Override
                        public int compare(RestaurantDist o1, RestaurantDist o2) {
                            return Float.compare(o1.getDistance(), o2.getDistance());
                        }
                    });

                    for (RestaurantDist r : restaurantDistArray)
                        Log.d("query-resto-sorted", "Results: name - " + r.getRestaurant().getRestoName());

                } else
                    Log.d("query", "No Restaurants");
                setAdapter();
                progressBar = (ProgressBar) findViewById(R.id.main_loading);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Gets the user's favorites and puts it into favoriteRestaurants Arraylist<RestaurantsModel>.
    private void getActualFavorites(ArrayList<String> favorites) {
        favoriteRestaurants.clear();
        for (int i = 0; i < favorites.size(); i++) {
            // Query favoritedRestaurants = db.collection("restaurants").whereEqualTo("restoName", favorites.get(i));
            Log.d("query-zzz", "Inside " + favorites.get(i));
            db.collection("restaurants").whereEqualTo("restoName", favorites.get(i)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("query-zzz", document.getId() + " => " + document.getData());

                            String inHours = document.getString("openHours");
                            double restoLat = document.getDouble("latitude");
                            double restoLong = document.getDouble("longitude");
                            String rating = document.get("overallRating").toString();
                            String description = document.getString("restoDescription");
                            String name = document.getString("restoName");
                            String photo = document.getString("restoPhoto");
                            String icon = document.getString("restoIcon");

                            RestaurantsModel resto = new RestaurantsModel(inHours, restoLat, restoLong, rating, description, name, photo, icon); // get resto

                            float[] results = new float[1];
                            Location.distanceBetween(resto.getLatitude(), resto.getLongitude(), latitude, longitude, results); // get distance

                            // add to arraylist
                            RestaurantDist restoDist = new RestaurantDist(results[0], resto);
                            favoriteRestaurants.add(restoDist);
                            Log.d("query-zzz", "Actual favorites size = " + favoriteRestaurants.size());
                        }
                    } else {
                        Log.d("query-zzz", "No favorites.", task.getException());
                    }
                }
            });
        }
    }

    // Gets names of favorite restaurants and puts it into ArrayList<String> favorites
    private void getStringFavorites() {
        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        favorites = (ArrayList<String>) document.get("favorites");
                        Log.d("query-zz", "favorites = " + favorites.toString());
                        getActualFavorites(favorites);
                    }
                } else {
                    Log.d("query-zz", "no document exists");
                }
            }
        });
    }

    // onClick functions from Home Main
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map_view:
                Intent intent = new Intent(HomeMainActivity.this, HomeMapActivity.class);
                intent.putExtra("RESTAURANTS_DIST_KEY", restaurantDistArray);
                intent.putExtra("FAVORITES_KEY", favoriteRestaurants);
                intent.putExtra("LATITUDE_KEY", latitude);
                intent.putExtra("LONGITUDE_KEY", longitude);
                startActivity(intent);
                break;
            case R.id.btn_profile:
                Intent intent2 = new Intent(HomeMainActivity.this, ProfileActivity.class);
                intent2.putExtra("FAVORITES_KEY", favoriteRestaurants);
                startActivity(intent2);
                break;
            case R.id.btn_roulette:
                show_popup(v);
                break;
            case R.id.btn_favorites:
                // change ui
                tab_favorites.setTextColor(Color.parseColor("#48D8BF"));
                tab_favorites.setTypeface(null, Typeface.BOLD);
                tab_nearby.setTextColor(Color.parseColor("#99333333"));
                tab_nearby.setTypeface(null, Typeface.NORMAL);

                // change list
                updateAdapterFavorites();
                break;
            case R.id.btn_nearby:
                // change ui
                tab_nearby.setTextColor(Color.parseColor("#48D8BF"));
                tab_nearby.setTypeface(null, Typeface.BOLD);
                tab_favorites.setTextColor(Color.parseColor("#99333333"));
                tab_favorites.setTypeface(null, Typeface.NORMAL);

                // change list
                updateAdapterNearby();
                break;
        }
    }

    // update data from nearby to favorites
    private void updateAdapterFavorites() {
        data.clear(); // clear old data
        data.addAll(favoriteRestaurants); // add new list
        adapter.notifyDataSetChanged(); // notify adapter
    }

    // update data from favorites to nearby
    private void updateAdapterNearby() {
        data.clear(); // clear old data
        data.addAll(restaurantDistArray); // add new list
        adapter.notifyDataSetChanged(); // notify adapter
    }

    // Set adapter upon initial loading
    private void setAdapter(){
        data.addAll(restaurantDistArray);
        adapter = new RestaurantsAdapter(this, data);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        restaurantList.setLayoutManager(layoutManager);
        restaurantList.setItemAnimator(new DefaultItemAnimator());
        restaurantList.setAdapter(adapter);
    }
}