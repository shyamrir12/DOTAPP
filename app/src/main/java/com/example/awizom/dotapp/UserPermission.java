package com.example.awizom.dotapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
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

public class UserPermission extends AppCompatActivity implements View.OnClickListener {


    private Button advUser,holdUser,placeUser,materialUser,handOverUser,receiveUser,dispatchUser,
            orderCreateUserBtn,partyUserBtn,searchUserBtn,printUserBtn;
    private TextView textViewAdvUser,textplaceOrderUser,txtholdaUser,txtMaterialReceiveLayertoggBtn,txthandoverUser,txtreceiverUser,
            txtdispatchUser,txtOrdercreateUser,txtPartyUser,txtSearchUser,txtPrintUser;
    private ProgressDialog progressDialog;
    private String userID="",permissionName="",status = "False";
    private UserPermissionModel userPermissionModel;
    private List<PermissionList> permissionList;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpermission);
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
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        advUser.setOnClickListener(this);
        holdUser.setOnClickListener(this);
        placeUser.setOnClickListener(this);
        materialUser.setOnClickListener(this);
        handOverUser.setOnClickListener(this);
        receiveUser.setOnClickListener(this);
        dispatchUser.setOnClickListener(this);
        printUserBtn.setOnClickListener(this);
        searchUserBtn.setOnClickListener(this);
        partyUserBtn.setOnClickListener(this);
        orderCreateUserBtn.setOnClickListener(this);


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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                userPermissionGet();
            }
        });


    }


    private void userPermissionGet() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new userPermissionGetDetail().execute(userID, SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == advUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            if (advUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = textViewAdvUser.getText().toString();
                                userPermissionPost();
                            }else if(advUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = textViewAdvUser.getText().toString();
                                userPermissionPost();
                            }



                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == placeUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            if (placeUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = textplaceOrderUser.getText().toString();
                                userPermissionPost();
                            }else if(placeUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = textplaceOrderUser.getText().toString();
                                userPermissionPost();
                            }



                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == holdUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (holdUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtholdaUser.getText().toString();
                                userPermissionPost();
                            }else if(holdUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtholdaUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == materialUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (materialUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtMaterialReceiveLayertoggBtn.getText().toString();
                                userPermissionPost();
                            }else if(materialUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtMaterialReceiveLayertoggBtn.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == handOverUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (handOverUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = txthandoverUser.getText().toString();
                                userPermissionPost();
                            }else if(handOverUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txthandoverUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == receiveUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (receiveUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtreceiverUser.getText().toString();
                                userPermissionPost();
                            }else if(receiveUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtreceiverUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == dispatchUser.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (dispatchUser.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtdispatchUser.getText().toString();
                                userPermissionPost();
                            }else if(dispatchUser.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtdispatchUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == orderCreateUserBtn.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (orderCreateUserBtn.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtOrdercreateUser.getText().toString();
                                userPermissionPost();
                            }else if(orderCreateUserBtn.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtOrdercreateUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == partyUserBtn.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                            if (partyUserBtn.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtPartyUser.getText().toString();
                                userPermissionPost();
                            }else if(partyUserBtn.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtPartyUser.getText().toString();
                                userPermissionPost();
                            }

                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == searchUserBtn.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {



                            if (searchUserBtn.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtSearchUser.getText().toString();
                                userPermissionPost();
                            }else if(searchUserBtn.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtSearchUser.getText().toString();
                                userPermissionPost();
                            }


                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }if(v.getId() == printUserBtn.getId()){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(UserPermission.this);
            alertbox.setMessage("are you sure want to change the permission");
            alertbox.setTitle("Change Permission");
            alertbox.setIcon(R.drawable.admin);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            if (printUserBtn.getText().equals("Give")) {
                                status = "True";
                                permissionName = txtPrintUser.getText().toString();
                                userPermissionPost();
                            }else if(printUserBtn.getText().equals("Remove")) {
                                status = "False";
                                permissionName = txtPrintUser.getText().toString();
                                userPermissionPost();
                            }


                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
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
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                mSwipeRefreshLayout.setRefreshing(false);
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
                            advUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("PlaceOrder")) {
                            placeUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Hold")) {
                            holdUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("MaterialReceive")) {
                            materialUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("HandOver")) {
                            handOverUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Receive")) {
                            receiveUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Dispatch")) {
                            dispatchUser.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("OrderCreate")) {
                            orderCreateUserBtn.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Party")) {
                            printUserBtn.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Search")) {
                            searchUserBtn.setText("Remove");
                        }if(permissionList.get(i).getPermissionName().equals("Print")) {
                            printUserBtn.setText("Remove");
                        }
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }


    private void userPermissionPost() {


        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new userPermissionPostDetail().execute(userID,permissionName,status,SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
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
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            }else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }


}
