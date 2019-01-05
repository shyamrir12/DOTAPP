package com.example.awizom.dotapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Adapters.RoomListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.AddCustomerFragment;
import com.example.awizom.dotapp.Fragments.CustomerListFrgment;
import com.example.awizom.dotapp.Fragments.DatePickerFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.Models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AfterCreateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView  i_name, i_contact,  orderDateLabel;
    private EditText orderDate, amount,textViewATotalAmount,c_contact,i_address;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private long cid = 0;
    DataOrder catelogOrderDetailModel;
    List<DataOrder> orderList;
    public int hour = 0, minute = 0;
    public DatePicker datePicker;
    public Calendar calendar;
    public int year, month, day;
    public String dateOb;
    public Calendar myCalendar;
    public Date date;
    private AutoCompleteTextView c_name,r_name;
    private List<CustomerModel> customerlist;
    List<HandOverModel> handOverlist1;
    private CustomerModel customer;
    private String[] customerNameList;
    ArrayAdapter<String> adapter;
    private Button addorder, addroom, actualRead, simpleRead, addUserStatus;
    private ImageButton addNewCustomer,  exit,share,print;
    int morderid = 0;
    String buttonname = "";
    String orderid = "";
    String actualorder = "";
    String filterkey = "";
    String stausname = "";
    String[] orderidPart;
    Intent intent;
    String actualRoomList;
    Fragment fragment = null;
    private Fragment addNewCustomerFragment;
    private ProgressDialog progressDialog;
    private AlertDialog b;
    private DataOrder data;
    String[] roomName;
    List<String> roomList;
    RecyclerView recyclerView;
    RoomListAdapter roomlistadapter;
    private Spinner spinner;
    AutoCompleteTextView roomText;
    List<CatelogOrderDetailModel> orderestimateforcustomer;
    List<CatelogOrderDetailModel> orderestimateforcustomer1;
    private Intent pdfOpenintent;
    private String hTelor;
   private EditText cName, cContact, cAddress, interioName, interioContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_create_order_layout);
        initView();
    }

    private void initView() {

        getFunctioncall();
        getSupportActionBar().setTitle("Create Order");
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfOpenintent = new Intent();

        c_name = findViewById(R.id.customerName);
        c_contact = findViewById(R.id.customerContact);
        share =findViewById(R.id.shareButton);
        print =findViewById(R.id.printButton);
        i_address = findViewById(R.id.interiorAddress);
        orderDate = findViewById(R.id.orderDatePicker);
        amount = findViewById(R.id.amountValue);
        textViewATotalAmount = findViewById(R.id.textViewATotalAmount);
        orderDate.setInputType(InputType.TYPE_NULL);

        addorder = findViewById(R.id.addOrder);
        addorder.setOnClickListener(this);
        addroom = findViewById(R.id.addRoom);
        addroom.setOnClickListener(this);
        addNewCustomer = findViewById(R.id.addnewCustomerButton);
        addNewCustomer.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // roomname = findViewById(R.id.hallList);
        addNewCustomerFragment = new AddCustomerFragment();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog = new ProgressDialog(this);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //message call
                getFunctioncall();
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFunctioncalls();
            }
        });


        orderDate.setOnClickListener(this);
//        roomname.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // TODO Auto-generated method stub
//                Intent intent=new Intent(getApplicationContext(), RoomDetailsActivity.class);
//                intent.putExtra("RoomName", roomName[position].split( "-" )[0].trim());
//                intent.putExtra("OrderID",Integer.valueOf( orderid));
//                intent.putExtra("CustomerName",c_name.getText().toString());
//                intent.putExtra("Mobile",c_contact.getText().toString());
//                intent.putExtra("OrderDate",orderDate.getText().toString());
//                intent.putExtra("Advance",Double.valueOf( amount.getText().toString()));
//                intent.putExtra("ActualOrder",actualorder);
//                startActivity(intent);
//            }
//        });
        try {
            orderid = getIntent().getExtras().getString("OrderID", "");
            actualorder = getIntent().getExtras().getString("ActualOrder", "");
            filterkey = getIntent().getExtras().getString("FilterKey", "");
            stausname = getIntent().getExtras().getString("StatusName", "");
            buttonname = getIntent().getExtras().getString("ButtonName", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!orderid.equals("")) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Refresh items
                    getMyOrder(orderid);
                }
            });
            getMyOrder(orderid);
            addroom.setVisibility(View.VISIBLE);

        } else {
            c_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (c_name.getText().length() == 0) {
                        cid = 0;
                        c_contact.setText("");
                        i_address.setText("");
                    } else {
                        getCustomerDetail(c_name.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            getCustomerDetailList();
            loadData();
            orderDate.setText(DateFormat.getDateInstance().format(new Date()));
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



    private void getFunctioncall() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            // progressDialog.setMessage("loading...");
            //  progressDialog.show();
            new AfterCreateActivity.detailsGET().execute( orderid, SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
//            mSwipeRefreshLayout.setRefreshing(false);
            //progressDialog.dismiss();
           // Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class detailsGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //String roomName = strings[0];
            String orderID = strings[0];
            String accesstoken = strings[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/" + orderID.trim() + "/" +"blank");
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
                    orderestimateforcustomer = new Gson().fromJson(result, listType);
                    String message="";
                    message="Mr./Mrs. : "+c_name.getText()+"\n Mobile no "+c_contact.getText() + "\n";

                    for(int j=0; j<roomList.size();j++)
                    {
                        message = message+"Room "+roomList.get(j).split("-")[0]+"\n";
                        for (int i = 0; i < orderestimateforcustomer.size(); i++) {
                            if (roomList.get(j).split("-")[0].trim().equals(orderestimateforcustomer.get(i).getRoomName().trim())) {
                                String materialtype = "", Aqty = "", Unit = "", Price = "", qty_price = "";


                                materialtype = (orderestimateforcustomer.get(i).getMaterialType().toString());

                                Aqty = String.valueOf(orderestimateforcustomer.get(i).getAQty());

                                Unit = (orderestimateforcustomer.get(i).getUnit().toString());

                                Price = (String.valueOf(Math.floor(orderestimateforcustomer.get(i).getPrice2())));
                                qty_price = String.valueOf(Math.floor(orderestimateforcustomer.get(i).getAQty() * orderestimateforcustomer.get(i).getPrice2()));

                                message = message + materialtype + "=" + Aqty + " " + Unit + "@" + Price + "=" + qty_price + "\n";
                            }


                        }

                    }



//                        for (int i = 0; i < orderestimateforcustomer.size(); i++) {
//                            String materialtype ="", Aqty="",Unit="",Price="",qty_price="";
//
//
//                            materialtype = (orderestimateforcustomer.get(i).getMaterialType().toString());
//
//                            Aqty=String.valueOf(orderestimateforcustomer.get(i).getAQty());
//
//                            Unit=(orderestimateforcustomer.get(i).getUnit().toString());
//
//                            Price=(String.valueOf(Math.floor(orderestimateforcustomer.get(i).getPrice2())));
//                            qty_price=String.valueOf(Math.floor(orderestimateforcustomer.get(i).getAQty()*orderestimateforcustomer.get(i).getPrice2()));
//
//                            message =message+materialtype + "=" + Aqty + " "+ Unit +"@" + Price +"="  + qty_price + "\n" ;
//
//
//
//                        }
                    message=message+"\n Total Amount= "+textViewATotalAmount.getText().toString();
                    shareMessage(message);





                    // adapter = new OrderItemAdapter(getBaseContext(), orderList, actualorder,filterkey,StatusName,buttonname,tailorList);
                    // recyclerView.setAdapter(adapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getFunctioncalls() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            // progressDialog.setMessage("loading...");
            //  progressDialog.show();

            new AfterCreateActivity.detailseGET().execute( orderid, SharedPrefManager.getInstance(this).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
            //progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class detailseGET extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //String roomName = strings[0];
            String orderID = strings[0];
            String accesstoken = strings[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderItemGet/" + orderID.trim() + "/" +"blank");
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
                    orderestimateforcustomer1 = new Gson().fromJson(result, listType);
                    Document doc = new Document();

                    PdfPTable table = new PdfPTable(new float[]{2, 2});
                    table.getDefaultCell().

                            setHorizontalAlignment(Element.ALIGN_CENTER);

                    table.addCell("Customer Details");

                    table.addCell("Items");


                    table.setHeaderRows(1);
                    PdfPCell[] cells = table.getRow(0).getCells();
                    for (
                            int j = 0;
                            j < cells.length; j++)
                    {
                        cells[j].setBackgroundColor(BaseColor.GRAY);
                    }
                    String message="";
                   // message="Mr./Mrs. : "+c_name.getText()+"\n Mobile no "+c_contact.getText() + "\n";

                    for(int j=0; j<roomList.size();j++) {
                        message = message + "Room " + roomList.get( j ).split( "-" )[0] + "\n";
                        for (int i = 0; i < orderestimateforcustomer1.size(); i++) {
                            if (roomList.get( j ).split( "-" )[0].trim().equals( orderestimateforcustomer1.get( i ).getRoomName().trim() )) {
                                String materialtype = "", Aqty = "", Unit = "", Price = "", qty_price = "";


                                materialtype = (orderestimateforcustomer1.get( i ).getMaterialType().toString());

                                Aqty = String.valueOf( orderestimateforcustomer1.get( i ).getAQty() );

                                Unit = (orderestimateforcustomer1.get( i ).getUnit().toString());

                                Price = (String.valueOf( Math.floor( orderestimateforcustomer1.get( i ).getPrice2() ) ));
                                qty_price = String.valueOf( Math.floor( orderestimateforcustomer1.get( i ).getAQty() * orderestimateforcustomer1.get( i ).getPrice2() ) );

                                message = message + materialtype + "=" + Aqty + " " + Unit + "@" + Price + "=" + qty_price + "\n";
                            }


                        }

                    }

                   // message=message+"\n Total Amount= "+textViewATotalAmount.getText().toString();


                        table.addCell(c_name.getText().toString()+"\n"+c_contact.getText().toString()+"\n Total="+textViewATotalAmount.getText().toString());

                        table.addCell(message);






                    try

                    {
                        // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

                        String path = AfterCreateActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
                        File dir = new File(String.valueOf(path));
                        if (!dir.exists())
                            dir.mkdirs();

                        Log.d("PDFCreator", "PDF Path: " + path);

                        File file = new File(dir, "OrderItemList.pdf");

                        FileOutputStream fOut = new FileOutputStream(file);


                        PdfWriter.getInstance(doc, fOut);

                        //open the document
                        doc.open();

                        Paragraph p1 = new Paragraph(c_name.getText().toString());


                        /* You can also SET FONT and SIZE like this */
                        Font paraFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
                        p1.setAlignment(Paragraph.ALIGN_CENTER);

                        p1.setSpacingAfter(20);
                        p1.setFont(paraFont1);
                        doc.add(p1);

                        Paragraph p2 = new Paragraph(c_contact.getText().toString());


                        /* You can also SET FONT and SIZE like this */
                        Font paraFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
                        p2.setAlignment(Paragraph.ALIGN_CENTER);

                        p2.setSpacingAfter(20);
                        p2.setFont(paraFont2);
                        doc.add(p2);

                        /* You can also SET FONT and SIZE like this */


                        doc.setMargins(0, 0, 5, 5);
                        doc.add(table);

                        Phrase footerText = new Phrase("This is an example of a footer");
                        AfterCreateActivity.HeaderFooter pdfFooter = new AfterCreateActivity.HeaderFooter();
                        doc.newPage();

                        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();


                    } catch (
                            DocumentException de)

                    {
                        Log.e("PDFCreator", "DocumentException:" + de);
                    } catch (
                            IOException e)

                    {
                        Log.e("PDFCreator", "ioException:" + e);
                    } finally

                    {
                        doc.close();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);

                    pdfOpenintent = new Intent(AfterCreateActivity.this, PdfViewActivity.class);
                    pdfOpenintent = pdfOpenintent.putExtra("PDFName","/OrderItemList.pdf");
                    startActivity( pdfOpenintent);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public  void shareMessage(String message)
    {
//
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, message );
//        sendIntent.setType("text/plain");
//        context.startActivity(sendIntent);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, "SHARE"));

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        //String dateString= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        // Date date = new Date();
        orderDate.setText(formatter.format(c.getTime()));

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

                if (!c_name.getText().toString().isEmpty()) {
                    try {
                        if (c_name.getText().length() > 0) {
                            postOrder();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    c_name.setError("Please enter customer name");
                }

                break;
            case R.id.addRoom:
                addroomdailogueOpen(Long.parseLong(orderid), actualRoomList);
                break;
            case R.id.addnewCustomerButton:
                openUpdateDailoge();
                break;
            case R.id.orderDatePicker:
                DialogFragment datepicker = new DatePickerFragment();

                datepicker.show(getSupportFragmentManager(), "date picker");
                break;
//            case  R.id.addstatus:
//                    addStatusUser();
//                break;


        }

    }


    private void addStatusUser() {
        try {

            //String res="";
            progressDialog.setMessage("loading...");
            progressDialog.show();
            if (filterkey.equals("pandingForAdv") || filterkey.equals("orderCreate") || filterkey.equals("PandingToPlaceOrder"))

                new AfterCreateActivity.POSTStatus().execute(orderid, "0", "0", "0", "0", "0", "", "", "", SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
            else
                Toast.makeText(getApplicationContext(), "Not Editable After Taking Advance: ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }
    }


    private void openUpdateDailoge() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.openpdatedialog, null);
        dialogBuilder.setView(dialogView);

        //  dialogBuilder.setTitle("Create Customer");

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
                if ((cName.getText().toString().isEmpty()) || (cContact.getText().toString().isEmpty()) ) {
/*|| (cAddress.getText().toString().isEmpty()) || (interioName.getText().toString().isEmpty())
                        || (interioContact.getText().toString().isEmpty())*/
                    cName.setError("Customer Name is required!");
                    cContact.setError("Customer Contact is required!");

                } else {
                    customerAddPost();
                }
//                if (cName.getText().toString().isEmpty() || cContact.getText().toString().isEmpty()) {
//                    cName.setError("customer name is required!");
//                    cContact.setError("customer contact is required!");
//                }else {
//                    String name = cName.getText().toString().trim();
//                    String contact = cContact.getText().toString().trim();
//                    String address = cAddress.getText().toString().trim();
//                    String intename = interioName.getText().toString().trim();
//                    String intecontact = interioContact.getText().toString().trim();
//
//                    try {
//                        //String res="";
//                        progressDialog.setMessage("loading...");
//                        progressDialog.show();
//                        new POSTAddCustomer().execute(name, contact, address, intename, intecontact, SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//                        // System.out.println("Error: " + e);
//                    }
//
//                }
            }
        });

    }
    private void customerAddPost() {

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
        }


    }
    private class POSTAddCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String customername = params[0];
            String mobile = params[1];
            String address = params[2];

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
                    getCustomerDetailList();
                    b.dismiss();

                }
                progressDialog.dismiss();
            }
        }
    }

    /*Room Add*/

    private void addroomdailogueOpen(final long orderid, String aroomlist) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.room_layout, null);
        dialogBuilder.setView(dialogView);
        spinner = dialogView.findViewById(R.id.spinner);
        roomText = dialogView.findViewById(R.id.roomNameText);


//        roomText.setThreshold(1);//will start working from first character
//        roomText.setAdapter(adapter);


        final String[] items = aroomlist.split(",");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        roomText.setThreshold(1);//will start working from first character
        roomText.setAdapter(spinnerArrayAdapter);

        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddOrder);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        exit = dialogView.findViewById(R.id.exit);

        //dialogBuilder.setTitle("Add Room" );

        dialogBuilder.setCancelable(true);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (String.valueOf(spinner.getSelectedItem()).trim().length() > 0) {
                if (roomText.getText().toString().length() > 0) {

                    try {
                        if (filterkey.equals("pandingForAdv") || filterkey.equals("orderCreate") || filterkey.equals("PandingToPlaceOrder"))
                            new postAddRoom().execute(String.valueOf(orderid), roomText.getText().toString(), SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
                        else
                            Toast.makeText(getApplicationContext(), "Not Editable After Taking Advance: ", Toast.LENGTH_SHORT).show();
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

                parameters.add("Elight", "0");
                parameters.add("Roman", "0");
                parameters.add("APlat", "0");
                parameters.add("RomanPrice", "0");
                parameters.add("ElightPrice", "0");
                parameters.add("APlatPrice", "0");


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
//                    Intent intent=new Intent(getApplicationContext(), RoomDetailsActivity.class);
//                    intent.putExtra("RoomName",  String.valueOf(spinner.getSelectedItem()).split("-")[0].trim());
//                    intent.putExtra("OrderID",String.valueOf( orderid));
//                    intent.putExtra("CustomerName",c_name.getText().toString());
//                    intent.putExtra("Mobile",c_contact.getText().toString());
//                    intent.putExtra("OrderDate",orderDate.getText().toString());
//                    intent.putExtra("Advance",Double.valueOf( amount.getText().toString()));
//                    intent.putExtra("ActualOrder",actualorder);
//                    intent.putExtra("StatusName", stausname);
//                    intent.putExtra("FilterKey", filterkey);
//                    intent.putExtra("ButtonName", buttonname);
//                    intent.putExtra("TailorList","");
//                    startActivity(intent);
                    getMyOrder(orderid);
                }
            }
        }

    }

    private void getMyOrder(String orderId) {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new GETOrderList().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }

    private class GETOrderList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = params[0];
            String orderid = params[1];
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderGet/" + orderid);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                // System.out.println("Error: " + e);
//                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                } else {
                    //System.out.println(result);
                    Gson gson = new Gson();
                    Type getType = new TypeToken<DataOrder>() {
                    }.getType();
                    DataOrder morder = new Gson().fromJson(result, getType);
                    cid = morder.getCustomerID();
                    c_name.setText(morder.getCustomerName());
                    c_contact.setText(morder.getMobile());
                    i_address.setText(morder.getAddress());
                    orderDate.setText(morder.getOrderDate().split("T")[0]);
                    amount.setText(String.valueOf(morder.getAdvance()));

                    if (actualorder.equals("ActualOrder")) {
                        textViewATotalAmount.setText(String.valueOf(morder.getATotalAmount()));
                        roomName = morder.getActuRoomList().split(",");
                    } else {
                        textViewATotalAmount.setText(String.valueOf(morder.getTotalAmount()));
                        roomName = morder.getRoomList().split(",");
                    }

                    actualRoomList = morder.getARoomList();

                    roomList = Arrays.asList(roomName);

//                    String StatusName,

                    roomlistadapter = new RoomListAdapter(getApplicationContext(), roomList, stausname, orderid
                            , c_name.getText().toString(), c_contact.getText().toString()
                            , orderDate.getText().toString()
                            , amount.getText().toString(), actualorder, filterkey, buttonname, morder.getTelorList());
                    recyclerView.setAdapter(roomlistadapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                    // ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_button_roomlist, R.id.label, roomName);
                    //roomname.setAdapter(spinnerArrayAdapter);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

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
            new getCustomer().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token, cusname);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
                //          Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
                    customerNameList[i] = String.valueOf(customerlist.get(i).getCustomerName());                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, customerNameList);
                c_name.setThreshold(1);//will start working from first character
                c_name.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                //Getting the instance of AutoCompleteTextView
            }


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
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<CustomerModel>() {
                }.getType();
                customer = new Gson().fromJson(result, listType);
                if (customer != null) {
                    cid = customer.getCustomerID();
                    c_contact.setText(customer.getMobile());
                    i_address.setText(customer.getAddress());
                }
            }
        }
    }

    /*customer List get*/
    private void postOrder() {

            String  date = orderDate.getText().toString();
            String advance = amount.getText().toString();

        try {
            if(filterkey.equals("orderCreate") )
                new POSTOrder().execute(String.valueOf(cid), date, advance, SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);

          else if (filterkey.equals("pandingForAdv") || filterkey.equals("PandingToPlaceOrder")) {
              if(Double.parseDouble(textViewATotalAmount.getText().toString()) > 0) {
                  new POSTOrder().execute(String.valueOf(cid), date, advance, SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
              }else {
                  Toast.makeText(getApplicationContext(), "First add items then take advance ", Toast.LENGTH_SHORT).show();

              }
            }
            else
                Toast.makeText(getApplicationContext(), "Not Editable After Taking Advance: ", Toast.LENGTH_SHORT).show();

            //String
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
                if (!orderid.equals("")) {
                    parameters.add("OrderID", orderid);
                }
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
//                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
                orderidPart = jsonbodyres.getMessage().split(",");
                orderid = orderidPart[1];
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage().toString(), Toast.LENGTH_SHORT).show();

//                if ((((SharedPrefManager.getInstance(getApplicationContext()).getUser().userRole.contains("Admin")))))
//
//                {
//                    Intent i = new Intent(AfterCreateActivity.this,HomeActivity.class);
//                    startActivity(i);
//                }
//
//                else
//                {
//                    Intent i = new Intent(AfterCreateActivity.this,HomeActivityUser.class);
//                    startActivity(i);
//                }
                if (jsonbodyres.getStatus() == true) {

                    addorder.setEnabled(false);
                    addorder.setClickable(false);
                    getMyOrder(orderid);
                    //addorder.setVisibility( View.GONE );
                    addroom.setVisibility(View.VISIBLE);
                    //  addUserStatus.setVisibility(View.VISIBLE);
                    //post status
//                    try {
//
//                        //String res="";
//                        progressDialog.setMessage("loading...");
//                        progressDialog.show();
//                        new AfterCreateActivity.POSTStatus().execute(orderid, "0", "0", "0", "0", "0", "", "", "",SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//                        // System.out.println("Error: " + e);
//                    }
                }
            }
        }

    }


    private class POSTStatus extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String orderid = params[0];
            String OrderPlaced = params[1];
            String MaterialReceived = params[2];
            String ReceivedFromTalor = params[3];

            String Dispatch = params[4];
            String Cancel = params[5];
            String HandOverTo = params[6];
            String TelorName = params[7];
            String ReceivedBy = params[8];
            String accesstoken = params[9];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);

                parameters.add("OrderPlaced", OrderPlaced);
                parameters.add("MaterialReceived", MaterialReceived);
                parameters.add("ReceivedFromTalor", ReceivedFromTalor);
                parameters.add("Cancel", Cancel);
                parameters.add("Dispatch", Dispatch);

                parameters.add("HandOverTo", HandOverTo);
                parameters.add("TelorName", TelorName);
                parameters.add("ReceivedBy", ReceivedBy);

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

                    addUserStatus.setVisibility(View.INVISIBLE);
                }
                progressDialog.dismiss();

            }


        }

    }

    private void createPDF() {


        //  openPdf();

    }

    private class HeaderFooter {

        Phrase[] header = new Phrase[2];
        /**
         * Current page number (will be reset for every chapter).
         */
        int pagenumber;

        /**
         * Initialize one of the headers.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("Movie history");
        }

        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         * com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {
            header[1] = new Phrase(title.getContent());
            pagenumber = 1;
        }

        /**
         * Increase the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }

        /**
         * Adds the header and the footer.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            switch (writer.getPageNumber() % 2) {
                case 0:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT, header[0],
                            rect.getRight(), rect.getTop(), 0);
                    break;
                case 1:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_LEFT, header[1],
                            rect.getLeft(), rect.getTop(), 0);
                    break;
            }
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }
}
