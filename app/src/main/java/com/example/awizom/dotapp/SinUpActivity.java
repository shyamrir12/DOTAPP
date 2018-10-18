package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_layout);
        initView();
    }
    @Override
    public void onBackPressed() {

    }

    private void initView() {
        getSupportActionBar().setTitle("Sign Up");
        userName = findViewById(R.id.customerName);
        passWord = findViewById(R.id.password);
        cnfrmPassWord = findViewById(R.id.confrmPassword);

        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
        loginHere = findViewById(R.id.loginHere);
        loginHere.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupButton:
                if (validation()) {
                    createUser();
                }
                break;
            case R.id.loginHere:
                startActivity(intent = new Intent(this, SigninActivity.class));
                break;
        }
    }

    private void createUser() {
        String name = userName.getText().toString().trim();
        String pwd = passWord.getText().toString().trim();
        String cpwd = cnfrmPassWord.getText().toString().trim();
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTRegister().execute(name, pwd, cpwd);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validation() {
        if (userName.getText().toString().isEmpty() && passWord.getText().toString().isEmpty() && cnfrmPassWord.getText().toString().isEmpty()) {
            Toast.makeText(this, "Filed can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }else if( !isValidPassword(passWord.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Password must contain mix of upper and lower case letters as well as digits and one special charecter(4-20)",Toast.LENGTH_SHORT);
            return false;
        }else if(passWord.getText().toString().contains(cnfrmPassWord.getText().toString())){
            Toast.makeText(getApplicationContext(),"Password not match",Toast.LENGTH_SHORT);

            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})").matcher(password);
        return matcher.matches();
    }

    private class POSTRegister extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String customername = params[0];
            String password = params[1];
            String cnfrmpassword = params[2];

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
                parameters.add("Role", "User");
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
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(SinUpActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
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
                progressDialog.dismiss();
            }
        }
    }

}

