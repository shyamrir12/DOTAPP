package com.example.awizom.dotapp.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView customerName, customerMobileNo, customerSno, customerOrder,customerDate,customerhall;
    private ImageButton additionButton;
    private TextView elight, roman, aPlat;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog ;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;
    OrderItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_details);
        initView();
    }

    private void initView() {

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

        switch (v.getId()){
            case R.id.addButton:
//                getFunctioncall();
                break;
        }
    }

    private void getFunctioncall() {

        try{
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new detailsGET().execute("test");

        }catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class detailsGET extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {

                String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderItemGet/1");
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
                Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
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
                Type listType = new TypeToken<List<CatelogOrderDetailModel>>(){}.getType();
                orderList = new Gson().fromJson(result,listType);

                adapter = new OrderItemAdapter(getBaseContext(), orderList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

            }


        }
    }
}
