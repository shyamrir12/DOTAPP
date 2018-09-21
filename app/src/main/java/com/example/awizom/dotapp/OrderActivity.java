package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.DataOrder;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressDialog progressDialog ;

    List<DataOrder> orderList;
    OrderAdapter adapter;
    Button addorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setTitle("Order");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addorder=findViewById(R.id.addorder);
        progressDialog = new ProgressDialog(this);
        getMyOrder();
    }
    public void getMyOrder()
    {
        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new OrderActivity.GETOrderList().execute();

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
                builder.url(AppConfig.BASE_URL+"GetOrder");
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
