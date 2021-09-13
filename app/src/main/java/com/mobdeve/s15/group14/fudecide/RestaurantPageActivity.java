package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TASKS:
        [/] Pass intent from HomeMainActivity (recycler view item to solo page)
        [/] Print all restaurant details progeperly
        [ ] Fetch and print menu items
        [ ] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [ ] Go back to HomeMain/HomeMap --> necessary?
        [ ] Add Review button
        [ ] See More reviews button
 */
public class RestaurantPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestaurantPage";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView menuList;
    private static ArrayList<MenuModel> menu = new ArrayList<>();
//    private static Object currResto;

    private ImageView iv_home, iv_liked;
    private Button btn_see_more, btn_add_review;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;
    private String userID;
    private String restoID;

    private Boolean liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        menuList = findViewById(R.id.rv_menu);

        this.iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(this);

        this.iv_liked = findViewById(R.id.iv_liked);
        iv_liked.setOnClickListener(this);

        this.btn_see_more = findViewById(R.id.btn_see_more);
        btn_see_more.setOnClickListener(this);

        this.btn_add_review = findViewById(R.id.btn_add_review);
        btn_add_review.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_review:
                startActivity(new Intent(this, AddReviewActivity.class));
                break;
            case R.id.btn_see_more:
                startActivity(new Intent(this, AllReviewsActivity.class));
                break;
            case R.id.iv_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.iv_liked:
                likeRestaurant();
                break;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        getIncomingIntent(); // get resto name from selected row
        findRestaurant(); // match resto name from db and collect details
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String restoNameTv = getIntent().getStringExtra("restoNameTv");
            setName(restoNameTv);
        }
    }

    private void setName(String restoNameTv){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView restoName = findViewById(R.id.restoNameTv);
        restoName.setText(restoNameTv);
    }

    private void findRestaurant() {
        String restoName = getIntent().getStringExtra("restoNameTv");
        // Get remaining restaurant data from db
        db.collection("restaurants").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String inHours = document.getString("openHours");
                        // compute distance from current location
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String rating = document.get("overallRating").toString();
                        String description = document.getString("restoDescription");
                        String name = document.getString("restoName");
                        String photo = document.getString("restoPhoto");

                        //collect menu
//                        List<menu> menu = [];


                        setDetails(rating, inHours, description);

//                        currResto = new RestaurantsModel(inHours, latitude, longitude, rating, description, name, photo);
                    }
                }
                else Log.d(TAG, "Restaurant not found");

                setAdapter();
            }
        });
    }

    private void setDetails(String rating, String inHours, String description){
        Log.d(TAG, "setDetails: setting restaurant details");

        TextView rate = findViewById(R.id.ratingTv);
        rate.setText(rating);

        TextView openHours = findViewById(R.id.openHoursTv);
        openHours.setText(inHours);

        // location

        TextView desc = findViewById(R.id.descTv);
        desc.setText(description);
    }

    // show menu

    // show reviews

    private void likeRestaurant() {

        if(liked == false){
            iv_liked.setImageResource(R.drawable.liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayUnion(restoName));

            Log.d("query-zz", "Adding " + restoName + "into favorites of " + userID);
            liked = true;
        }
        else {
            iv_liked.setImageResource(R.drawable.not_liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayRemove(restoName));

            Log.d("query-zz", "Removing " + restoName + "from favorites of " + userID);
            liked = false;
        }
    }

    // Set adapter
    private void setAdapter(){
        MenuAdapter adapter = new MenuAdapter(this, menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        menuList.setLayoutManager(layoutManager);
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setAdapter(adapter);
    }

    public static ArrayList<MenuModel> getMenu() { return menu; }

}