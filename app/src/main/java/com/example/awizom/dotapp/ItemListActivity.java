package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Catelog;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button  cancelElight;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;
    ArrayAdapter<String> catadapter;
    ArrayAdapter<String> designadapter;
    OrderItemAdapter adapter;
    private EditText s_no, pageNo, price, price2, qty, aQty;
    private AutoCompleteTextView catlogName, design;
    private Spinner unitSpinner, materialType;
    private Button addButton, cancelButton;
    String actualorder = "";
    private String  orderID;
    DataOrder orderitem;
    private String advance,StatusName,filterkey,buttonname,tailorList;


    // private Toolbar toolbar;private TextView textView; private ImageButton arrow_id_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        initView();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private void initView() {

        getSupportActionBar().setTitle("Order Item Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderID = getIntent().getExtras().getString("OrderID", "0");
        filterkey = getIntent().getExtras().getString("FilterKey", "");
        StatusName = getIntent().getExtras().getString("StatusName", "");
        buttonname = getIntent().getExtras().getString("ButtonName", "");
        tailorList = getIntent().getExtras().getString("TailorList", "");

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getFunctioncall();
            }
        });
        getFunctioncall();
        catelogOrderDetailModel = new CatelogOrderDetailModel();


    }

    private void allOkButtonRoomLevel() {

        try {

            //     progressDialog.setMessage("loading...");
            //      progressDialog.show();
            new allokButtonRoomLevel().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);

        } catch (Exception e) {

            e.printStackTrace();
            //       progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class allokButtonRoomLevel extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            orderitem = new DataOrder();
            catelogOrderDetailModel = new CatelogOrderDetailModel();
            String accesstoken = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", String.valueOf(orderitem.getOrderID()));
                parameters.add("RoomName", catelogOrderDetailModel.getRoomName());
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", "OrderPlaced");
                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //      progressDialog.dismiss();
//                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {}
                //       progressDialog.dismiss();
            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }
    }


    private void dilogShow() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_bottom_item, null);
        dialogBuilder.setView(dialogView);
        cancelElight = dialogView.findViewById(R.id.cancelElight);

        dialogBuilder.setTitle("Edit bottom");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        cancelElight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }


    private boolean validation() {

        boolean status=true;
        if ((catlogName.getText().toString().isEmpty())) {
            catlogName.setError("Catalog name is required!");
            status=false;
        } else if (design.getText().toString().isEmpty()) {
            design.setError("Design is required!");status=false;
        } else if (qty.getText().toString().isEmpty()) {
            qty.setError("Qty is required!");status=false;
        } else if ((price.getText().toString().isEmpty())) {
            price.setError("price is required!");status=false;
        }
        return status;

    }


    private void getFunctioncall() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            // progressDialog.setMessage("loading...");
            //  progressDialog.show();
            new detailsGET().execute( orderID, filterkey,SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
            //progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class detailsGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //String roomName = strings[0];
            String orderID = strings[0];
            String filterkey = strings[1];
            String accesstoken = strings[2];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/" + orderID.trim() + "/" +"blank" +"/" +filterkey);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;

        }

        protected void onPostExecute(String result) {
            try {

                if (result.isEmpty()) {
                    //progressDialog.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CatelogOrderDetailModel>>() {
                    }.getType();
                    orderList = new Gson().fromJson(result, listType);
                    adapter = new OrderItemAdapter(ItemListActivity.this, orderList, actualorder,filterkey,StatusName,buttonname,tailorList,"blank");
                    recyclerView.setAdapter(adapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            String accesstoken = params[16];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderItemID", orderItemId);
                parameters.add("MaterialType", materialtype);
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

                parameters.add("RoomName", roomName.trim());
                parameters.add("OrderID", orderID.trim());

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getFunctioncall();
                }
                progressDialog.dismiss();
            }
        }
    }


    private void getCatalogDesignSingle() {
        try {

            new getCatalogDesign().execute(catlogName.getText().toString(), design.getText().toString(), SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class getCatalogDesign extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            String accesstoken = strings[2];
            String catalogName = strings[0];
            String designName = strings[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet/" + catalogName + "/" + designName);
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<Catelog>() {
                }.getType();
                Catelog catelogdesign = new Gson().fromJson(result, listType);
                if (catelogdesign != null) {
                    price.setText(String.valueOf(catelogdesign.getPrice()));
                    if (catelogdesign.getUnit().trim().length() > 0) {
                        unitSpinner.setSelection(((ArrayAdapter<String>) unitSpinner.getAdapter()).getPosition(catelogdesign.getUnit().toString()));
                    }
                }


                //Getting the instance of AutoCompleteTextView

            }


        }
    }

    private void getCatalogList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new getCatalog().execute(SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class getCatalog extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            String accesstoken = strings[0];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet");
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                String[] cateloglist = new Gson().fromJson(result, listType);
                catadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, cateloglist);
                catlogName.setThreshold(1);//will start working from first character
                catlogName.setAdapter(catadapter);//setting the adapter data into the AutoCompleteTextView
                //Getting the instance of AutoCompleteTextView
                progressDialog.dismiss();


            }


        }
    }

    private void getDesignList() {
        try {
            // progressDialog.setMessage("loading...");
            // progressDialog.show();
            new getDesign().execute(catlogName.getText().toString(), SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            // progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class getDesign extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String json = "";
            String catalogName = strings[0];
            String accesstoken = strings[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet/" + catalogName);
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
                Type listType = new TypeToken<String[]>() {
                }.getType();
                String[] designlist = new Gson().fromJson(result, listType);
                designadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, designlist);
                design.setThreshold(1);//will start working from first character
                design.setAdapter(designadapter);//setting the adapter data into the AutoCompleteTextView
            }
        }
    }
}
