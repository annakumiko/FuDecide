package com.mobdeve.s15.group14.fudecide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        register = (Button) findViewById(R.id.btn_signupview);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.btn_loginview);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signupview:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.btn_loginview:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}