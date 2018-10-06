package com.example.awizom.dotapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AfterCreateOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private Intent intent;

    private TextView c_contact,i_name,i_contact,i_address,orderDateLabel,orderDate,amount;
    private ListView roomname;
    private long cid=0;
    DataOrder catelogOrderDetailModel;
    List<DataOrder> orderList;
    public  int hour = 0,minute = 0;

    public DatePicker datePicker;
    public Calendar calendar;
    public int year, month, day;
    public  String dateOb;
    public Calendar myCalendar ;
    public Date date;
    private AutoCompleteTextView c_name;

    private List<CustomerModel> customerlist;
    private String[] customerNameList;
    ArrayAdapter<String> adapter;
    private Button addorder,addroom;
    int morderid=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_create_order_layout);
        initView();
    }

    private void initView() {


        c_name = findViewById(R.id.customerName);
        c_contact = findViewById(R.id.customerContact);
        i_name = findViewById(R.id.interiorName);
        i_contact = findViewById(R.id.interiorMobile);
        i_address = findViewById(R.id.interiorAddress);

        orderDate = findViewById(R.id.orderValue);
        amount = findViewById(R.id.amountValue);

        orderDateLabel = findViewById(R.id.orderDatePicker);
        orderDateLabel.setOnClickListener(this);
        addorder = findViewById(R.id.addOrder);
        addorder.setOnClickListener(this);
        addroom = findViewById(R.id.addRoom);
        addroom.setOnClickListener(this);

        roomname = findViewById(R.id.hallList);

        c_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(c_name.getText().length()==0)
                {
                    cid=0;
                    c_contact.setText("");
                    i_address.setText("");
                    i_name.setText("");
                    i_contact.setText("");
                }
                else
                {
                    getCustomerDetail(c_name.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        getCustomerDetailList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderDatePicker:
                showDateTimePicker();
                break;
            case R.id.addOrder:
                if(c_name.getText().length() > 0) {
                    addroom.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.addRoom:
                addroomdailogueOpen(orderList.get(1).getOrderID(),orderList.get(1).getARoomList());
                break;

        }
    }

/*Room Add*/

    private void addroomdailogueOpen(final long orderid,String aroomlist){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AfterCreateOrderActivity.this);
        LayoutInflater inflater = LayoutInflater.from(AfterCreateOrderActivity.this);
        final View dialogView = inflater.inflate(R.layout.room_layout, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);

        String[] items = aroomlist.split(",");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddOrder);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Room");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (String.valueOf(spinner.getSelectedItem()).trim().length()>0) {
                    try {
                        new postAddRoom().execute(String.valueOf(orderid), String.valueOf(spinner.getSelectedItem()).trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                    b.dismiss();
                }
            }


        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });

    }
    private class postAddRoom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String orderid = params[0];
            String roomname = params[1];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderRoomPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", roomname);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("Error: " + e);
                Toast.makeText(AfterCreateOrderActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getMyOrder();
                }
            }
        }

    }

    private void getMyOrder() {
        try {
            morderid = orderList.get(1).getOrderID();
            new GETOrderList().execute(String.valueOf(morderid));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AfterCreateOrderActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class GETOrderList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            // String accesstoken = params[0];
            String orderid = params[0];
            //String clave = params[1];
            //String res = params[2];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderGet/"+orderid);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //  builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
            }
            return json;
        }
        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println(result);
                Gson gson = new Gson();
                Type getType = new TypeToken<DataOrder>(){}.getType();
                DataOrder  morder = new Gson().fromJson(result,getType);
                String[] roomName = morder.getRoomList().split(",");
               // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, roomName);
               // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // The drop down view
               // spinner.setAdapter(spinnerArrayAdapter);
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_button_roomlist,R.id.label, roomName);
                roomname.setAdapter( spinnerArrayAdapter );
                }
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.hallList:
                    startActivity(intent = new Intent(AfterCreateOrderActivity.this,RoomDetailsActivity.class));
                    break;
            }
        }
    }

    /*customer get*/
    private void getCustomerDetailList() {
        try {

            new getCustomerList().execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void getCustomerDetail(String cusname) {
        try {

            for (CustomerModel cm : customerlist) {
                if ( cm.getCustomerName().equals(cusname))
                {
                    cid=cm.getCustomerID();
                    c_contact.setText(cm.getMobile());
                    i_address.setText(cm.getAddress());
                    i_name.setText(cm.getInteriorName());
                    i_contact.setText(cm.getInteriorMobile());
                    break;
                }
            }
        } catch (Exception e) {

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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CustomerModel>>() {
                }.getType();
                customerlist = new Gson().fromJson(result, listType);
                customerNameList = new String[customerlist.size()];
                for (int i = 0; i < customerlist.size(); i++) {
                    customerNameList[i] = String.valueOf(customerlist.get(i).getCustomerName());

                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, customerNameList);
                c_name.setThreshold(1);//will start working from first character
                c_name.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                //Getting the instance of AutoCompleteTextView
            }


        }
    }


/*Date picker Show*/
    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(year, monthOfYear, dayOfMonth);
                orderDate.setText(dayOfMonth + "/"
                        + (monthOfYear + 1) + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

/*customer List get*/
    private void postOrder() {

    }


}
