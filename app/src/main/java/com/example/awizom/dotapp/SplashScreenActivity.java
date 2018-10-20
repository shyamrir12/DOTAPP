package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.awizom.dotapp.Helper.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

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
              //     Toast.makeText(getApplicationContext(), "internet is available", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(SplashScreenActivity.this, SigninActivity.class);

                    startActivity(i);
                    finish();
                } else  {
//                    Toast.makeText(getApplicationContext(), "internet is not available", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(SplashScreenActivity.this);
                    alertbox.setIcon(R.drawable.warning);
                    alertbox.setTitle("Internet Connection Is Not Available");
                    alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                         //   startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                           // Intent intent = new Intent(Intent.ACTION_MAIN);
                         //   intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                         //   startActivity(intent);

                            // finish used for destroyed activity
                          finishAffinity();
                           System.exit(0);

                        }
                    });

             //      alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
         //               public void onClick(DialogInterface arg0, int arg1) {
                            // Nothing will be happened when clicked on no button
                            // of Dialog
             //           }
          //          });

                    alertbox.show();

                  //  System.out.print("internet is not available");
                }





            }
        }, SPLASH_TIME_OUT);
    }


}
