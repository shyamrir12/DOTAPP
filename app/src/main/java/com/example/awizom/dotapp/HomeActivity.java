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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.awizom.dotapp.Fragments.AboutFragment;
import com.example.awizom.dotapp.Fragments.BottomCustomerFragment;
import com.example.awizom.dotapp.Fragments.BottomOrderFragment;
import com.example.awizom.dotapp.Fragments.BottomPrintFragment;
import com.example.awizom.dotapp.Fragments.BottomSearchfragment;
import com.example.awizom.dotapp.Fragments.BottomStatusFragment;
import com.example.awizom.dotapp.Fragments.Help_Fragment;
import com.example.awizom.dotapp.Fragments.TelorListFragment;
import com.example.awizom.dotapp.Fragments.UserListFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;

public class HomeActivity extends AppCompatActivity {

    private Intent intent;
    private Fragment telorListFragment, userListFragment, customerLayoutfragment,
            printLayoutfragment, orderLayoutfragment, statusLayoutFragment, searchfragment,aboutfragment,helpfragment;
    private Fragment fragment = null;
    boolean doubleBackToExitPressedOnce = false;
    private Context mContext;
    private String countValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        if (isConnectingToInternet(HomeActivity.this)) {
//            Toast.makeText(getApplicationContext(), "internet is available", Toast.LENGTH_LONG).show();
//        } else {
//            System.out.print("internet is not available");
//        }
        userListFragment = new UserListFragment();
        customerLayoutfragment = new BottomCustomerFragment();
        printLayoutfragment = new BottomPrintFragment();
        orderLayoutfragment = new BottomOrderFragment();
        statusLayoutFragment = new BottomStatusFragment();
        searchfragment = new BottomSearchfragment();
        aboutfragment = new AboutFragment();
        helpfragment = new Help_Fragment();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(HomeActivity.this);
            alertbox.setIcon(R.drawable.ic_warning_black_24dp);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.admin1:
                getSupportActionBar().setTitle("User List");
                fragment = userListFragment;
                fragmentClass = UserListFragment.class;
                break;

            case R.id.user1:
                getSupportActionBar().setTitle("Telor List");
                fragment = telorListFragment;
                fragmentClass = TelorListFragment.class;
                break;


            case R.id.home:
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                finish();
                onStop();
                break;
            case R.id.exit:

                SharedPrefManager.getInstance(this).logout();
                Intent login = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(login);


                finish();

                break;
            case R.id.aboutApp:
                getSupportActionBar().setTitle("About Details");
                fragment = aboutfragment;
                fragmentClass = AboutFragment.class;
                break;


            case R.id.help:
                getSupportActionBar().setTitle("HELP");
                fragment = helpfragment;
                fragmentClass = Help_Fragment.class;
                break;

            //return (true);
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
                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin")) ||
                            (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("User")))   {
                        getSupportActionBar().setTitle("Customer Details");
                        fragment = customerLayoutfragment;
                        fragmentClass = BottomCustomerFragment.class;
                    } else{
                        Toast.makeText(getApplicationContext(),"User Is Not Permitted",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.navigation_order:
                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin")) ||
                            (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("User")))   {
                    getSupportActionBar().setTitle("Order Details");
                    fragment = orderLayoutfragment;
                    fragmentClass = BottomOrderFragment.class;
                    } else{
                        Toast.makeText(getApplicationContext(),"User Is Not Permitted",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.navigation_status:
                    getSupportActionBar().setTitle("Status Details");
                    fragment = statusLayoutFragment;
                    fragmentClass = BottomStatusFragment.class;


                    break;
                case R.id.navigation_print:
                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin")) ||
                            (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("User")))   {

                        getSupportActionBar().setTitle("Print Details");
                    fragment = printLayoutfragment;
                    fragmentClass = BottomPrintFragment.class;

                    } else{
                        Toast.makeText(getApplicationContext(),"User Is Not Permitted",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.navigation_search:

                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin")) ||
                            (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("User")))   {

                        getSupportActionBar().setTitle("Search Details");
                    fragment = searchfragment;
                    fragmentClass = BottomSearchfragment.class;
                    } else{
                        Toast.makeText(getApplicationContext(),"User Is Not Permitted",Toast.LENGTH_SHORT).show();
                    }
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

