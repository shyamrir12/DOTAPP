package com.example.awizom.dotapp.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderAdapter;
import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.OrderActivity;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView customerName, customerMobileNo, customerSno, customerOrder, customerDate, customerhall;
    private ImageButton additionButton;
    private TextView elight, roman, aPlat;
    private RecyclerView recyclerView;

    ProgressDialog progressDialog;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;

    OrderItemAdapter adapter;
    private Intent intent;
    private EditText s_no, catlogName, design, pageNo, price, price2, materialType, qty, aQty;
    private Button addButton, cancelButton;
    private AlertDialog b;
    private String id,hallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details);
        initView();
    }

    private void initView() {

//        id=getIntent().getExtras().getString("id","");
//        hallName=getIntent().getExtras().getString("hall","");

        customerName = findViewById(R.id.customer_name);
        customerMobileNo = findViewById(R.id.customer_mobile_no);
        customerSno = findViewById(R.id.s_no);
        customerOrder = findViewById(R.id.order);
        customerDate = findViewById(R.id.date);
        customerhall = findViewById(R.id.room_type);

        elight = findViewById(R.id.elight);
        roman = findViewById(R.id.roman);
        aPlat = findViewById(R.id.aPlat);
        additionButton = findViewById(R.id.addButton);

        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        functionalityCall();

        catelogOrderDetailModel = new CatelogOrderDetailModel();

    }

    private void functionalityCall() {

        additionButton.setOnClickListener(this);
        getFunctioncall();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addButton:
                showDialog();
                //startActivity(intent = new Intent(this, AddOrderDialog.class));
                break;
            case R.id.cancelButton:
                b.dismiss();
                break;
            case R.id.add:
                addList();
                break;
        }
    }

    private void addList() {
        String snumber = s_no.getText().toString();
        String catlogname = catlogName.getText().toString();
        String desiGn = design.getText().toString();
        String page_no = pageNo.getText().toString();
        String priCe = price.getText().toString();
        String priCe2 = price2.getText().toString();
        String qTy = qty.getText().toString();
        String aqty = aQty.getText().toString();
        String materialtype = materialType.getText().toString();

        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrder().execute(snumber,catlogname,desiGn,page_no,priCe,priCe2,qTy,aqty,materialtype);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
        b.dismiss();

    }

    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String snumber = params[0];
            String catlogname = params[1];
            String desiGn = params[2];
            String page_no = params[3];
            String priCe = params[4];
            String priCe2 = params[5];
            String qTy = params[6];
            String aqty = params[7];
            String materialtype = params[8];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderItemPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("SerialNo", snumber);
                parameters.add("CatalogName", catlogname);
                parameters.add("Design", desiGn);
                parameters.add("PageNo", page_no);
                parameters.add("Price", priCe);
                parameters.add("Price2", priCe2);
                parameters.add("Qty", qTy);
                parameters.add("AQty", aqty);
                parameters.add("MaterialType",materialtype);
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
//                    getMyOrder();

                }
                progressDialog.dismiss();
            }
        }
    }

//    public void getMyOrder() {
//        try {
//            //String res="";
//            progressDialog.setMessage("loading...");
//            progressDialog.show();
//            new RoomDetailsActivity().GETOrderList().execute("test");
//
//            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            progressDialog.dismiss();
//            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            // System.out.println("Error: " + e);
//        }
//    }


    private void showDialog() {
        initViewByAlertdailog();

    }

    private void initViewByAlertdailog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dailog_layout, null);
        dialogBuilder.setView(dialogView);

        s_no = dialogView.findViewById(R.id.sNo);
        catlogName = dialogView.findViewById(R.id.catlogName);
        design = dialogView.findViewById(R.id.design);
        pageNo = dialogView.findViewById(R.id.pageNo);
        price = dialogView.findViewById(R.id.price);
        price2 = dialogView.findViewById(R.id.price2);
        materialType = dialogView.findViewById(R.id.materialType);
        qty = dialogView.findViewById(R.id.qTy);
        aQty = dialogView.findViewById(R.id.aQty);

        addButton = dialogView.findViewById(R.id.add);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        dialogBuilder.setTitle("Add Order");
        b = dialogBuilder.create();
        b.show();

        buttonClick();
    }

    private void buttonClick() {

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }


    private void getFunctioncall() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new detailsGET().execute("test");

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class detailsGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/1/Hall");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

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
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                //System.out.println(result);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CatelogOrderDetailModel>>() {
                }.getType();
                orderList = new Gson().fromJson(result, listType);

                adapter = new OrderItemAdapter(getBaseContext(), orderList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

            }


        }
    }
}
