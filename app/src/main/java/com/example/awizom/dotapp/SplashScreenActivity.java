package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.RoomListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    String[] values,data,hold,dispatch,placeorder,recedMaterial,pendinfradvance,pendinHandOver,recdfromtailor;
    String sendParam;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        initView();
    }


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

    private void initView() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isConnectingToInternet(SplashScreenActivity.this)) {

                    statusCountGETmethodCall();
                } else {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(SplashScreenActivity.this);
                    alertbox.setIcon(R.drawable.ic_warning_black_24dp);
                    alertbox.setTitle("Internet Connection Is Not Available");
                    alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //   startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            // Intent intent = new Intent(Intent.ACTION_MAIN);
                            //   intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                            //   startActivity(intent);


                            finishAffinity();
                            System.exit(0);

                        }
                    });

                    alertbox.show();


                }


            }
        }, SPLASH_TIME_OUT);
    }

    private void statusCountGETmethodCall() {

        try {

        new statusCountGET().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class statusCountGET extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = params[0];

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "StatusCountGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent login = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(login);


            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    Intent login = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(login);


                } else {

                    intent = new Intent(SplashScreenActivity.this, SigninActivity.class);

                    startActivity(intent);
                    finish();

//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<String[]>(){}.getType();
//                    values= gson.fromJson(result, listType);
//
//                    pendinfradvance = values[0].split("=");
//                    placeorder= values[1].split("=");
//                    hold = values[2].split("=");
//                    recedMaterial = values[3].split("=");
//                    pendinHandOver = values[4].split("=");
//                    recdfromtailor = values[5].split("=");
//                    dispatch = values[6].split("=");
//
////                    for (int i = 0; i < values.length; i++) {
////                        dataitem = values[i].split("=");
////                        sendParam = dataitem[1];
////                    }
//                    intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    intent = intent.putExtra("placeorder",sendParam);
//                    intent = intent.putExtra("dispatch",dispatch);
//                    intent = intent.putExtra("hold",hold);
//                    intent = intent.putExtra("material",recedMaterial);
//                    intent = intent.putExtra("tailor",recdfromtailor);
//                    intent = intent.putExtra("material",recedMaterial);
//                    startActivity(intent);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

}
