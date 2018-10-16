package com.example.awizom.dotapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Fragments.BottomCustomerFragment;
import com.example.awizom.dotapp.Fragments.BottomOrderFragment;
import com.example.awizom.dotapp.Fragments.BottomReportFragment;
import com.example.awizom.dotapp.Fragments.BottomStatusFragment;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;

import static com.example.awizom.dotapp.MainActivity.isConnectingToInternet;

public class HomeActivity extends AppCompatActivity {

    private Intent intent;
    private Fragment customerLayoutfragment, reportLayoutfragment,orderLayoutfragment,statusLayoutFragment;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (isConnectingToInternet(HomeActivity.this)) {
            Toast.makeText(getApplicationContext(), "internet is available", Toast.LENGTH_LONG).show();
        } else {
            System.out.print("internet is not available");
        }

        customerLayoutfragment = new BottomCustomerFragment();
        reportLayoutfragment = new BottomReportFragment();
        orderLayoutfragment = new BottomOrderFragment();
        statusLayoutFragment = new BottomStatusFragment();
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
                    getSupportActionBar().setTitle("Customer Details");
                    fragment = customerLayoutfragment;
                    fragmentClass = BottomCustomerFragment.class;
                    break;
                case R.id.navigation_order:
                    getSupportActionBar().setTitle("Order Details");
                    fragment = orderLayoutfragment;
                    fragmentClass = BottomOrderFragment.class;
                    break;
                case R.id.navigation_report:
                    getSupportActionBar().setTitle("Report Details");
                    fragment = reportLayoutfragment;
                    fragmentClass = BottomReportFragment.class;
                    break;
                case R.id.navigation_status:
                    getSupportActionBar().setTitle("Status Details");
                    fragment = statusLayoutFragment;
                    fragmentClass = BottomStatusFragment.class;
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
            return false;
        }
    };

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
