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
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SinUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signupButton;
    private EditText userName,passWord,cnfrmPassWord;
    private TextView loginHere;
    private Intent intent;
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_layout);
        initView();
    }

    private void initView() {
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
        switch (v.getId()){
            case R.id.signupButton :
                if(validation()) {
                    createUser();
                }
                break;
            case R.id.loginHere :
                    startActivity(intent = new Intent(this,SinUpActivity.class));
                break;
        }
    }

    private void createUser(){
        String name = userName.getText().toString().trim();
        String pwd = passWord.getText().toString().trim();
        String cpwd = cnfrmPassWord.getText().toString().trim();
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrder().execute(name,pwd,cpwd);
        }
        catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validation() {
        if(userName.getText().toString().isEmpty() &&  passWord.getText().toString().isEmpty() &&  cnfrmPassWord.getText().toString().isEmpty()){
            Toast.makeText(this,"Filed can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }return true;
    }

    private class POSTOrder extends AsyncTask<String, Void, String> {
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
                builder.url(AppConfig.BASE_URL_API_REG+"register");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("username", customername);
                parameters.add("password", password);
                parameters.add("confirmpassword", cnfrmpassword);
                parameters.add("role", "user");
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
                Toast.makeText(SinUpActivity.this, "Invalid request",Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(SinUpActivity.this,jsonbodyres.getMessage(),Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    startActivity(intent = new Intent(SinUpActivity.this, HomeActivity.class));
                }progressDialog.dismiss();
            }
        }
    }

}

