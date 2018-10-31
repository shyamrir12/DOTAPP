package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.OrderListFragment;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView pendingorder, pendingcustomer, pendingreceived, pendingitemToPlace;
    private Intent intent;
    private Fragment reportpendingOrderFragment;
    private CardView cardViewFirst, cardViewSecond, cardViewthird, cardViewFourth;
    Fragment fragment = null;

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
        pendingorder.setOnClickListener(this);
        pendingcustomer.setOnClickListener(this);
        pendingreceived.setOnClickListener(this);
        pendingitemToPlace.setOnClickListener(this);
        reportpendingOrderFragment = new OrderListFragment();

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
                    startActivity(intent = new Intent(getApplicationContext(), CustomerActivity.class));
                    return true;
                case R.id.navigation_order:
                    startActivity(intent = new Intent(getApplicationContext(), OrderBottomActivity.class));
                    return true;
//                case R.id.navigation_report:
//                    startActivity(intent = new Intent(getApplicationContext(), ReportActivity.class));
//                    return true;
                case R.id.navigation_status:
                    startActivity(intent = new Intent(getApplicationContext(), StatusActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
        switch (v.getId()) {
            case R.id.pendingOrder:
                Bundle bundle = new Bundle();
                bundle.putString("NAME_KEY", "PendingOrderwithadvance");
                OrderListFragment myFragment = new OrderListFragment();
                myFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).commit();
                getSupportActionBar().setTitle("Pending Order with advance");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingCustomer:
                Bundle bundle11 = new Bundle();
                bundle11.putString("NAME_KEY", "PendingCustomerwithnoadvance");
                OrderListFragment myFragment1 = new OrderListFragment();
                myFragment1.setArguments(bundle11);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment1).commit();

                getSupportActionBar().setTitle("Pending Customer with no advance");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingReceived:

                Bundle bundle12 = new Bundle();
                bundle12.putString("NAME_KEY", "PendingMaterialReceived");
                OrderListFragment myFragment2 = new OrderListFragment();
                myFragment2.setArguments(bundle12);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment2).commit();

                getSupportActionBar().setTitle("Pending Material Received");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingItemToPlace:

                Bundle bundle13 = new Bundle();
                bundle13.putString("NAME_KEY", "Pendingitemtoplaceholder");
                OrderListFragment myFragment3 = new OrderListFragment();
                myFragment3.setArguments(bundle13);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment3).commit();

                getSupportActionBar().setTitle("Pending item to placeholder");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
