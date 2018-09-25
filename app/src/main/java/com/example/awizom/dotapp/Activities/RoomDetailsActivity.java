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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

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
   // private Spinner unitSpinner;
    private Button addButton, cancelButton;
    private AlertDialog b;
    private String roomName,orderID,customernAME,mobileNumber,orderDate,advance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details);
        initView();
    }

    private void initView() {

        roomName=getIntent().getExtras().getString("RoomName","");
        orderID= String.valueOf(getIntent().getIntExtra("OrderID",0));
        customernAME=getIntent().getExtras().getString("CustomerName","");
        mobileNumber=getIntent().getExtras().getString("Mobile","");
        orderDate=getIntent().getExtras().getString("OrderDate","");
        advance= String.valueOf(getIntent().getDoubleExtra("Advance",0));

        customerName = findViewById(R.id.customer_name);
        customerMobileNo = findViewById(R.id.customer_mobile_no);
        customerOrder = findViewById(R.id.order_date);
        customerhall = findViewById(R.id.room_name);

        customerName.setText(customernAME);
        customerMobileNo.setText(mobileNumber);
        String date[] = orderDate.split("T",0);
        customerOrder.setText(date[0]);
        customerhall.setText(roomName);


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
       // String unIt =  unit.getText().toString();

//        unitSpinner.setOnItemClickListener(this);
//        List<String> list = new ArrayList<String>();
//        list.add("Mtr.");list.add("Role");list.add("Sqft");


        try {

            progressDialog.setMessage("loading...");
            progressDialog.show();
            new POSTOrder().execute("0",materialtype,priCe2,qTy,"0","mtr.","0",catlogname,snumber,desiGn,page_no,priCe,"mtr.","0",roomName,orderID);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
        b.dismiss();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
         //     InputStream inputStream
            String orderItemId = params[0];
            String materialtype = params[1];
            String priCe2 = params[2];
            String qTy = params[3];
            String aqty = params[4];
            String orderUnit = params[5];
            String orderRoomId = params[6];

            String catlogname = params[7];
            String snumber = params[8];
            String desiGn = params[9];
            String page_no = params[10];
            String priCe = params[11];
            String unit = params[12];
            String catalogID = params[13];
            String roomName = params[14];
            String orderID = params[15];




            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderItemPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderItemID", orderItemId);
                parameters.add("MaterialType",materialtype);
                parameters.add("Price2", priCe2);
                parameters.add("Qty", qTy);
                parameters.add("AQty", aqty);
                parameters.add("OrderUnit", orderUnit);
                parameters.add("OrderRoomID", orderRoomId);


                parameters.add("CatalogName", catlogname);
                parameters.add("SerialNo", snumber);
                parameters.add("Design", desiGn);
                parameters.add("PageNo", page_no);
                parameters.add("Price", priCe);

                parameters.add("Unit", unit);
                parameters.add("CatalogID", catalogID);

                parameters.add("RoomName",roomName.trim());
                parameters.add("OrderID",orderID.trim());



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
       // unitSpinner = dialogView.findViewById(R.id.unit);

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
            new detailsGET().execute(roomName,orderID);

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
