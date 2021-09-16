package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
    TASKS:
        [/] Pass intent from HomeMainActivity (recycler view item to solo page)
        [/] Print all restaurant details progeperly
        [ ] Fetch and print menu items
        [/] Fetch and print reviews
        [ ] Compute distance from current location
        [ ] Compute overall rating
        [/] Go back to HomeMain/HomeMap --> necessary?
        [/] Add Review button
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

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;
    private String userID;
    private FirebaseUser user;
    private DatabaseReference dbRef;
    private String userNameVar;

    private Boolean liked;

    private int googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    LoginActivity la = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        googleSignIn = la.getGoogleSignIn();

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // OnCreate if Firebase Auth is used
        if (googleSignIn == 0){
            userID = mAuth.getCurrentUser().getUid();
            refreshData();

            // OnCreate if Google Sign In is used
        } else if (googleSignIn == 1){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            // Set tv_user to name
            if (acct != null) {
                String name = acct.getGivenName();
                userNameVar = name;
            }
        }

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

        btn_add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddReview(restoName);
            }
        });

        getIncomingIntent(); // get resto name from selected row
        findRestaurant(restoName); // match resto name from db and collect details
        getRestoReviews(restoName); // get reviews of selected restaurant
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    private void refreshData() {
        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        userNameVar = name;
                    } else {
                        Log.d("query-zz", "No such document");
                    }
                } else {
                    Log.d("query-zz", "failed");
                }
            }
        });
    }

    private void goToAddReview(String restoName){
        Intent intent = new Intent(this, AddReviewActivity.class);
        intent.putExtra("rev_restoName", restoName);
        startActivity(intent);
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");

        if(getIntent().hasExtra("restoNameTv")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            // fetch data from intent
            String restoNameTv = getIntent().getStringExtra("restoNameTv");

            // set text to view
            TextView restoName = findViewById(R.id.restoNameTv);
            restoName.setText(restoNameTv);
        }
    }

    private void findRestaurant(String restoName) {
        menu.clear();
        reviews.clear();
        // Get remaining restaurant data from db
        db.collection("restaurants").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
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
        reviews.clear();

        // Collect resto reviews
        db.collection("reviews").whereEqualTo("restoName", restoName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String reviewID = document.getString("reviewID");
                        String uname = document.getString("name");
                        String rName = document.getString("restoName");
                        String reviewText = document.getString("reviewText");
                        String datePosted = document.getString("datePosted");
                        double rating = document.getDouble("rating");

//                        // if current username matches name of review, do not hide button
//                        if(uname == userNameVar) {
//                            int hide = 1;
//                        }
//                        else {
//                            int hide = 0;
//                        }

                        reviews.add(new ReviewModel(reviewID, uname, rName, reviewText, datePosted, rating));
                    }
                } else
                    Log.d(TAG, "No reviews");

                setReviewAdapter();
            }
        });
    }

    private void setDetails(String rating, String inHours, String description, String photo){
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

        Log.d(TAG, "setDetails: Photo Link path is " + photo );
    }

    private void likeRestaurant() {

        if(liked == false){
            iv_liked.setImageResource(R.drawable.liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayUnion(restoName));

            Log.d("query-zz", "Adding " + restoName + "into favorites of " + userID);
            liked = true;
        }
        else{
            iv_liked.setImageResource(R.drawable.not_liked);

            String restoName = getIntent().getStringExtra("restoNameTv");

            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.update("favorites", FieldValue.arrayRemove(restoName));

            Log.d("query-zz", "Removing " + restoName + "from favorites of " + userID);
            liked = false;
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