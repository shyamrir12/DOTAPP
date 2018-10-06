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

import com.example.awizom.dotapp.AfterCreateOrderActivity;
import com.example.awizom.dotapp.Config.AppConfig;
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

public class OrderCreateFragment extends Fragment implements View.OnClickListener {

    private EditText  customerDate, customerAdvance;
    private Button addOrder;
    private Intent intent;
    private ProgressDialog progressDialog;
    int morderid=0;
    private List<DataOrder> orderList;
    int position=0;
    private List<CustomerModel> customerlist;
    private String[] customerNameList;
    ArrayAdapter<String> adapter;
    private AutoCompleteTextView customerName;
    String cm_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_create_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        customerName = view.findViewById(R.id.customerName);
        customerDate = view.findViewById(R.id.contact);
        customerAdvance = view.findViewById(R.id.password);
        addOrder = view.findViewById(R.id.addOrder);
        addOrder.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());


        customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerDate.setText("");
                customerAdvance.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customerName.getText().length() > 0)
                    getCustomerDetail(customerName.getText().toString());
            }
        });
        getCustomerDetailList();

    }

    private void getCustomerDetail(String cusname) {
        try {

            for (CustomerModel cm : customerlist) {
                if (cm.getCustomerName().equals(cusname)) {
                    customerDate.setText(cm.getMobile());
                    customerAdvance.setText(cm.getAddress());
                    cm_id = String.valueOf(cm.getCustomerID());
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addOrder:
             addOrderPost();
                break;
        }
    }

    private void addOrderPost() {
        String c_name = customerName.getText().toString();



        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrderCreate().execute(c_name, cm_id);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }

    }
    private class POSTOrderCreate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String customerid = params[0];
            String json = "";
            try {


                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", "0");
                parameters.add("CustomerID", cm_id);


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
                Toast.makeText(getActivity(), "Invalid Request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getActivity(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(intent = new Intent(getActivity(), AfterCreateOrderActivity.class));
                if (jsonbodyres.getStatus() == true) { }
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
                customerName.setThreshold(1);//will start working from first character
                customerName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                //Getting the instance of AutoCompleteTextView
                progressDialog.dismiss();
            }


        }
    }


}
