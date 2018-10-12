package com.example.awizom.dotapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.example.awizom.dotapp.SigninActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AfterCreateOrderoFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private TextView c_contact,i_name,i_contact,i_address,orderDateLabel;
    private EditText orderDate,amount;
    private ListView roomname;
    String[] roomName;
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

    String orderid;
    String[] orderidPart;
    Intent intent;

    String actualRoomList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.after_create_order_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        c_name = view.findViewById(R.id.customerName);
        c_contact = view.findViewById(R.id.customerContact);
//        i_name = view.findViewById(R.id.interiorName);
//        i_contact = findViewById(R.id.interiorMobile);
        i_address = view.findViewById(R.id.interiorAddress);

        orderDate = view.findViewById(R.id.orderDatePicker);
        amount = view.findViewById(R.id.amountValue);

        orderDate.setOnTouchListener(this);
        orderDate.setInputType(InputType.TYPE_NULL);
        addorder = view.findViewById(R.id.addOrder);
        addorder.setOnClickListener(this);
        addroom = view.findViewById(R.id.addRoom);
        addroom.setOnClickListener(this);

        roomname = view.findViewById(R.id.hallList);

        c_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(c_name.getText().length()==0)
                {
                    cid=0;
                    c_contact.setText("");
                    i_address.setText("");
                    //i_name.setText("");
                    i_contact.setText("");
                }
                else
                {
                    getCustomerDetail(c_name.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        getCustomerDetailList();

        roomname.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getContext(), RoomDetailsActivity.class);
                intent.putExtra("RoomName", roomName[position].trim());
                intent.putExtra("OrderID",Integer.valueOf( orderidPart[1]));
                intent.putExtra("CustomerName",c_name.getText().toString());
                intent.putExtra("Mobile",c_contact.getText().toString());
                intent.putExtra("OrderDate",orderDate.getText().toString());
                intent.putExtra("Advance",Double.valueOf( amount.getText().toString()));
                startActivity(intent);
            }
        });

       // orderDate.setText( DateFormat.getDateInstance().format(new Date()) );

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addOrder:
                try {
                if(c_name.getText().length() > 0) {
                    postOrder();
                } }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.addRoom:
                addroomdailogueOpen(Long.parseLong(orderidPart[1]),actualRoomList);
                break;
        }
    }

    /*Room Add*/

    private void addroomdailogueOpen(final long orderid,String aroomlist){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.room_layout, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);

        String[] items = aroomlist.split(",");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
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
                        new postAddRoom().execute(String.valueOf(orderid), String.valueOf(spinner.getSelectedItem()).trim(),SharedPrefManager.getInstance(getContext()).getUser().access_token);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                    b.dismiss();
                }
            }


        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.orderDatePicker:
                showDateTimePicker();
                break;

        }
        return false;
    }

    private class postAddRoom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String accesstoken = params[0];
            String orderid = params[1];
            String roomname = params[2];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderRoomPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getMyOrder();
                }
            }
        }

    }

    private void getMyOrder() {
        try {

            new GETOrderList().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class GETOrderList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderGet/"+orderidPart[1]);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("Error: " + e);
                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
            }
            return json;
        }
        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println(result);
                Gson gson = new Gson();
                Type getType = new TypeToken<DataOrder>(){}.getType();
                DataOrder  morder = new Gson().fromJson(result,getType);
                roomName = morder.getRoomList().split(",");
                actualRoomList = morder.getARoomList();
                // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, roomName);
                // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // The drop down view
                // spinner.setAdapter(spinnerArrayAdapter);
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_button_roomlist,R.id.label, roomName);
                roomname.setAdapter( spinnerArrayAdapter );

            }
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.hallList:
                    startActivity(intent = new Intent(getContext(),RoomDetailsActivity.class));
                    break;
            }
        }
    }

    /*customer get*/
    private void getCustomerDetailList() {
        try {

            new getCustomerList().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
            String accesstoken = "";
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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CustomerModel>>() {
                }.getType();
                customerlist = new Gson().fromJson(result, listType);
                customerNameList = new String[customerlist.size()];
                for (int i = 0; i < customerlist.size(); i++) {
                    customerNameList[i] = String.valueOf(customerlist.get(i).getCustomerName());

                }
                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, customerNameList);
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
                orderDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    /*customer List get*/
    private void postOrder() {

        String date = orderDate.getText().toString();
        String advance = amount.getText().toString();
        try {

            //String
            new POSTOrder().execute(String.valueOf(cid),date,advance,SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String accesstoken = params[0];
            String customerid = params[1];
            String orderdate = params[2];
            String orderamount = params[3];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();

                parameters.add("CustomerID", customerid);
                parameters.add("OrderDate", orderdate);
                parameters.add("Advance", orderamount);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("Error: " + e);
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                orderid = jsonbodyres.getMessage().toString();
                orderidPart = orderid.split(",");
                Toast.makeText(getContext(),jsonbodyres.getMessage().toString(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {

                    addorder.setEnabled(false);
                    addorder.setClickable(false);
                    getMyOrder();
                    addroom.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
