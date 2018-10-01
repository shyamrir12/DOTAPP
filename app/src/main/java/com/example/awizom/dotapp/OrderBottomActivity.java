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
import com.example.awizom.dotapp.Fragments.OrderCreateFragment;
import com.example.awizom.dotapp.Fragments.PendinOrderCreateFragment;

public class OrderBottomActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private TextView pendingOrderList,pendingOrderCreate,cancelOrder;
    private Intent intent;
    private Fragment ordercreateFragment, pendinOrderListFragment;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_layout);
        initView();
    }

    private void initView() {
        cardViewFirst = findViewById(R.id.order_pending_cardview);
        cardViewSecond = findViewById(R.id.order_create_cardview);
        cardViewthird = findViewById(R.id.order_cancel_cardview);

        pendingOrderList = findViewById(R.id.pendingOrder);
        pendingOrderCreate = findViewById(R.id.orderCreate);
        cancelOrder = findViewById(R.id.cancelOrder);

        ordercreateFragment = new OrderCreateFragment();
        pendinOrderListFragment = new PendinOrderCreateFragment();

        cardViewFirst.setOnClickListener(this);
        cardViewSecond.setOnClickListener(this);
        cardViewthird.setOnClickListener(this);

        pendingOrderList.setOnClickListener(this);
        pendingOrderCreate.setOnClickListener(this);
        cancelOrder.setOnClickListener(this);

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
            case R.id.order_create_cardview:
                fragment = ordercreateFragment;
                fragmentClass = OrderCreateFragment.class;
                break;
            case R.id.order_pending_cardview:
                fragment = pendinOrderListFragment;
                fragmentClass = PendinOrderCreateFragment.class;
                break;
            case R.id.orderCreate:
                fragment = ordercreateFragment;
                fragmentClass = OrderCreateFragment.class;
                break;
            case R.id.pendingOrder:
                fragment = ordercreateFragment;
                fragmentClass = OrderActivity.class;
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
