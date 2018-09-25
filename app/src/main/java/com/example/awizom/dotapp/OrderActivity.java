package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.DataOrder;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ProgressDialog progressDialog ;

    List<DataOrder> orderList;
    OrderAdapter adapter;
    Button addorder;

    private Toolbar toolbar;private TextView textView; private ImageButton arrow_id_back;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        textView = findViewById(R.id.activity_id_name);
        textView.setText("Order List");
        arrow_id_back = findViewById(R.id.arrow_id_back);
        arrow_id_back.setOnClickListener(this);
        arrow_id_back.setVisibility(View.INVISIBLE);

       // getSupportActionBar().setTitle("Order");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addorder=findViewById(R.id.addorder);
        progressDialog = new ProgressDialog(this);
        getMyOrder();

        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDeleteDialog();
            }
        });
    }
    private void showUpdateDeleteDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customer_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText editCustomerName = (EditText) dialogView.findViewById(R.id.editCustomerName);
        final EditText editAddress = (EditText) dialogView.findViewById(R.id.editAddress);
        final EditText editMobile = (EditText) dialogView.findViewById(R.id.editMobile);
        final EditText editInteriorName = (EditText) dialogView.findViewById(R.id.editInteriorName);
        final EditText editInteriorMobile = (EditText) dialogView.findViewById(R.id.editInteriorMobile);

        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddOrder);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Order");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CustomerName = editCustomerName.getText().toString().trim();
                String Address = editAddress.getText().toString().trim();
                String Mobile = editMobile.getText().toString().trim();
                String InteriorName = editInteriorName.getText().toString().trim();
                String InteriorMobile = editInteriorMobile.getText().toString().trim();
               try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new OrderActivity.POSTOrder().execute(CustomerName,Address,Mobile,InteriorName,InteriorMobile);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
                }
                    b.dismiss();

            }


        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */

            }
        });
    }
    public void saveOrder() {
        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();

           // String commenttext = editTextComment.getText().toString();
            new OrderActivity.POSTOrder().execute();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_id_back:
                //startActivity(intent = new Intent(this,));
                break;
        }
    }

    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String customername = params[0];
            String address = params[1];
            String mobile = params[2];
            String interiorname = params[3];
            String interiormobile = params[4];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"CustomerPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CustomerID", "0");
                parameters.add("CustomerName", customername);
                parameters.add("Address", address);
                parameters.add("Mobile", mobile);
                parameters.add("InteriorName", interiorname);
                parameters.add("InteriorMobile", interiormobile);
                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getMyOrder();

                }
                progressDialog.dismiss();

            }


        }

    }

    public void getMyOrder()
    {
        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new OrderActivity.GETOrderList().execute("test");

            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class GETOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String accesstoken = params[0];
            //String clave = params[1];
            //String res = params[2];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
              //  builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                //System.out.println(result);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DataOrder>>(){}.getType();
                orderList = new Gson().fromJson(result,listType);
                adapter = new OrderAdapter(OrderActivity.this, orderList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

            }


        }
    }
}
