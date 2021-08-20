package com.mobdeve.s15.group14.fudecide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView home, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        home = (ImageView) findViewById(R.id.btn_home);
        home.setOnClickListener(this);

        logout = (ImageView) findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                break;
        }
    }
}