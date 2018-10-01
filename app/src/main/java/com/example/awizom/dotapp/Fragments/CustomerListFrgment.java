package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.ElightBottomModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomerListFrgment extends Fragment{
    private  TextView orderPlace,receiVed,handOver,receivedTailor,receivedBy,mTextMessage;
    private Intent intent;
    ProgressDialog progressDialog;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;
    ElightBottomModel morder;
    OrderItemAdapter adapter;
    private RecyclerView recyclerView;
    private String roomName,orderID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
    }

    private void getFunctioncall() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new detailsGET().execute(roomName,orderID);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }
    private class detailsGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String roomName = strings[0];
            String orderID = strings[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/"+orderID.trim()+"/"+roomName.trim());
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;

        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CatelogOrderDetailModel>>() {
                }.getType();
                orderList = new Gson().fromJson(result, listType);
                adapter = new OrderItemAdapter(getActivity(), orderList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
    }
}