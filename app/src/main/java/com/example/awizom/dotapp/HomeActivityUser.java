package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Fragments.AddCustomerFragment;
import com.example.awizom.dotapp.Fragments.BottomCustomerFragment;
import com.example.awizom.dotapp.Fragments.BottomOrderFragment;
import com.example.awizom.dotapp.Fragments.BottomReportFragment;
import com.example.awizom.dotapp.Fragments.BottomStatusFragment;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.UserListFragment;
import com.example.awizom.dotapp.R;

public class HomeActivityUser extends AppCompatActivity {

    private Intent intent;
    private Fragment userListFragment, customerLayoutfragment, reportLayoutfragment, orderLayoutfragment, statusLayoutFragment;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (isConnectingToInternet(HomeActivityUser.this)) {
            Toast.makeText(getApplicationContext(), "internet is available", Toast.LENGTH_LONG).show();
        } else {
            System.out.print("internet is not available");
        }
        userListFragment = new UserListFragment();
        customerLayoutfragment = new BottomCustomerFragment();
        reportLayoutfragment = new BottomReportFragment();
        orderLayoutfragment = new BottomOrderFragment();
        statusLayoutFragment = new BottomStatusFragment();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(HomeActivityUser.this);
            alertbox.setIcon(R.drawable.warning);
            alertbox.setTitle("Do You Want To Exit Programme?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
                    finishAffinity();
                    System.exit(0);


                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button
                    // of Dialog
                }
            });

            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
    }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main_user, menu);
       return true;
   }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class fragmentClass = null;

       switch (item.getItemId()) {
  //          case R.id.admin1:
//                getSupportActionBar().setTitle("User List");
//                fragment = userListFragment;
//                fragmentClass = UserListFragment.class;
//                break;
//
//            //  case R.id.user1:
//
//            // Toast.makeText(this, "user is clicked ", Toast.LENGTH_LONG).show();
//
//            //     return (true);
        case R.id.about:
                Intent i = new Intent(this, HomeActivityUser.class);
                startActivity(i);
                finish();
                break;
//
//
//
          case R.id.exit:
              Intent intt = new Intent(this, SigninActivity.class);
              startActivity(intt);
              finish();
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

        return (super.onOptionsItemSelected(item));
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
//                case R.id.navigation_report:
//                    getSupportActionBar().setTitle("Report Details");
//                    fragment = reportLayoutfragment;
//                    fragmentClass = BottomReportFragment.class;
//                    break;
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
