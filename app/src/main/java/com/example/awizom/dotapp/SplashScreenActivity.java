package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.dotapp.Helper.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        initView();
    }

    private void initView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent i = new Intent(SplashScreenActivity.this, SigninActivity.class);

                startActivity(i);
                finish();


            }
        }, SPLASH_TIME_OUT);
    }


}
