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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Token;
import com.example.awizom.dotapp.Models.UserLogin;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signinButton;
    private EditText userName, passWord;
    private TextView signupHere;
    private Intent intent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initView();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(SigninActivity.this);
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

    private void initView() {
        getSupportActionBar().setTitle("Sign In");
        progressDialog = new ProgressDialog(this);
        userName = findViewById(R.id.userId);
        passWord = findViewById(R.id.password);
        signinButton = findViewById(R.id.signinButton);
        signinButton.setOnClickListener(this);
        signupHere = findViewById(R.id.signupHere);
        signupHere.setOnClickListener(this);
       try {
           if (!SharedPrefManager.getInstance(SigninActivity.this).getUser().access_token.equals(null)) {
               Intent log = new Intent(getApplicationContext(), HomeActivity.class);
               startActivity(log);
               finish();
            }
       }catch (Exception e){
            e.printStackTrace();
       }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinButton:
                if (validation()) {
                    userLogin();
                    // startActivity(intent = new Intent(this,HomeActivity.class));
                }
                break;
            case R.id.signupHere:
                startActivity(intent = new Intent(this, SinUpActivity.class));
                break;
        }
    }

    private boolean validation() {
        if (userName.getText().toString().isEmpty() && passWord.getText().toString().isEmpty()) {
            Toast.makeText(this, "Filed can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }else if( !isValidPassword(passWord.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Password incorrect",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})").matcher(password);
        return matcher.matches();
    }

    public void userLogin() {

        progressDialog.setMessage("loading...");
        progressDialog.show();

        try {
            //String res="";

            new GetLogin().execute(userName.getText().toString(), passWord.getText().toString());


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();

        }


    }

    private class GetLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream = null;
            String cliente = params[0];
            String clave = params[1];
            //String res = params[2];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "UserLoginGet/" + cliente + "/" + clave);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

               /* FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("grant_type", "password");
                parameters.add("username", cliente);
                parameters.add("password", clave);
                builder.post(parameters.build());*/

                Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                    //System.out.println(json);


                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Error: " + e);
//                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }


            return json;
        }

        protected void onPostExecute(String result) {
           try{
            if (result.isEmpty()) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Invalid user id or password", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);

                if (jsonbody.isStatus()) {
                    Token user = new Token();
                    user.userRole = jsonbody.Role;
                    user.access_token = jsonbody.login.access_token;
                    user.userName = jsonbody.login.userName;
                    user.token_type = jsonbody.login.token_type;
                    user.expires_in = jsonbody.login.expires_in;

                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    if (!SharedPrefManager.getInstance(SigninActivity.this).getUser().access_token.equals(null)) {

                        if(user.getUserRole().contains("Admin")) {
                            Intent log = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(log);

                        }
                        else {

                            Intent log = new Intent(getApplicationContext(), HomeActivityUser.class);
                            startActivity(log);


                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), jsonbody.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
           }catch (Exception e){
               e.printStackTrace();
           }

        }
    }

}
