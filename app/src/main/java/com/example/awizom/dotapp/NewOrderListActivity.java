package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.OrderListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.HandOverTelorList;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.DataOrder;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NewOrderListActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    //ProgressDialog progressDialog;
    List<DataOrder> orderList;
    RecyclerView recyclerView;
    OrderListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String filterKey = "";
    String valueButtonName = "";
    String statusName = "";
    String dailogMessage = "";
    String countvalue = "";
    private String message="";
    TextView errorMsg;
    private Intent pdfOpenintent;
    private ImageButton print,share;
    private  String id,c_name,c_contact,t_amt,r_n ;
    List<CatelogOrderDetailModel> orderestimateforcustomer;
    List<String> roomList;
    String p1;
    PdfPTable table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panding_order_list);
        initView();
    }

    private void initView() {

        // progressDialog = new ProgressDialog(getApplicationContext());
        errorMsg = findViewById(R.id.errorMessage);
        print=findViewById(R.id.print);
        share=findViewById(R.id.share);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //progressDialog = new ProgressDialog(this);


        filterKey = getIntent().getExtras().getString("FilterKey", "");
        valueButtonName = getIntent().getExtras().getString("ButtonName", "");
        statusName = getIntent().getExtras().getString("StatusName", "");
        dailogMessage = getIntent().getExtras().getString("DailogMessage", "");
        countvalue = getIntent().getExtras().getString("Count", "");
        share.setOnClickListener(this);
        print.setOnClickListener(this);

        if (statusName.equals("Reset")) {
            getSupportActionBar().setTitle("Dispatch");
        }


        if (statusName.equals("Cancel")) {
            getSupportActionBar().setTitle("panding For Adv");
        } else {
            getSupportActionBar().setTitle(statusName);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (!filterKey.equals("PandingToPlaceOrder")) {
                    getOrderList();
                } else if (filterKey.equals("PandingToPlaceOrder")) {
                    getOrderList();
                }
            }
        });

        if (!filterKey.equals("PandingToPlaceOrder")) {
            getOrderList();
        } else if (filterKey.equals("PandingToPlaceOrder")) {
            getOrderList();
        }


    }

//    private boolean validation() {
//        if (!SharedPrefManager.getInstance(NewOrderListActivity.this).getUser().userRole.contains("Admin"))
//        {
//
//        return true;
//        }
//        else
//        {
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "User is not permitted for reset",
//                    Toast.LENGTH_SHORT);
//
//            toast.show();
//            return false;
//
//        }
//
//    }


    private void getOrderList() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
//            progressDialog.setMessage("loading...");
//            progressDialog.show();
            new NewOrderListActivity.GetOrderDetails().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
//            progressDialog.dismiss();
            errorMsg.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.share:
                try {
                    getShareFunctioncall();
                }catch (Exception e){
                    e.printStackTrace();
                }

                  break;
            case R.id.print:
                try {
                    getPrintFunctioncalls();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;



        }




    }

    private void CreatePdf() {

        Document doc = new Document();

        PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2});
        table.getDefaultCell().

                setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell("Customer Name");
        table.addCell("Address");
        table.addCell("Mobile");
        table.addCell("Date");
        table.addCell("Advance");
        table.addCell("Amount");

        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (
                int j = 0;
                j < cells.length; j++)

        {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }


        for (
                int i = 0;
                i < orderList.size(); i++)


        {

            table.addCell(orderList.get(i).getCustomerName().toString());
            table.addCell(orderList.get(i).getAddress().toString());
            table.addCell(orderList.get(i).getMobile().toString());
            table.addCell(String.valueOf(orderList.get(i).getOrderDate().split("-")[0].trim()));
            table.addCell(String.valueOf(orderList.get(i).getAdvance()));
            table.addCell(String.valueOf(orderList.get(i).getTotalAmount()));



        }

        try

        {
            // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            String path = getApplicationContext().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

            File dir = new File(String.valueOf(path));
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "NewOrderList.pdf");

            FileOutputStream fOut = new FileOutputStream(file);


            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph("list");


            /* You can also SET FONT and SIZE like this */
            Font paraFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
            p1.setAlignment(Paragraph.ALIGN_CENTER);

            p1.setSpacingAfter(20);
            p1.setFont(paraFont1);
            doc.add(p1);

            /* You can also SET FONT and SIZE like this */


            doc.setMargins(0, 0, 5, 5);
            doc.add(table);

            Phrase footerText = new Phrase("This is an example of a footer");
            NewOrderListActivity.HeaderFooter pdfFooter = new NewOrderListActivity.HeaderFooter();
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



        pdfOpenintent = new Intent(getApplicationContext(), PdfViewActivity.class);
        pdfOpenintent = pdfOpenintent.putExtra("PDFName","/NewOrderList.pdf");
        startActivity( pdfOpenintent);
        //  openPdf();

    }

    private void CreateMessage()
    {
        String message="";
        for(int i=0;i<orderList.size();  i++  ) {

            String CustomerName="",Address="",Mobile="",Date="",Advance="",Amount="";

                CustomerName = (orderList.get(i).getCustomerName().toString());
                Address = (orderList.get(i).getAddress().toString());
                Mobile = (orderList.get(i).getMobile().toString());
                Date = (orderList.get(i).getOrderDate().toString().split("T")[0].trim());
                Advance = String.valueOf((orderList.get(i).getAdvance()));
                Amount = (orderList.get(i).getTotalAmount().toString());

                message = message+ "\nCustomerName = " + CustomerName +
                        "\nAddress = " + Address +
                        "\nMobile = " + Mobile +
                        "\nDate = " + Date +
                        "\nAdvance = " + Advance +
                        "\nAmount = " + Amount;

        }
        shareMessage(message);
    }

    private void shareMessage(String message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intent.createChooser(shareIntent, "SHARE"));
    }

    private class GetOrderDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String accesstoken = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();

                // String name= getArguments().getString("NAME_KEY").toString();
                if (filterKey.equals("pandingForAdv")) {
                    builder.url(AppConfig.BASE_URL_API + "OrderDetailsByFilterGet");
                } else {
                    builder.url(AppConfig.BASE_URL_API + "OrderDetailsByFilterGet/" + filterKey);
                }
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                errorMsg.setVisibility(View.VISIBLE);
                e.printStackTrace();
                // progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                errorMsg.setVisibility(View.VISIBLE);
                // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "There is no data available" +
                        "", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DataOrder>>() {
                }.getType();
                orderList = new Gson().fromJson(result, listType);
                adapter = new OrderListAdapter(NewOrderListActivity.this, orderList, filterKey, valueButtonName, statusName);
                recyclerView.setAdapter(adapter);
                //  progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }

    private void getShareFunctioncall() {
        message="";
        for (int j = 0; j < orderList.size(); j++) {
            c_name=orderList.get(j).getCustomerName();
            c_contact=orderList.get(j).getMobile();
            t_amt=String.valueOf(orderList.get(j).getATotalAmount());



            message = message + "Customer Name" + "=" + c_name + "\nContact No " + c_contact + "\nRoom Details" + orderList.get(j).getActuRoomList() +"\nTotal Amount= " +t_amt + "\n\n";


        }

        shareMessage(message);
    }



    private void getPrintFunctioncalls() {


        Document doc = new Document();
         table = new PdfPTable(new float[]{2, 2,2,2});
        table.getDefaultCell().
        setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Name");
        table.addCell("Contect No");
        table.addCell("Room Detail");
        table.addCell("Total");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (
                int j = 0;
                j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }

        for (int j = 0; j < orderList.size(); j++) {
            c_name=orderList.get(j).getCustomerName();
            c_contact=orderList.get(j).getMobile();
            t_amt=String.valueOf(orderList.get(j).getATotalAmount());

            table.addCell(c_name);
            table.addCell(c_contact);
            table.addCell(orderList.get(j).getActuRoomList());
            table.addCell("\n Total ="+t_amt);



        }


        try

        {
            // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            String path = NewOrderListActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
            File dir = new File(String.valueOf(path));
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "NewOrderList.pdf");

            FileOutputStream fOut = new FileOutputStream(file);


            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(c_name);


            /* You can also SET FONT and SIZE like this */
            Font paraFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
            p1.setAlignment(Paragraph.ALIGN_CENTER);

            p1.setSpacingAfter(20);
            p1.setFont(paraFont1);
            doc.add(p1);

            Paragraph p2 = new Paragraph(c_contact);


            /* You can also SET FONT and SIZE like this */
//                        Font paraFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
//                        p2.setAlignment(Paragraph.ALIGN_CENTER);
//
//                        p2.setSpacingAfter(20);
//                        p2.setFont(paraFont2);
//                        doc.add(p2);

            /* You can also SET FONT and SIZE like this */


            doc.setMargins(0, 0, 5, 5);
            doc.add(table);

            Phrase footerText = new Phrase("This is an example of a footer");
            NewOrderListActivity.HeaderFooter pdfFooter = new NewOrderListActivity.HeaderFooter();
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

        pdfOpenintent = new Intent(NewOrderListActivity.this, PdfViewActivity.class);
        pdfOpenintent = pdfOpenintent.putExtra("PDFName","/NewOrderList.pdf");
        startActivity( pdfOpenintent);

        for(int i=0;i<orderList.size();  i++  ) {
            id = String.valueOf(orderList.get(i).getOrderID());




        }

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
