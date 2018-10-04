package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.ElightBottomModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ModifyCustomerFragment extends Fragment implements View.OnClickListener {
    private EditText cContact,cAddress,interioName,interioContact;
    private Spinner cName;
    private Button updateCustomer;
    private Intent intent;
    private ProgressDialog progressDialog ;
    private List<CustomerModel > customerlist;
    private ArrayList<String> customerNameList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_edit_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        cName = view.findViewById(R.id.customerName);
        cContact = view.findViewById(R.id.contact);
        cAddress = view.findViewById(R.id.password);
        interioName = view.findViewById(R.id.confrmPassword);
        interioContact = view.findViewById(R.id.interiormobile);
        updateCustomer = view.findViewById(R.id.updateButton);
        updateCustomer.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());

        getCustomerDetailList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.updateButton:
                customerUpdatePost();
                break;
        }
    }

    private void customerUpdatePost() {

        //String name = cName.getText().toString().trim();
        String contact = cContact.getText().toString().trim();
        String address = cAddress.getText().toString().trim();
        String intename = interioName.getText().toString().trim();
        String intecontact = interioContact.getText().toString().trim();

        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrder().execute(contact,address,intename,intecontact);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            postModifyCutomer();
            // System.out.println("Error: " + e);
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
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Invalid request",Toast.LENGTH_SHORT).show();
                startActivity(intent = new Intent(getActivity(), CustomerActivity.class));
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getActivity(),jsonbodyres.getMessage(),Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true){
                    startActivity(intent = new Intent(getActivity(), CustomerActivity.class));
                }
                progressDialog.dismiss();
            }
        }


    }


    private void getCustomerDetailList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new getCustomerList().execute();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }
    private class getCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerGet/");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            }catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CustomerModel>>() {
                }.getType();
                customerlist = new Gson().fromJson(result, listType);
                progressDialog.dismiss();
                customerNameList = new ArrayList<>();

                cName = new Spinner(getActivity(), customerNameList,)


                for (CustomerModel customer : customerlist) {

                            customerNameList.add(customer.getCustomerName());
                        }



//                cName.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        for (CustomerModel customer : customerlist) {
//
//                            if(customer.getCustomerName().contains(cName.getText().toString()))
//
//                            customerNameList.add(customer.getCustomerName());
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
            }
        }
    }
    private void postModifyCutomer() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new postUpdateCustomer().execute();
        }catch(Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class postUpdateCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerGet/");
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
            if (result.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                Type getType = new TypeToken<CustomerModel>(){}.getType();
                progressDialog.dismiss();
            }
        }


    }
}
