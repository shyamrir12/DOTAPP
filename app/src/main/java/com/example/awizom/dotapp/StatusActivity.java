package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.OrderListFragment;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView handOver, tailOr, receIvedby, pendingtoPlaceOrder,pendingToreceivedMaterial,pendingtorecevefrometailor,cancelList,dispatchList;
    private Intent intent;
    private Fragment statuspendingOrderFragment;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_layout);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Status Details");
//        handOver = findViewById(R.id.pendingToreceivedMaterial);
////        tailOr = findViewById(R.id.tailor);
//        receIvedby = findViewById(R.id.receivedby);
//  //      pendingtoPlaceOrder = findViewById(R.id.pendingToPlaceOrder);
//        pendingToreceivedMaterial = findViewById(R.id.pendingtToPlaceOrder);
//    //    pendingtorecevefrometailor = findViewById(R.id.pendingTorecevefrometailor);
//  //      cancelList = findViewById(R.id.cancel);
//        dispatchList = findViewById(R.id.dispatch);

//        handOver.setOnClickListener(this);
//        tailOr.setOnClickListener(this);
//        tailOr.setOnClickListener(this);
//        receIvedby.setOnClickListener(this);
//        pendingtoPlaceOrder.setOnClickListener(this);
//        pendingToreceivedMaterial.setOnClickListener(this);
//        pendingtorecevefrometailor.setOnClickListener(this);
//        cancelList.setOnClickListener(this);
//        dispatchList.setOnClickListener(this);
//
//        statuspendingOrderFragment = new CustomerListFrgment();


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
//
//        Class fragmentClass = null;
//        switch (v.getId()){
//            case R.id.pendingToreceivedMaterial:
//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_KEY", "HandOverTo");
//                OrderListFragment myFragment = new OrderListFragment();
//                myFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//                getSupportActionBar().setTitle("Pending Order with advance");
//                fragment = statuspendingOrderFragment;
//                fragmentClass = OrderListFragment.class;
//                break;
//
////            case R.id.tailor:
////                Bundle bundle11 = new Bundle();
////                bundle11.putString("NAME_KEY", "TelorName");
////                OrderListFragment myFragment1 = new OrderListFragment();
////                myFragment1.setArguments(bundle11);
////                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment1).commit();
////
////                break;
//
//            case R.id.receivedby:
//                Bundle bundle12 = new Bundle();
//                bundle12.putString("NAME_KEY", "PendingReceivedBy");
//                OrderListFragment myFragment2 = new OrderListFragment();
//                myFragment2.setArguments(bundle12);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();
//
//                break;
//
////            case R.id.pendingToPlaceOrder:
////                Bundle bundle13 = new Bundle();
////                bundle13.putString("NAME_KEY", "Pendingitemtoplaceholder");
////                OrderListFragment myFragment3 = new OrderListFragment();
////                myFragment3.setArguments(bundle13);
////                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment3).commit();
////
////                break;
//
//            case R.id.pendingtToPlaceOrder:
//                Bundle bundle14 = new Bundle();
//                bundle14.putString("NAME_KEY", "PendingMaterialReceived");
//                OrderListFragment myFragment4 = new OrderListFragment();
//                myFragment4.setArguments(bundle14);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment4).commit();
//                break;
//
////            case R.id.cancel:
////                Bundle bundle15 = new Bundle();
////                bundle15.putString("NAME_KEY", "CancelOrderlist");
////                OrderListFragment myFragment5 = new OrderListFragment();
////                myFragment5.setArguments(bundle15);
////                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment5).commit();
////                break;
//
//            case R.id.dispatch:
//                Bundle bundle16 = new Bundle();
//                bundle16.putString("NAME_KEY", "Dispatch");
//                OrderListFragment myFragment6 = new OrderListFragment();
//                myFragment6.setArguments(bundle16);
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment6).commit();
//                break;
//
//        }
   }
}
