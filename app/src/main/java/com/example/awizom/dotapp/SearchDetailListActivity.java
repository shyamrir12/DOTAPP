package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.SearchListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class SearchDetailListActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private ProgressDialog progressDialog;
    private List<DataOrder> searchList;
    private DataOrder dataOrderValue;
    private RecyclerView recyclerView;
    private SearchListAdapter adapter;
    private AutoCompleteTextView searchItem;
    private Button go;
    private String statusName = "";
    private String orderId = "";
    private long cid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_detail_list);
        initView();
    }

    private void initView() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchItem = findViewById(R.id.searchItem);
        searchItem.setOnClickListener(this);
        go = findViewById(R.id.goButton);
        go.setOnClickListener(this);
        statusName = getIntent().getExtras().getString("StatusName", "");
        orderId = getIntent().getExtras().getString("OrderID", "");
        getSupportActionBar().setTitle(statusName);
        dataOrderValue = new DataOrder();


        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchItem.getText().length() == 0) {
                    cid = 0;
                    searchItem.setText("");
                } else {
                    getCustomerDetail(searchItem.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goButton:

                if (!searchItem.getText().toString().isEmpty()) {
                    getSearchList();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill the field", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void getSearchList() {
        try {

            new GetSearchDetails().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetSearchDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String accesstoken = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderSearchGet/" + searchItem.getText().toString());
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "There is no data available" +
                        "", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DataOrder>>() {
                }.getType();
                searchList = new Gson().fromJson(result, listType);
                adapter = new SearchListAdapter(getApplicationContext(), searchList, statusName);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void getCustomerDetail(String cusname) {

        try {
            new getCustomer().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token, cusname);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

    }

    private class getCustomer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            String accesstoken = strings[0];
            String cusname = strings[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerGet/" + cusname);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<DataOrder>() {
                }.getType();
                dataOrderValue = new Gson().fromJson(result, listType);
                if (dataOrderValue != null) {
                    cid = dataOrderValue.getCustomerID();
                }

            }


        }
    }


}