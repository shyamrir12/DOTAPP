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

import com.example.awizom.dotapp.Fragments.OrderListFragment;

public class OrderBottomActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private TextView pendingOrderList, pendingOrderCreate, cancelOrder;
    private Intent intent;
    private Fragment pendinOrderListFragment, orderCreate;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_layout);
        initView();
    }

    private void initView() {

        getSupportActionBar().setTitle("Order Details");
        cardViewFirst = findViewById(R.id.order_pending_cardview);
        cardViewSecond = findViewById(R.id.order_create_cardview);
//        cardViewthird = findViewById(R.id.order_cancel_cardview);


        pendingOrderList = findViewById(R.id.pendingOrder);
        pendingOrderCreate = findViewById(R.id.orderCreate);
//        cancelOrder = findViewById(R.id.cancelOrder);


        pendinOrderListFragment = new OrderListFragment();
 //       orderCreate = new AfterCreateOrderoFragment();

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

            case R.id.order_create_cardview:
                getSupportActionBar().setTitle("Order Create");
                fragment = orderCreate;
                //      fragmentClass = AfterCreateOrderoFragment.class;
                break;
            case R.id.order_pending_cardview:

                Bundle bundle2 = new Bundle();
                bundle2.putString("NAME_KEY", "PendingOrderList");
                OrderListFragment myFragment2 = new OrderListFragment();
                myFragment2.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment2).commit();

                getSupportActionBar().setTitle("Pending List");
                fragment = pendinOrderListFragment;
                fragmentClass = OrderListFragment.class;
                break;
//            case R.id.order_cancel_cardview:
//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_KEY", "CancelOrderList");
//                OrderListFragment myFragment = new OrderListFragment();
//                myFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//
//
//                getSupportActionBar().setTitle("Cancel List");
//                fragment = pendinOrderListFragment;
//                fragmentClass = OrderListFragment.class;
//                break;
            case R.id.orderCreate:
                fragment = orderCreate;
                getSupportActionBar().setTitle("Order Create");
                //  fragmentClass = AfterCreateOrderoFragment.class;

                break;
            case R.id.pendingOrder:

                Bundle bundle3 = new Bundle();
                bundle3.putString("NAME_KEY", "PendingOrderList");
                OrderListFragment myFragment3 = new OrderListFragment();
                myFragment3.setArguments(bundle3);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment3).commit();


                getSupportActionBar().setTitle("Pending List");
                fragment = pendinOrderListFragment;
                fragmentClass = OrderListFragment.class;
                break;
//            case R.id.cancelOrder:
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("NAME_KEY", "CancelOrderList");
//                OrderListFragment myFragment1 = new OrderListFragment();
//                myFragment1.setArguments(bundle1);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment1).commit();
//
//
//                getSupportActionBar().setTitle("Cancel List");
//                fragment = pendinOrderListFragment;
//                fragmentClass = OrderListFragment.class;
//                break;
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
