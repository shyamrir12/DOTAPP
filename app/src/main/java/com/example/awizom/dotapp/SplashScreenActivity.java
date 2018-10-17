package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        initView();
    }

    private void initView() {

        new Handler().postDelayed(new Runnable() {@Override
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(SplashScreenActivity.this, SigninActivity.class);
            finish();
            startActivity(i);

            // close this activity

        }
        }, SPLASH_TIME_OUT);
    }


}
