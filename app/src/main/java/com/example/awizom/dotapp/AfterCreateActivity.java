package com.example.awizom.dotapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.AddCustomerFragment;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AfterCreateActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Button addorder,addroom,actualRead,simpleRead;
    private ImageButton addNewCustomer;
    int morderid=0;

    String orderid="";
    String[] orderidPart;
    Intent intent;
    String actualRoomList;
    Fragment fragment = null;
    private Fragment addNewCustomerFragment;
    private ProgressDialog progressDialog;
    private AlertDialog b;
    private DataOrder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_create_order_layout);
        initView();
    }

    private void initView() {

        c_name = findViewById(R.id.customerName);
        c_contact = findViewById(R.id.customerContact);
        i_address = findViewById(R.id.interiorAddress);
        orderDate = findViewById(R.id.orderDatePicker);
        amount = findViewById(R.id.amountValue);

        orderDate.setInputType( InputType.TYPE_NULL);
        orderDate.setOnClickListener(this);
        addorder = findViewById(R.id.addOrder);
        addorder.setOnClickListener(this);
        addroom = findViewById(R.id.addRoom);
        addroom.setOnClickListener(this);
        addNewCustomer = findViewById(R.id.addnewCustomerButton);
        addNewCustomer.setOnClickListener(this);

        roomname = findViewById(R.id.hallList);
        addNewCustomerFragment = new AddCustomerFragment();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog = new ProgressDialog(this);



        roomname.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO Auto-generated method stub
                Intent intent=new Intent(getApplicationContext(), RoomDetailsActivity.class);
                intent.putExtra("RoomName", roomName[position].trim());
                intent.putExtra("OrderID",Integer.valueOf( orderid));
                intent.putExtra("CustomerName",c_name.getText().toString());
                intent.putExtra("Mobile",c_contact.getText().toString());
                intent.putExtra("OrderDate",orderDate.getText().toString());
                intent.putExtra("Advance",Double.valueOf( amount.getText().toString()));
                startActivity(intent);
            }
        });



        orderid = getIntent().getExtras().getString( "OrderID", "" );
        if(!orderid.equals( "" )){
            getMyOrder( orderid );
        }
        else {
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
            loadData();
            orderDate.setText( DateFormat.getDateInstance().format(new Date()) );
        }

    }

    private void loadData() {
        try {
            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GETLoadDataList().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);

            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }

    private class GETLoadDataList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderGet");
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
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type getType = new TypeToken<DataOrder>() {
                }.getType();
                //data = new Gson().fromJson(result, getType);

            }


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addOrder:
                try {
                    if (c_name.getText().length() > 0) {
                        postOrder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addRoom:
                addroomdailogueOpen(Long.parseLong(orderid), actualRoomList);
                break;
            case R.id.addnewCustomerButton:
                openUpdateDailoge();
                break;
            case R.id.orderDatePicker:
                showDateTimePicker();
                break;
        }

    }

    private void openUpdateDailoge() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customer_add_layout, null);
        dialogBuilder.setView(dialogView);
        final EditText cName, cContact, cAddress, interioName, interioContact;
        dialogBuilder.setTitle("Create Customer");

        b = dialogBuilder.create();
        b.show();
        cName = dialogView.findViewById(R.id.customerName);
        cContact = dialogView.findViewById(R.id.contact);
        cAddress = dialogView.findViewById(R.id.password);
        interioName = dialogView.findViewById(R.id.confrmPassword);
        interioContact = dialogView.findViewById(R.id.interiormobile);



        final Button buttonCreate = dialogView.findViewById(R.id.customerButton);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cName.getText().toString().trim();
                String contact = cContact.getText().toString().trim();
                String address = cAddress.getText().toString().trim();
                String intename = interioName.getText().toString().trim();
                String intecontact = interioContact.getText().toString().trim();

                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new POSTAddCustomer().execute(name, contact, address, intename, intecontact, SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
                }
            }
        });
    }

    private class POSTAddCustomer extends AsyncTask<String, Void, String> {
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                startActivity(intent = new Intent(getApplicationContext(), CustomerListFrgment.class));
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    b.dismiss();
                }
                progressDialog.dismiss();
            }
        }
    }

    /*Room Add*/

    private void addroomdailogueOpen(final long orderid,String aroomlist){



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.room_layout, null);
        dialogBuilder.setView(dialogView);


        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);

        String[] items = aroomlist.split(",");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
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
                        new postAddRoom().execute(String.valueOf(orderid), String.valueOf(spinner.getSelectedItem()).trim(),SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
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
                b.dismiss();
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
            String accesstoken = params[2];
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    getMyOrder(orderid);
                }
            }
        }

    }

    private void getMyOrder(String orderId) {
        try {

            new GETOrderList().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token,orderId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class GETOrderList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = params[0];
            String orderid=params[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderGet/"+orderid);
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
//                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
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
                cid=morder.getCustomerID();
                c_contact.setText(morder.getMobile());
                i_address.setText(morder.getAddress());
                orderDate.setText( morder.getOrderDate() );
                  amount.setText(String.valueOf(  morder.getAdvance()) );
                roomName = morder.getRoomList().split(",");
                actualRoomList = morder.getARoomList();
                // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, roomName);
                // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // The drop down view
                // spinner.setAdapter(spinnerArrayAdapter);
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_button_roomlist,R.id.label,roomName);
                roomname.setAdapter( spinnerArrayAdapter );

            }
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.hallList:
                    startActivity(intent = new Intent(getApplicationContext(),RoomDetailsActivity.class));
                    break;
            }
        }
    }

    /*customer get*/
    private void getCustomerDetailList() {
        try {

            new getCustomerList().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
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
//                    i_name.setText(cm.getInteriorName());
//                    i_contact.setText(cm.getInteriorMobile());
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
            String accesstoken = strings[0];
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
                orderDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    /*customer List get*/
    private void postOrder() {

        String date = orderDate.getText().toString();
        String advance = amount.getText().toString();
        try {

            //String
            new POSTOrder().execute(String.valueOf(cid),date,advance,SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }
    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String customerid = params[0];
            String orderdate = params[1];
            String orderamount = params[2];
            String accesstoken = params[3];
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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
                orderidPart =  jsonbodyres.getMessage().split(",");
                orderid=orderidPart[1];
                Toast.makeText(getApplicationContext(),jsonbodyres.getMessage().toString(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {

                    addorder.setEnabled(false);
                    addorder.setClickable(false);
                    getMyOrder(orderid);
                    addroom.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
