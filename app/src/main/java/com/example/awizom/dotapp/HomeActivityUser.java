package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.AboutFragment;
import com.example.awizom.dotapp.Fragments.BottomCustomerFragment;
import com.example.awizom.dotapp.Fragments.BottomOrderFragment;
import com.example.awizom.dotapp.Fragments.BottomPrintFragment;
import com.example.awizom.dotapp.Fragments.BottomSearchfragment;
import com.example.awizom.dotapp.Fragments.BottomStatusFragment;
import com.example.awizom.dotapp.Fragments.Help_Fragment;
import com.example.awizom.dotapp.Fragments.RoomNameListFragment;
import com.example.awizom.dotapp.Fragments.TelorListFragment;
import com.example.awizom.dotapp.Fragments.UserListFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.PermissionList;
import com.example.awizom.dotapp.Models.UserModel;
import com.example.awizom.dotapp.Models.UserPermissionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HomeActivityUser extends AppCompatActivity {

    private Intent intent;
    private Fragment userListFragment,telorListFragment,roomListFragment, customerLayoutfragment, printLayoutfragment, orderLayoutfragment,
            statusLayoutFragment, searchfragment, aboutfragment,helpfragment;
    Fragment fragment = null;
    private UserPermissionModel userPermissionModel;
    private List<PermissionList> permissionList;
    List<UserModel> userItemList;
    String userId;
    Class fragmentClass;
  String  permissionStatusName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userListFragment = new UserListFragment();
        customerLayoutfragment = new BottomCustomerFragment();
        printLayoutfragment = new BottomPrintFragment();
        orderLayoutfragment = new BottomOrderFragment();
        statusLayoutFragment = new BottomStatusFragment();
        searchfragment = new BottomSearchfragment();
        aboutfragment = new AboutFragment();
        roomListFragment = new RoomNameListFragment();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(HomeActivityUser.this);
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
            case R.id.home:
                Intent i = new Intent(this, HomeActivityUser.class);
                startActivity(i);
                finish();
                break;
//
//
//
            case R.id.exit:
                SharedPrefManager.getInstance(this).logout();
                Intent login = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(login);
                //
                break;
            case R.id.aboutApp:
                getSupportActionBar().setTitle("About Details");
                fragment = aboutfragment;
                fragmentClass = AboutFragment.class;
                break;

            case R.id.help:

                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;

            case R.id.tailorList:

                getSupportActionBar().setTitle("Telor List");
                fragment = telorListFragment;
                fragmentClass = TelorListFragment.class;
                break;
            case R.id.roomList:

                getSupportActionBar().setTitle("Room List");
                fragment = roomListFragment;
                fragmentClass = RoomNameListFragment.class;
                break;
            case R.id.changPwd:

                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
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

                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin"))) {

                        getSupportActionBar().setTitle("Customer Details");
                        fragment = customerLayoutfragment;
                        fragmentClass = BottomCustomerFragment.class;

                    } else if((SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userRole.contains("User")) ) {
                        permissionStatusName="Party";
                        userPermissionGet();

                    }else {
                        Toast toast = Toast.makeText(HomeActivityUser.this, "Not Permitted", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case R.id.navigation_order:

                    if ((SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("Admin")) ||
                            (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserRole().contains("User"))) {


                        getSupportActionBar().setTitle("Order Details");
                        fragment = orderLayoutfragment;
                        fragmentClass = BottomOrderFragment.class;

                    } else {
                        Toast.makeText(getApplicationContext(), "User Is Not Permitted", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.navigation_print:
                    if ((SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userRole.contains("Admin")) ){

                        getSupportActionBar().setTitle("Print Details");
                        fragment = printLayoutfragment;
                        fragmentClass = BottomPrintFragment.class;

                    } else if((SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userRole.contains("User")) ) {
                        permissionStatusName="Print";
                        userPermissionGet();

                    }else {
                        Toast toast = Toast.makeText(HomeActivityUser.this, "Not Permitted", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case R.id.navigation_status:
                    getSupportActionBar().setTitle("Status Details");
                    fragment = statusLayoutFragment;
                    fragmentClass = BottomStatusFragment.class;
                    break;
                case R.id.navigation_search:
                    if ((SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userRole.contains("Admin")) ){

                        getSupportActionBar().setTitle("Status Details");
                        fragment = searchfragment;
                        fragmentClass = BottomSearchfragment.class;

                    } else if((SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userRole.contains("User")) ) {
                        permissionStatusName="Search";
                        userPermissionGet();

                    }else {
                        Toast toast = Toast.makeText(HomeActivityUser.this, "Not Permitted", Toast.LENGTH_SHORT);
                        toast.show();
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


    private void userPermissionGet() {

        try {

            new userPermissionGetDetail().execute(SharedPrefManager.getInstance(HomeActivityUser.this).getUser().userID,SharedPrefManager.getInstance(HomeActivityUser.this).getUser().access_token);
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    private class userPermissionGetDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String userID = params[0];

            String accesstoken = params[1];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "UserPermissionGet/" + userID);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(HomeActivityUser.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }
        protected void onPostExecute(String result) {
            if (result.isEmpty()) {

                Toast.makeText(HomeActivityUser.this, "Invalid request", Toast.LENGTH_SHORT).show();
            }else {
                Gson gson = new Gson();
                Type listType = new TypeToken<UserPermissionModel>() {
                }.getType();
                if(SharedPrefManager.getInstance(HomeActivityUser.this).getUser().getUserRole().equals("Admin")) {
                    return;
                }
                userPermissionModel = new Gson().fromJson(result, listType);
                if(userPermissionModel != null){

                    permissionList = userPermissionModel.getPermissionList();
                    List<String> perList = new ArrayList<>();
                    for(PermissionList up : permissionList){
                        perList.add(up.getPermissionName());
                    }
                    if(perList.contains( permissionStatusName ))
                    {  if (permissionStatusName.equals( "Party" )) {
                        getSupportActionBar().setTitle("Customer Details");
                        fragment = customerLayoutfragment;
                        fragmentClass = BottomCustomerFragment.class;


                    } else  if (permissionStatusName.equals( "Search" )) {
                        getSupportActionBar().setTitle("Search Details");
                        fragment = searchfragment;
                        fragmentClass = BottomSearchfragment.class;


                    }
                    else  if (permissionStatusName.equals( "Print" )) {
                        getSupportActionBar().setTitle("Print Details");
                        fragment = printLayoutfragment;
                        fragmentClass = BottomPrintFragment.class;


                    }

                    }
                    else {

                        Toast toast = Toast.makeText(HomeActivityUser.this, "Not Permitted", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
                        setTitle("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//
//                    boolean check=false;
//                    for(int i=0; i<permissionList.size(); i++){
//
//                        if (permissionList.get(i).getPermissionName().equals("Party")) {
//                            getSupportActionBar().setTitle("Customer Details");
//                            fragment = customerLayoutfragment;
//                            fragmentClass = BottomCustomerFragment.class;
//                            check=true;
//                        }else  if (permissionList.get(i).getPermissionName().equals("Search")) {
//                            getSupportActionBar().setTitle("Search Details");
//                            fragment = searchfragment;
//                            fragmentClass = BottomSearchfragment.class;
//                            check=true;
//                        }else if(permissionList.get(i).getPermissionName().equals("Print")) {
//                            getSupportActionBar().setTitle("Print Details");
//                            fragment = printLayoutfragment;
//                            fragmentClass = BottomPrintFragment.class;
//                            check=true;
//                        }else {
//                            if (check == false) {
//                                Toast toast = Toast.makeText(HomeActivityUser.this, "Not Permitted", Toast.LENGTH_SHORT);
//                                toast.show();
//                            }
//                        }
//                    }


                }

            }
        }
    }

}
