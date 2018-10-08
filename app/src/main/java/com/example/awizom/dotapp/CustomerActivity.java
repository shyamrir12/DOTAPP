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

import com.example.awizom.dotapp.Fragments.AddCustomerFragment;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.ModifyCustomerFragment;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView customerList, customerModify, customerAdd;
    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private Intent intent;
    private Fragment addCustomerFragment, modifyCustomerFragment, listCustomerFragment;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_layout);
        initView();
    }

    private void initView() {

        customerList = findViewById(R.id.customerList);
        customerModify = findViewById(R.id.modifyCustomer);
        customerAdd = findViewById(R.id.addCustomer);

        cardViewFirst = findViewById(R.id.first_cardview);
        cardViewSecond = findViewById(R.id.second_cardview);
        cardViewthird = findViewById(R.id.third_cardview);

        addCustomerFragment = new AddCustomerFragment();
        modifyCustomerFragment = new ModifyCustomerFragment();
        listCustomerFragment = new CustomerListFrgment();

        customerList.setOnClickListener(this);
        customerModify.setOnClickListener(this);
        customerAdd.setOnClickListener(this);

        cardViewFirst.setOnClickListener(this);
        cardViewSecond.setOnClickListener(this);
        cardViewthird.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_customer:
                    startActivity(intent = new Intent(getApplicationContext(), CustomerActivity.class));
                    return true;
                case R.id.navigation_order:
                    startActivity(intent = new Intent(getApplicationContext(), OrderBottomActivity.class));
                    return true;
                case R.id.navigation_report:
                    startActivity(intent = new Intent(getApplicationContext(), ReportActivity.class));
                    return true;
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
            case R.id.first_cardview:
                getSupportActionBar().setTitle("Add Customer");
                fragment = addCustomerFragment;
                fragmentClass = AddCustomerFragment.class;
                break;
            case R.id.second_cardview:
                getSupportActionBar().setTitle("Modify Customer");
                fragment = modifyCustomerFragment;
                fragmentClass = ModifyCustomerFragment.class;
                break;
            case R.id.third_cardview:
                getSupportActionBar().setTitle("Customer List");
                fragment = listCustomerFragment;
                fragmentClass = CustomerListFrgment.class;
                break;
            case R.id.customerList:
                getSupportActionBar().setTitle("Customer List");
                fragmentClass = CustomerListFrgment.class;
                break;
            case R.id.orderCreate:
                getSupportActionBar().setTitle("Add Customer");
                fragmentClass = AddCustomerFragment.class;
                break;

            case R.id.modifyCustomer:
                getSupportActionBar().setTitle("Modify Customer");
                fragment = modifyCustomerFragment;
                fragmentClass = ModifyCustomerFragment.class;
                break;
            case R.id.addCustomer:
                getSupportActionBar().setTitle("Pending Order");
                fragmentClass = AddCustomerFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
