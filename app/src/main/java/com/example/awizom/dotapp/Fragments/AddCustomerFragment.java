package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SigninActivity;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AddCustomerFragment extends Fragment implements View.OnClickListener {

    private EditText cName, cContact, cAddress, interioName, interioContact;
    private Button addCustomer;
    private Intent intent;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_add_layout, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        cName = view.findViewById(R.id.customerName);
        cContact = view.findViewById(R.id.contact);
        cAddress = view.findViewById(R.id.password);
        interioName = view.findViewById(R.id.confrmPassword);
        interioContact = view.findViewById(R.id.interiormobile);

        addCustomer = view.findViewById(R.id.customerButton);
        addCustomer.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customerButton:
                customerAddPost();
                break;
        }
    }


    private void customerAddPost() {

        String name = cName.getText().toString().trim();
        String contact = cContact.getText().toString().trim();
        String address = cAddress.getText().toString().trim();
        String intename = interioName.getText().toString().trim();
        String intecontact = interioContact.getText().toString().trim();

        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrder().execute(name, contact, address, intename, intecontact, SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }

    }

    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String accesstoken = params[0];
            String customername = params[1];
            String address = params[2];
            String mobile = params[3];
            String interiorname = params[4];
            String interiormobile = params[5];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
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
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
                startActivity(intent = new Intent(getActivity(), CustomerListFrgment.class));
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getActivity(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    startActivity(intent = new Intent(getActivity(), CustomerActivity.class));
                }
                progressDialog.dismiss();
            }
        }
    }
}