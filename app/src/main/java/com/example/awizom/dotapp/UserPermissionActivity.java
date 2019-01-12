package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.PermissionList;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.UserPermissionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class UserPermissionActivity extends AppCompatActivity {

    private  ToggleButton advUser,holdUser,placeUser,materialUser,handOverUser,receiveUser,dispatchUser,
                            orderCreateUserBtn,partyUserBtn,searchUserBtn,printUserBtn;
    private TextView textViewAdvUser,textplaceOrderUser,txtholdaUser,txtMaterialReceiveLayertoggBtn,txthandoverUser,txtreceiverUser,
            txtdispatchUser,txtOrdercreateUser,txtPartyUser,txtSearchUser,txtPrintUser;
    private ProgressDialog progressDialog;
    private String userID="",permissionName="",status = "";
    private UserPermissionModel userPermissionModel;
    private List<PermissionList> permissionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_permission_layout);
        initView();
    }

    private void initView() {
       advUser = findViewById(R.id.advtoggBtn);
        holdUser = findViewById(R.id.HoldtoggBtn);
        placeUser = findViewById(R.id.PlaceOrderLayertoggBtn);
        materialUser = findViewById(R.id.MaterialReceiveLayertoggBtn);
        handOverUser = findViewById(R.id.HandOverLayertoggBtn);
        receiveUser = findViewById(R.id.ReceivetoggBtn);
        dispatchUser = findViewById(R.id.DispatchLayertoggBtn);

       printUserBtn = findViewById(R.id.PrintLayertoggBtn);
        searchUserBtn = findViewById(R.id.SearchLayertoggBtn);
        partyUserBtn = findViewById(R.id.PartyLayertoggBtn);
        orderCreateUserBtn  = findViewById(R.id.OrderCreateLayertoggBtn);

        textViewAdvUser = findViewById(R.id.advanceUser);
        textplaceOrderUser = findViewById(R.id.placeOrderUser);
        txtholdaUser = findViewById(R.id.holdaUser);
        txtMaterialReceiveLayertoggBtn = findViewById(R.id.MaterialReceiveLayertoggBtn);
        txthandoverUser = findViewById(R.id.handoverUser);
        txtreceiverUser =findViewById(R.id.receiverUser);
        txtdispatchUser = findViewById(R.id.dispatchUser);

        txtPrintUser = findViewById(R.id.PrintUser);
        txtSearchUser = findViewById(R.id.OrderCreateUser);
        txtPartyUser =findViewById(R.id.PartyUser);
        txtOrdercreateUser = findViewById(R.id.OrderCreateUser);



        userID = getIntent().getExtras().getString("UserId","");
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        userPermissionGet();


        advUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                if (isChecked == true) {
                    status = String.valueOf(isChecked);
                    permissionName = textViewAdvUser.getText().toString();
                    userPermissionPost();

                } else if(isChecked == false){
                    status = String.valueOf(isChecked);
                    permissionName = textViewAdvUser.getText().toString();
                    userPermissionPost();
                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });
        placeUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (isChecked == true) {
                                    status = String.valueOf(isChecked);
                                    permissionName = textplaceOrderUser.getText().toString();
                                    userPermissionPost();
                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = textplaceOrderUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }



        });

        holdUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                            if(isChecked == true){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtholdaUser.getText().toString();
                                    userPermissionPost();
                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtholdaUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });



        materialUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {


                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if(isChecked == true){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtMaterialReceiveLayertoggBtn.getText().toString();
                                    userPermissionPost();
                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtMaterialReceiveLayertoggBtn.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();

            }
        });

        handOverUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if(isChecked == true){
                                    status = String.valueOf(isChecked);
                                    permissionName = txthandoverUser.getText().toString();
                                    userPermissionPost();
                                } else  if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txthandoverUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();

            }
        });

        receiveUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                if(isChecked == true){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtreceiverUser.getText().toString();
                                    userPermissionPost();
                                } else  if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtreceiverUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();



            }
        });
        dispatchUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                if(isChecked == true){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtdispatchUser.getText().toString();
                                    userPermissionPost();
                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtdispatchUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });
        orderCreateUserBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (isChecked == true) {
                                    status = String.valueOf(isChecked);
                                    permissionName = txtOrdercreateUser.getText().toString();
                                    userPermissionPost();

                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtOrdercreateUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });
        partyUserBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (isChecked == true) {
                                    status = String.valueOf(isChecked);
                                    permissionName = txtPartyUser.getText().toString();
                                    userPermissionPost();

                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtPartyUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });
        searchUserBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (isChecked == true) {
                                    status = String.valueOf(isChecked);
                                    permissionName = txtSearchUser.getText().toString();
                                    userPermissionPost();

                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtSearchUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });
        printUserBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermissionActivity.this);
                alertbox.setMessage("are you sure want to give the permission");
                alertbox.setTitle("Change Permission");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                                if (isChecked == true) {
                                    status = String.valueOf(isChecked);
                                    permissionName = txtPrintUser.getText().toString();
                                    userPermissionPost();

                                } else if(isChecked == false){
                                    status = String.valueOf(isChecked);
                                    permissionName = txtPrintUser.getText().toString();
                                    userPermissionPost();
                                }
                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();


            }
        });

    }

    private void userPermissionGet() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new userPermissionGetDetail().execute(userID,SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            progressDialog.dismiss();
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            }else {
                Gson gson = new Gson();
                Type listType = new TypeToken<UserPermissionModel>() {
                }.getType();
                userPermissionModel = new Gson().fromJson(result, listType);
                if(userPermissionModel != null){

                    permissionList = userPermissionModel.getPermissionList();

                    for(int i=0; i<permissionList.size(); i++){
                        if(permissionList.get(i).getPermissionName().equals("Advance")){
                            advUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("PlaceOrder")) {
                            placeUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Hold")) {
                            holdUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("MaterialReceive")) {
                            materialUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("HandOver")) {
                            handOverUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Receive")) {
                            receiveUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Dispatch")) {
                            dispatchUser.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("OrderCreate")) {
                            orderCreateUserBtn.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Party")) {
                            printUserBtn.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Search")) {
                            searchUserBtn.setText("ON");
                        }if(permissionList.get(i).getPermissionName().equals("Print")) {
                            printUserBtn.setText("ON");
                        }
                    }
                }
                progressDialog.dismiss();
            }
        }
    }


    private void userPermissionPost() {


        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new userPermissionPostDetail().execute(userID,permissionName,status,SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();

        }
    }
    private class userPermissionPostDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String userID = params[0];
            String permission = params[1];
            String status = params[2];
            String accesstoken = params[3];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "UserPermissionPost/" + userID +"/"+ permission +"/"+status);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
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
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
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
