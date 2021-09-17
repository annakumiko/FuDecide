package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    Sample function to avoid manually inputting restaurants in the db
    TASKS:
        [/] Add restaurant details
        [ ] Add multiple menu items
 */
public class AddRestaurantActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AddResto";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText restoName, restoDescription, openHours;
    private EditText itemName0, itemPrice0;
    private EditText itemName1, itemPrice1;
    private EditText itemName2, itemPrice2;
    private EditText itemName3, itemPrice3;
    private EditText itemName4, itemPrice4;
//    private EditText itemName5, itemPrice5;
//    private EditText itemName6, itemPrice6;
//    private EditText itemName7, itemPrice7;
//    private EditText itemName8, itemPrice8;
//    private EditText itemName9, itemPrice9;

    private Button saveRestoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        restoName = findViewById(R.id.restoName);
        restoDescription = findViewById(R.id.restoDescription);
        openHours = findViewById(R.id.openHours);

        itemName0 = findViewById(R.id.itemName0);
        itemPrice0 = findViewById(R.id.itemName0);

        itemName1 = findViewById(R.id.itemName1);
        itemPrice1 = findViewById(R.id.itemName1);

        itemName2 = findViewById(R.id.itemName2);
        itemPrice2 = findViewById(R.id.itemName2);

        itemName3 = findViewById(R.id.itemName3);
        itemPrice3 = findViewById(R.id.itemName3);

        itemName4 = findViewById(R.id.itemName4);
        itemPrice4 = findViewById(R.id.itemName4);

//        itemName5 = findViewById(R.id.itemName5);
//        itemPrice5 = findViewById(R.id.itemName5);
//
//        itemName6 = findViewById(R.id.itemName6);
//        itemPrice6 = findViewById(R.id.itemName6);
//
//        itemName7 = findViewById(R.id.itemName7);
//        itemPrice7 = findViewById(R.id.itemName7);
//
//        itemName8 = findViewById(R.id.itemName8);
//        itemPrice8 = findViewById(R.id.itemName8);
//
//        itemName9 = findViewById(R.id.itemName9);
//        itemPrice9 = findViewById(R.id.itemName9);

        saveRestoBtn = findViewById(R.id.saveRestoBtn);
        saveRestoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurant();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void saveRestaurant(){
        // get values from inputs
        String rName = restoName.getText().toString();
        String rDesc = restoDescription.getText().toString();
        String time = openHours.getText().toString();

        String menuItem0 = itemName0.getText().toString();
        String menuPrice0 = itemPrice0.getText().toString();

        String menuItem1 = itemName1.getText().toString();
        String menuPrice1 = itemPrice1.getText().toString();

        String menuItem2 = itemName2.getText().toString();
        String menuPrice2 = itemPrice2.getText().toString();

        String menuItem3 = itemName3.getText().toString();
        String menuPrice3 = itemPrice3.getText().toString();

        String menuItem4 = itemName4.getText().toString();
        String menuPrice4 = itemPrice4.getText().toString();

//        String menuItem5 = itemName5.getText().toString();
//        String menuPrice5 = itemPrice5.getText().toString();
//
//        String menuItem6 = itemName6.getText().toString();
//        String menuPrice6 = itemPrice6.getText().toString();
//
//        String menuItem7 = itemName7.getText().toString();
//        String menuPrice7 = itemPrice7.getText().toString();
//
//        String menuItem8 = itemName8.getText().toString();
//        String menuPrice8 = itemPrice8.getText().toString();
//
//        String menuItem9 = itemName9.getText().toString();
//        String menuPrice9 = itemPrice9.getText().toString();

        // save to db (restaurants collection)
        CollectionReference restoDB = db.collection("restaurants");

        // put menu items into one object
        Map<String, Object> menu0 = new HashMap<>();
        menu0.put("itemName", menuItem0);
        menu0.put("itemPrice", menuPrice0);

        Map<String, Object> menu1 = new HashMap<>();
        menu1.put("itemName", menuItem1);
        menu1.put("itemPrice", menuPrice1);

        Map<String, Object> menu2 = new HashMap<>();
        menu2.put("itemName", menuItem2);
        menu2.put("itemPrice", menuPrice2);

        Map<String, Object> menu3 = new HashMap<>();
        menu3.put("itemName", menuItem3);
        menu3.put("itemPrice", menuPrice3);

        Map<String, Object> menu4 = new HashMap<>();
        menu4.put("itemName", menuItem4);
        menu4.put("itemPrice", menuPrice4);

//        Map<String, Object> menu5 = new HashMap<>();
//        menu5.put("itemName", menuItem5);
//        menu5.put("itemPrice", menuPrice5);
//
//        Map<String, Object> menu6 = new HashMap<>();
//        menu6.put("itemName", menuItem6);
//        menu6.put("itemPrice", menuPrice6);
//
//        Map<String, Object> menu7 = new HashMap<>();
//        menu7.put("itemName", menuItem7);
//        menu7.put("itemPrice", menuPrice7);
//
//        Map<String, Object> menu8 = new HashMap<>();
//        menu8.put("itemName", menuItem8);
//        menu8.put("itemPrice", menuPrice8);
//
//        Map<String, Object> menu9 = new HashMap<>();
//        menu9.put("itemName", menuItem9);
//        menu9.put("itemPrice", menuPrice9);

        // put objects into an array
        Map<Integer, Object> menuObj = new HashMap<>();
        menuObj.put(0, menu0);
        menuObj.put(1, menu1);
        menuObj.put(2, menu2);
        menuObj.put(3, menu3);
        menuObj.put(4, menu4);
//        menuObj.put(5, menu5);
//        menuObj.put(6, menu6);
//        menuObj.put(7, menu7);
//        menuObj.put(8, menu8);
//        menuObj.put(9, menu9);

        // put menu object and other details in one resto object
        Map<String, Object> restoObj = new HashMap<>();
        restoObj.put("openHours", time);
        restoObj.put("latitude", 14);
        restoObj.put("longitude", 120);
        restoObj.put("overallRating", 0);
        restoObj.put("restoDescription", rDesc);
        restoObj.put("restoName", rName);
        restoObj.put("restoPhoto", "");
        restoObj.put("restoIcon", "");
        restoObj.put("menu", menuObj);

        // add object to restaurants collection
        restoDB.add(restoObj)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Restaurant added");

                            finish();
                            Toast.makeText(AddRestaurantActivity.this, "Restaurant added", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}