package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.Token;
import com.example.awizom.dotapp.Models.UserRegister;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SinUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signupButton;
    private EditText userName, passWord, cnfrmPassWord;
    private TextView loginHere;
    private Intent intent;
    private ProgressDialog progressDialog;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_layout);
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(SinUpActivity.this);
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


    private void initView() {
        getSupportActionBar().setTitle("Sign Up");
        userName = findViewById(R.id.customerName);
        passWord = findViewById(R.id.password);
        cnfrmPassWord = findViewById(R.id.confrmPassword);
        spinner = findViewById(R.id.spinnerUserRole);
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
        loginHere = findViewById(R.id.loginHere);
        loginHere.setOnClickListener(this);
        String userrole[] = {"User", "Advance", "PlaceOrder", "Hold", "MaterialReceive", "HandOver", "Receive", "Dispatch"};


        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userrole);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupButton:
                if (validation()) {
                    createUser();
                }
//                if ((userName.getText().toString().isEmpty()) || (passWord.getText().toString().isEmpty()) ||
//                (cnfrmPassWord.getText().toString().isEmpty()) ||  (!cnfrmPassWord.getText().toString().equals(passWord.getText().toString()))  ) {
//                    userName.setError("User name is required!"); passWord.setError("password is required!");
//                    cnfrmPassWord.setError("password is required!"); cnfrmPassWord.setError("password is not match!");
//                }else {
//                    createUser();
//
//                }
                break;
            case R.id.loginHere:
                startActivity(intent = new Intent(this, SigninActivity.class));
                break;
        }
    }

    private boolean validation() {


        if ((userName.getText().toString().isEmpty())) {
            userName.setError("User name is required!");
        } else if (passWord.getText().toString().isEmpty()) {
            passWord.setError("password is required!");
        } else if (cnfrmPassWord.getText().toString().isEmpty()) {
            cnfrmPassWord.setError("password is required!");
        } else if (!cnfrmPassWord.getText().toString().equals(passWord.getText().toString())) {
            cnfrmPassWord.setError("password is not match!");
        }
        return true;

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }


    private void createUser() {
        String name = userName.getText().toString().trim();
        String pwd = passWord.getText().toString().trim();
        String cpwd = cnfrmPassWord.getText().toString().trim();
        String ur = "User";
        if (spinner != null)
            ur = spinner.getSelectedItem().toString().trim();
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTRegister().execute(name, pwd, cpwd, ur);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class POSTRegister extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String customername = params[0];
            String password = params[1];
            String cnfrmpassword = params[2];
            String userrole = params[3];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "register");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("UserName", customername);
                parameters.add("Password", password);
                parameters.add("ConfirmPassword", cnfrmpassword);
                parameters.add("Role", userrole);
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(SinUpActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {


                if (result.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(SinUpActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    UserRegister.RootObject jsonbody = gson.fromJson(result, UserRegister.RootObject.class);
                    if (jsonbody.isStatus()) {
                        Token user = new Token();
                        user.userRole = jsonbody.Role;
                        user.access_token = jsonbody.login.access_token;
                        user.userName = jsonbody.login.userName;
                        user.token_type = jsonbody.login.token_type;
                        user.expires_in = jsonbody.login.expires_in;

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        if (!SharedPrefManager.getInstance(SinUpActivity.this).getUser().access_token.equals(null)) {
                            startActivity(intent = new Intent(SinUpActivity.this, HomeActivityUser.class));
                        }
                    } else {
                        Toast.makeText(SinUpActivity.this, jsonbody.dataIdentityResult.getErrors().get(0), Toast.LENGTH_SHORT).show();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

