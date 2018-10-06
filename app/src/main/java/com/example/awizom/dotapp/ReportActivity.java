package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.awizom.dotapp.Fragments.CustomerListFrgment;

public class ReportActivity extends AppCompatActivity {

    private TextView pendingorder,pendingcustomer,pendingreceived, pendingitemToPlace;
    private Intent intent;
    private Fragment reportpendingOrderFragment,pendingCustomerFragment,pendingReceivedfragment,pendingItemToPlaceFragment;
    private CardView cardViewFirst, cardViewSecond, cardViewthird,cardViewFourth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_layout);
        initView();
    }

    private void initView() {

        getSupportActionBar().setTitle("Report Details");
        pendingorder = findViewById(R.id.pendingOrder);
        pendingcustomer = findViewById(R.id.pendingCustomer);
        pendingreceived = findViewById(R.id.pendingReceived);
        pendingitemToPlace = findViewById(R.id.pendingItemToPlace);

        cardViewFirst = findViewById(R.id.first_cardview);
        cardViewSecond = findViewById(R.id.second_cardview);
        cardViewthird = findViewById(R.id.third_cardview);
        cardViewFourth = findViewById(R.id.fourth_cardView);

        cardViewFirst = findViewById(R.id.first_cardview);
        cardViewSecond = findViewById(R.id.second_cardview);
        cardViewthird = findViewById(R.id.third_cardview);
        cardViewFourth = findViewById(R.id.third_cardview);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class fragmentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_customer:
                    startActivity(intent = new Intent(getApplicationContext(),CustomerActivity.class));
                    return true;
                case R.id.navigation_order:
                    startActivity(intent = new Intent(getApplicationContext(),OrderBottomActivity.class));
                    return true;
                case R.id.navigation_report:
                    startActivity(intent = new Intent(getApplicationContext(),ReportActivity.class));
                    return true;
                case R.id.navigation_status:
                    startActivity(intent = new Intent(getApplicationContext(),StatusActivity.class));
                    return true;
            }
            return false;
        }
    };
}
