package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.CustomerListAdapter;
import com.example.awizom.dotapp.Adapters.OrderListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.OrderListFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NewOrderListActivity extends AppCompatActivity {
    private Intent intent;
    ProgressDialog progressDialog;
    List<DataOrder> orderList;
    RecyclerView recyclerView;
    OrderListAdapter adapter;

    String filterKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.panding_order_list );
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(getApplicationContext());
        //  mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(this);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                getOrderList();
//            }
//        });



        filterKey = getIntent().getExtras().getString( "FilterKey", "" );
        if (!filterKey.equals( "" ))
            getOrderList();



    }

    private void getOrderList() {
        try {

            progressDialog.setMessage("loading...");
            progressDialog.show();
            new NewOrderListActivity.GetOrderDetails().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetOrderDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String accesstoken = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();

               // String name= getArguments().getString("NAME_KEY").toString();

                builder.url(AppConfig.BASE_URL_API + "OrderDetailsByFilterGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                //Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DataOrder>>() {
                }.getType();
                orderList = new Gson().fromJson(result, listType);
                adapter = new OrderListAdapter(getApplicationContext(), orderList,filterKey);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }

}
