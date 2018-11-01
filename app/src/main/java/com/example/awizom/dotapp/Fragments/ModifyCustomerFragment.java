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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ModifyCustomerFragment extends Fragment implements View.OnClickListener {
    private EditText cContact, cAddress, interioName, interioContact;
    private AutoCompleteTextView cName;
    private Button updateCustomer;
    private Intent intent;
    private ProgressDialog progressDialog;
    private List<CustomerModel> customerlist;
    private String[] customerNameList;
    ArrayAdapter<String> adapter;
    private CustomerModel dataOrderValue;
    private long cid = 0;

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


        cName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cContact.setText("");
                cAddress.setText("");
                interioName.setText("");
                interioContact.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cName.getText().length() > 0)
                    getCustomerName(cName.getText().toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateButton:

                if ((cName.getText().toString().isEmpty()) || (cContact.getText().toString().isEmpty())) {
                    cName.setError("Customer Name is required!");
                    cContact.setError("Customer Contact is required!");
                }else {
                    customerUpdatePost();
                }
                break;

        }
    }


    private void getCustomerDetail() {
        try {
            for (CustomerModel cm : customerlist) {
                if (cm.getCustomerName().equals(cName)) {
                    cContact.setText(cm.getMobile());
                    cAddress.setText(cm.getAddress());
                    interioName.setText(cm.getInteriorName());
                    interioContact.setText(cm.getInteriorMobile());
                }
            }
        } catch (Exception e) {

        }

    }

    private void customerUpdatePost() {

        String name = cName.getText().toString().trim();
        String contact = cContact.getText().toString().trim();
        String address = cAddress.getText().toString().trim();
        String intename = interioName.getText().toString().trim();
        String intecontact = interioContact.getText().toString().trim();

        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTCustomer().execute(name, address, contact, intename, intecontact, SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();

            // System.out.println("Error: " + e);
        }
    }

    private class POSTCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String customername = params[0];
            String address = params[1];
            String mobile = params[2];
            String interiorname = params[3];
            String interiormobile = params[4];
            String accesstoken = params[5];

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
                startActivity(intent = new Intent(getActivity(), CustomerActivity.class));
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getActivity(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getCustomerDetailList();
                }
                progressDialog.dismiss();
            }
        }


    }


    private void getCustomerDetailList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new getCustomerList().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class getCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String accesstoken = strings[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerGet/");
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
                Type listType = new TypeToken<List<CustomerModel>>() {
                }.getType();
                customerlist = new Gson().fromJson(result, listType);
                customerNameList = new String[customerlist.size()];
                for (int i = 0; i < customerlist.size(); i++) {
                    customerNameList[i] = String.valueOf(customerlist.get(i).getCustomerName());
                }

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, customerNameList);
                cName.setThreshold(1);//will start working from first character
                cName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                //Getting the instance of AutoCompleteTextView
                progressDialog.dismiss();


            }
        }
    }


    private void getCustomerName(String cusname) {

        try {

            new getCustomer().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token, cusname);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<CustomerModel>() {
                }.getType();
                dataOrderValue = new Gson().fromJson(result, listType);
                if (dataOrderValue != null) {
                    cid = dataOrderValue.getCustomerID();
                    cContact.setText(dataOrderValue.getMobile());
                    cAddress.setText(dataOrderValue.getAddress());
                    interioName.setText(dataOrderValue.getInteriorName());
                    interioContact.setText(dataOrderValue.getInteriorMobile());
                }
            }
        }
    }

}
