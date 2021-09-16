package com.mobdeve.s15.group14.fudecide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView home, logout;
    private TextView userNameTextView, userBio, edit_bio;
    private Dialog bio_popup;
    private Button save;
    private RecyclerView favoritesList, reviewsList;

    private FirebaseUser user;
    private DatabaseReference dbRef;

    private String userID;

    private int googleSignIn;

    GoogleSignInClient mGoogleSignInClient;

    LoginActivity la = new LoginActivity();

    private FirebaseAuth mAuth;
    private FirebaseFirestore fs;

    private ArrayList<RestaurantsModel> favoriteRestaurants = new ArrayList<>();
    private ArrayList<ReviewModel> reviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        googleSignIn = la.getGoogleSignIn();
        Toast.makeText(ProfileActivity.this, "googleSignIn = " + googleSignIn, Toast.LENGTH_LONG).show();

        home = (ImageView) findViewById(R.id.btn_home);
        home.setOnClickListener(this);

        logout = (ImageView) findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);

        edit_bio = (TextView) findViewById(R.id.tv_editbio);
        edit_bio.setOnClickListener(this);

        bio_popup = new Dialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        userNameTextView = (TextView) findViewById(R.id.tv_user);
        userBio = (TextView) findViewById(R.id.tv_bio);
        save = (Button) bio_popup.findViewById(R.id.btn_save);

        reviewsList = findViewById(R.id.rv_user_reviews);

        ArrayList<RestaurantsModel> FAVORITES_KEY = (ArrayList<RestaurantsModel>) getIntent().getSerializableExtra("FAVORITES_KEY");
        this.favoriteRestaurants = FAVORITES_KEY;
        favoritesList = (RecyclerView) findViewById(R.id.rv_favorites);
        Log.d("query-zz", "Favorite restaurants size = " + favoriteRestaurants.size() + "");
        Log.d("query-zz", "FAVORITE_KEY size = " + FAVORITES_KEY.size() + "");
        setFavoritesAdapter();

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
                userNameTextView.setText("Hello, " + name);
                getRestoReviews(name); // get reviews of current user
            }
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
                        userNameTextView.setText("Hello, " + document.getString("name"));
                        userBio.setText(document.getString("bio"));

                        getRestoReviews(document.getString("name")); // get reviews of current user
                    } else {
                        Log.d("query-zz", "No such document");
                    }
                } else {
                    Log.d("query-zz", "failed");
                }
            }
        });
    }

    // Helper function to show the popup window for the bio
    public void show_popup(View v) {
        ImageView close;
        Button save;
        EditText et_bio;

        bio_popup.setContentView(R.layout.bio_popup);

        et_bio = bio_popup.findViewById(R.id.et_bio);
        close = (ImageView) bio_popup.findViewById(R.id.btn_close2);
        save = (Button) bio_popup.findViewById(R.id.btn_save);

        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                et_bio.setText(documentSnapshot.getString("bio"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBio();
                refreshData();
                bio_popup.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bio_popup.dismiss();
            }
        });

        bio_popup.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                finish();
                break;
            case R.id.btn_logout:
                Toast.makeText(ProfileActivity.this, "Signing out", Toast.LENGTH_LONG).show();
                signOut();
            case R.id.tv_editbio:
                show_popup(v);
                break;
        }
    }

    private void updateBio() {
        EditText et_bio = bio_popup.findViewById(R.id.et_bio);
        String newBio = et_bio.getText().toString().trim();

        if (newBio.isEmpty()) {
            et_bio.setError("Please enter a new bio.");
            et_bio.requestFocus();
            return;
        }

        DocumentReference documentReference = fs.collection("users").document(userID);
        documentReference.update("bio", newBio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("query-zz", "Successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("query-zz", "Error updating document", e);
                    }
                });
    }

    // signOut function
    private void signOut() {
        //Intent intent = new Intent(ProfileActivity.this, LandingActivity.class);

        // signOut for Firebase
        if (googleSignIn == 0){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
            finish();

            // signOut for Google
        } else if (googleSignIn == 1){
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
                            finish();
                        }
                    });
        }
    }

    private void getRestoReviews(String name){
        Log.d("TAG", "Name: " + name);
        reviews.clear();

        // Collect resto reviews
        fs.collection("reviews").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                        reviews.add(new ReviewModel(reviewID, uname, rName, reviewText, datePosted, rating));
//                        Log.d(TAG, "Review: " + uname + rName + reviewText + datePosted + rating);
                    }
                } else
                    Log.d("TAG", "No reviews");

                setReviewAdapter();
            }
        });
    }

    private void setFavoritesAdapter(){
        FavoritesAdapter adapter = new FavoritesAdapter(this, favoriteRestaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        favoritesList.setLayoutManager(layoutManager);
        favoritesList.setItemAnimator(new DefaultItemAnimator());
        favoritesList.setAdapter(adapter);
    }

    private void setReviewAdapter(){
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviewsList.setLayoutManager(layoutManager);
        reviewsList.setItemAnimator(new DefaultItemAnimator());
        reviewsList.setAdapter(adapter);
    }

    public ArrayList<ReviewModel> getReviews() { return reviews; }
}