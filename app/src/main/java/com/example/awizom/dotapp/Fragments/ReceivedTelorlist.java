package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.CustomerListAdapter;
import com.example.awizom.dotapp.Adapters.HandOverAdapter;
import com.example.awizom.dotapp.Adapters.TelorListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.TelorModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ReceivedTelorlist extends Fragment {

    ProgressDialog progressDialog;
    ListView lv;
    ImageButton img;
    RecyclerView lv1;
    // List <TelorModel> list1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Handler handler = new Handler();
    Runnable refresh;
    private Button add, cancel;
    private EditText t_name,old_t_name;
    ArrayAdapter<String> telorListAapter;
    String[] telorlist;
    private String telornamet,telorname_old,hTelor;
    List<HandOverModel> list1;
    HandOverAdapter adapterh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telor_list_item, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while loading telors");
        lv = view.findViewById(R.id.telorList);
        lv1=view.findViewById(R.id.rcyclr);
        lv1.setHasFixedSize(true);
        lv1.setLayoutManager(new LinearLayoutManager(getActivity()));


        img = view.findViewById(R.id.updateButton);
        //     lv = view.findViewById(R.id.telorList);
        img.setVisibility(View.GONE);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getTelorList();
            }
        });
        getTelorList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                       lv.setVisibility(View.GONE);
                           hTelor=telorlist[position];
                      //     Toast.makeText(getActivity(), telorlist[position], Toast.LENGTH_SHORT).show();

                           getreceivedTelorList();
            }
        });
    }

    private void getreceivedTelorList() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GetReceivedTelorDetails().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token,hTelor);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class GetReceivedTelorDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {



            String accesstoken = params[0];
            String telorname = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "ReceivedItemlistGet/"+ telorname );
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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<HandOverModel>>() {
                }.getType();
                list1 = new Gson().fromJson(result, listType);
                adapterh = new HandOverAdapter(getContext(), list1);
                lv1.setAdapter(adapterh);

                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }


    }


    private void postTelorList() {
        telornamet=t_name.getText().toString();

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostTelorDetails().execute(telornamet.trim(),SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class PostTelorDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String telorname = params[0];
            String accesstoken = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "TelorPost/" + telorname + "/" + "blank");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getContext()
                        , jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    // modifyItem(pos,um);

                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
            }


        }


    }


    private void getTelorList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GetTelorDetails().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private class GetTelorDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "ReceivedTelorlistGet");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Length", "0");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

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
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                telorlist = new Gson().fromJson(result, listType);

                telorListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_button_telorlist, R.id.label, telorlist);
                lv.setAdapter(telorListAapter);

                progressDialog.dismiss();
            }

        }
    }
}