package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.awizom.dotapp.Models.Catelog;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.ElightBottomModel;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnLongClickListener {

    private TextView customerName, customerMobileNo, customerSno, customerOrder, customerDate, customerhall;
    private ImageButton additionButton;
    private TextView elight, roman, aPlat, totalAmount;

    private RelativeLayout relative_Layout_press, relativeLayout_edit_dailog, bottom_relative_press1;
    private RecyclerView recyclerView;
    private EditText editElight, editRoman, editAplot;
    private Button updateBottom, cancelElight;

    ProgressDialog progressDialog;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;
    ElightBottomModel morder;
    ArrayAdapter<String> catadapter;
    ArrayAdapter<String> designadapter;
    OrderItemAdapter adapter;
    private Intent intent;
    private EditText s_no, pageNo, price, price2, qty, aQty;
    private AutoCompleteTextView catlogName, design;
    private Spinner unitSpinner, materialType;
    private Button addButton, cancelButton;
    //  private AlertDialog b;
    private String roomName, orderID, customernAME, mobileNumber, orderDate, advance;

    // private Toolbar toolbar;private TextView textView; private ImageButton arrow_id_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details);
        initView();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private void initView() {

        getSupportActionBar().setTitle("Room Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        roomName = getIntent().getExtras().getString("RoomName", "");
        orderID = String.valueOf(getIntent().getIntExtra("OrderID", 0));
        customernAME = getIntent().getExtras().getString("CustomerName", "");
        mobileNumber = getIntent().getExtras().getString("Mobile", "");
        orderDate = getIntent().getExtras().getString("OrderDate", "");
        advance = String.valueOf(getIntent().getDoubleExtra("Advance", 0));

        customerName = findViewById(R.id.customer_name);
        customerMobileNo = findViewById(R.id.customer_mobile_no);
        customerOrder = findViewById(R.id.order_date);
        customerhall = findViewById(R.id.room_name);

        customerName.setText(customernAME);
        customerMobileNo.setText(mobileNumber);
        String date[] = orderDate.split("T", 0);
        customerOrder.setText(date[0]);
        customerhall.setText(roomName);


        elight = findViewById(R.id.elight_value);
        roman = findViewById(R.id.roman_value);
        aPlat = findViewById(R.id.aPlat_value);
        totalAmount = findViewById(R.id.total_value);
        additionButton = findViewById(R.id.updateButton);


        relative_Layout_press = findViewById(R.id.bottom_relative_press);
        relative_Layout_press.setOnClickListener(this);
        bottom_relative_press1 = findViewById(R.id.bottom_relative_press1);
        bottom_relative_press1.setOnClickListener(this);
        elight.setOnLongClickListener(this);
        roman.setOnLongClickListener(this);
        aPlat.setOnLongClickListener(this);

        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        additionButton.setOnClickListener(this);
        getFunctioncall();
        getElightBottom();

        catelogOrderDetailModel = new CatelogOrderDetailModel();

       /* textView = findViewById(R.id.activity_id_name);
        textView.setText("Room Details");
        arrow_id_back = findViewById(R.id.arrow_id_back);
        arrow_id_back.setOnClickListener(this);
        arrow_id_back.setVisibility(View.VISIBLE);*/
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.updateButton:
                initViewByAlertdailog();
                break;
            case R.id.bottom_relative_press:
                dilogShow();
                break;
            case R.id.bottom_relative_press1:
                dilogShow();
                break;
        }
    }


    private void dilogShow() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_bottom_item, null);
        dialogBuilder.setView(dialogView);

        editElight = dialogView.findViewById(R.id.editElight);
        editRoman = dialogView.findViewById(R.id.editRoman);
        editAplot = dialogView.findViewById(R.id.editAplot);

        updateBottom = dialogView.findViewById(R.id.updateElight);
        cancelElight = dialogView.findViewById(R.id.cancelElight);
        editElight.setText(morder.Elight.toString());
        editRoman.setText(morder.Roman.toString());
        editAplot.setText(morder.APlat.toString());
        dialogBuilder.setTitle("Edit bottom");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        updateBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String elight = editElight.getText().toString();
                String roman = editRoman.getText().toString();
                String aplot = editAplot.getText().toString();
                try {

                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new RoomDetailsActivity.POSTElight().execute(roomName.trim(), String.valueOf(orderID).trim(), elight, roman, aplot);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }

                b.dismiss();

            }


        });


        cancelElight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.elight:
                dilogShow();
                break;
            case R.id.aPlat:
                dilogShow();
                break;
            case R.id.roman:
                dilogShow();
                break;
        }
        return false;
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
        unitSpinner = dialogView.findViewById(R.id.unit);
        getCatalogList();
        addButton = dialogView.findViewById(R.id.add);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        dialogBuilder.setTitle("Add Order");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        catlogName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                design.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (catlogName.getText().length() > 0)
                    getDesignList();
            }
        });
        design.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                price.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (design.getText().length() > 0)
                    getCatalogDesignSingle();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String snumber = s_no.getText().toString();
                String catlogname = catlogName.getText().toString();
                String desiGn = design.getText().toString();
                String page_no = pageNo.getText().toString();
                String priCe = price.getText().toString();
                String priCe2 = price2.getText().toString();
                String qTy = qty.getText().toString();
                String aqty = aQty.getText().toString();
                String materialtype = materialType.getSelectedItem().toString();
                String unIt = unitSpinner.getSelectedItem().toString();
                try {
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new POSTOrder().execute("0", materialtype, priCe2, qTy, "0", unIt, "0", catlogname, snumber, desiGn, page_no, priCe, unIt, "0", roomName, orderID);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
                b.dismiss();

            }


        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });

    }

    private void getFunctioncall() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new RoomDetailsActivity.detailsGET().execute(roomName, orderID);
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
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/" + orderID.trim() + "/" + roomName.trim());
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

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
            try {


                if (result.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CatelogOrderDetailModel>>() {
                    }.getType();
                    orderList = new Gson().fromJson(result, listType);
                    adapter = new OrderItemAdapter(getBaseContext(), orderList);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
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

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

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
                    getElightBottom();
                }
                progressDialog.dismiss();
            }
        }
    }

    private void getElightBottom() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new elightdetailsGET().execute(roomName, orderID);

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

    }

    private class elightdetailsGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String roomName = strings[0];
            String orderID = strings[1];
            String json = "";

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "RoomGet/" + orderID.trim() + "/" + roomName.trim());
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

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
                Type getType = new TypeToken<ElightBottomModel>() {
                }.getType();
                morder = new Gson().fromJson(result, getType);

                elight.setText(morder.Elight.toString());

                roman.setText(morder.Roman.toString());

                aPlat.setText(morder.APlat.toString());

                totalAmount.setText(Double.toString(morder.getTotalAmount()));
                progressDialog.dismiss();

            }


        }
    }

    private class POSTElight extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String roomname = params[0];
            String orderid = params[1];
            String elight = params[2];
            String roman = params[3];
            String aPlat = params[4];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderRoomPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("RoomName", roomname.trim());
                parameters.add("OrderID", orderid);
                parameters.add("Elight", elight);
                parameters.add("Roman", roman);
                parameters.add("APlat", aPlat);


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
                    getElightBottom();

                }
                progressDialog.dismiss();

            }


        }

    }

    private void getCatalogDesignSingle() {
        try {

            new getCatalogDesign().execute(catlogName.getText().toString(), design.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class getCatalogDesign extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            String catalogName = strings[0];
            String designName = strings[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet/" + catalogName + "/" + designName);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(RoomDetailsActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {

                Toast.makeText(RoomDetailsActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
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
            new getCatalog().execute();
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
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(RoomDetailsActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(RoomDetailsActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                String[] cateloglist = new Gson().fromJson(result, listType);


                catadapter = new ArrayAdapter<String>(RoomDetailsActivity.this, android.R.layout.select_dialog_item, cateloglist);
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
            new getDesign().execute(catlogName.getText().toString());
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
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CatalogGet/" + catalogName);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RoomDetailsActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {

                Toast.makeText(RoomDetailsActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                String[] designlist = new Gson().fromJson(result, listType);
                designadapter = new ArrayAdapter<String>(RoomDetailsActivity.this, android.R.layout.select_dialog_item, designlist);
                design.setThreshold(1);//will start working from first character
                design.setAdapter(designadapter);//setting the adapter data into the AutoCompleteTextView

            }


        }
    }
}
