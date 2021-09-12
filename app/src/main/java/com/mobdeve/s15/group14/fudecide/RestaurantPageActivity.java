package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
    TASKS:
        [/] Pass intent from HomeMainActivity (recycler view item to solo page)
        [/] Print all restaurant details progeperly
        [ ] Fetch and print menu items
        [/] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [/] Go back to HomeMain/HomeMap --> necessary?
        [ ] Add Review button
        [ ] See More reviews button
 */
public class RestaurantPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestaurantPage";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private RecyclerView menuList, reviewList;
    private ImageView iv_home, iv_liked;
    private Button btn_see_more, btn_add_review;

    private static ArrayList<MenuModel> menu = new ArrayList<>();
    private static ArrayList<ReviewModel> reviews = new ArrayList<>();

//    private static Object currResto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        menuList = findViewById(R.id.rv_menu);
        reviewList = findViewById(R.id.rv_reviews);

        this.iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(this);

        this.iv_liked = findViewById(R.id.iv_liked);
        iv_liked.setOnClickListener(this);

        this.btn_see_more = findViewById(R.id.btn_see_more);
        btn_see_more.setOnClickListener(this);

        this.btn_add_review = findViewById(R.id.btn_add_review);
        btn_add_review.setOnClickListener(this);

        String restoName = getIntent().getStringExtra("restoNameTv");

        getIncomingIntent(); // get resto name from selected row
        findRestaurant(restoName); // match resto name from db and collect details
        getRestoReviews(restoName); // get reviews of selected restaurant
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

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String restoNameTv = getIntent().getStringExtra("restoNameTv");
            setName(restoNameTv);
        }
    }

    private void setName(String restoNameTv){
        TextView restoName = findViewById(R.id.restoNameTv);
        restoName.setText(restoNameTv);
    }

    private void findRestaurant(String restoName) {
        // Get remaining restaurant data from db
        db.collection("restaurants").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    menu.clear();
                    reviews.clear();

                    for(QueryDocumentSnapshot document : task.getResult()){
                        String inHours = document.getString("openHours");
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String rating = document.get("overallRating").toString();
                        String description = document.getString("restoDescription");
                        String name = document.getString("restoName");
                        String photo = document.getString("restoPhoto");

                        //collect menu
//                        List<menu> menu = [];

                        setDetails(rating, inHours, description, photo);

//                        currResto = new RestaurantsModel(inHours, latitude, longitude, rating, description, name, photo);
                    }
                }
                else Log.d(TAG, "Restaurant not found");

                setMenuAdapter();
            }
        });
    }

    private void getRestoReviews(String restoName){
        // Collect resto reviews
        db.collection("reviews").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String uname = document.getString("name");
                        String rName = document.getString("restoName");
                        String reviewText = document.getString("reviewText");
                        String datePosted = document.getString("datePosted");
                        double rating = document.getDouble("rating");

                        reviews.add(new ReviewModel(uname, rName, reviewText, datePosted, rating));
//                        Log.d(TAG, "Review: " + uname + rName + reviewText + datePosted + rating);
                    }
                } else
                    Log.d(TAG, "No reviews");

                setReviewAdapter();
            }
        });

    }

    private void setDetails(String rating, String inHours, String description, String photo){
//        Log.d(TAG, "setDetails: setting restaurant details");

        TextView rate = findViewById(R.id.ratingTv);
        rate.setText(rating);

        TextView openHours = findViewById(R.id.openHoursTv);
        openHours.setText(inHours);

        // location

        TextView desc = findViewById(R.id.descTv);
        desc.setText(description);

        ImageView photoIv = findViewById(R.id.restoImg);
        File imgFile = new File(photo);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        photoIv.setImageBitmap(myBitmap);

//        photoIv.setImageURI(Uri.parse(photo));
//        photoIv.setImageBitmap(BitmapFactory.decodeFile(photo));

        Log.d(TAG, "setDetails: Photo Link path is " + photo );
    }

    // show menu

    // show reviews

    private void likeRestaurant() {
        Boolean liked = false; // replace with user data

        if(!liked){ // when empty heart is clicked
            iv_liked.setImageResource(R.drawable.liked);
            // save to current user document
        }
        else {
            iv_liked.setImageResource(R.drawable.not_liked);
            // save to current user document
        }
    }

    // Set adapters
    private void setMenuAdapter(){
        MenuAdapter adapter = new MenuAdapter(this, menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        menuList.setLayoutManager(layoutManager);
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setAdapter(adapter);
    }

    private void setReviewAdapter(){
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviewList.setLayoutManager(layoutManager);
        reviewList.setItemAnimator(new DefaultItemAnimator());
        reviewList.setAdapter(adapter);
    }

    public static ArrayList<MenuModel> getMenu() { return menu; }

    public static ArrayList<ReviewModel> getReviews() { return reviews; }
}