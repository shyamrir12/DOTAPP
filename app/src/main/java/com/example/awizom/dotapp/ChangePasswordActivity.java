package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText oldpassword,newPassword,cnfrmPassword;
    private Button changePasswordButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        initView();
    }
    private void initView() {

        oldpassword = findViewById(R.id.oldPass);
        newPassword = findViewById(R.id.newPass);
        cnfrmPassword = findViewById(R.id.confrmPassword);
        changePasswordButton = findViewById(R.id.changePassBtn);
        changePasswordButton.setOnClickListener(this);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.changePassBtn:
                android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("Do you want to change the status");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        changePasswordMethodCall();

                    }
                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                alertbox.show();

                break;
        }
    }

    private void changePasswordMethodCall() {

        String oldPwd = oldpassword.getText().toString().trim();
        String newPwd = newPassword.getText().toString().trim();
        String cnPwd = cnfrmPassword.getText().toString().trim();
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new changePasswordPost().execute(oldPwd,newPwd,cnPwd,SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();

        }
    }

    private class changePasswordPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String oldPwd = params[0];
            String newPwd = params[1];
            String cnPwd = params[2];
            String accesstoken = params[3];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "ChangePassword");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OldPassword", oldPwd);
                parameters.add("NewPassword", newPwd);
                parameters.add("ConfirmPassword", cnPwd);
                builder.post(parameters.build());
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }
        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully password change", Toast.LENGTH_SHORT).show();
            }else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {

                }
                progressDialog.dismiss();
            }
        }
    }
}
