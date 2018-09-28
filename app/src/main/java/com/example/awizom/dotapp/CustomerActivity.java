package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class CustomerActivity extends AppCompatActivity {

    private TextView customerList,customerModify,customerAdd, mTextMessage;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_layout);
        initView();
    }

    private void initView() {
        customerList = findViewById(R.id.customer);
        customerModify = findViewById(R.id.customerModify);
        customerAdd = findViewById(R.id.customerAdd);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_customer:
                    startActivity(intent = new Intent(getApplicationContext(),CustomerActivity.class));
                    mTextMessage.setText(R.string.title_customer);
                    return true;
                case R.id.navigation_order:
                    mTextMessage.setText(R.string.title_order);
                    return true;
                case R.id.navigation_report:
                    mTextMessage.setText(R.string.title_report);
                    return true;
                case R.id.navigation_status:
                    mTextMessage.setText(R.string.title_status);
                    return true;
            }
            return false;
        }
    };



}
