package com.example.awizom.dotapp.Fragments;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.CustomerListAdapter;
import com.example.awizom.dotapp.Adapters.HandOverAdapter;
import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Adapters.OrderListAdapter;
import com.example.awizom.dotapp.Adapters.TelorListAdapter;
import com.example.awizom.dotapp.Adapters.UserListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.TelorModel;
import com.example.awizom.dotapp.Models.UserModel;
import com.example.awizom.dotapp.PdfViewActivity;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HandOverTelorList extends Fragment {

    ProgressDialog progressDialog;
    ListView lv;
    TextView telornam, nodata;
    ImageButton img2,print;
    RecyclerView lv1;
    List<HandOverModel> handOverlist1;
    HandOverAdapter adapterh;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Handler handler = new Handler();
    Runnable refresh;
    private Button add, cancel;
    private EditText t_name, old_t_name;
    ArrayAdapter<String> handoverListAapter;
    String[] telorlist;
    private String r, str;
    private String[] catalogname, data;
    Type listType;
    private String hTelor;
    private Intent pdfOpenintent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.handover_telor_list, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        pdfOpenintent = new Intent();
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while loading telors");
        lv = view.findViewById(R.id.telorList);
        lv1 = view.findViewById(R.id.rcyclr);
        //telornam=view.findViewById(R.id.tlr1);
        lv1.setHasFixedSize(true);
        telornam = view.findViewById(R.id.telorname);
        lv1.setLayoutManager(new LinearLayoutManager(getActivity()));
        img2 = view.findViewById(R.id.updateButton1);
        print = view.findViewById(R.id.printButton);
        nodata = view.findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //createPDF();
                CreateMessage();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF();
            }
        });
        //     lv = view.findViewById(R.id.telorList);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getTelorList();

            }
        });
        getTelorList();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(getActivity(), telorlist[position], Toast.LENGTH_SHORT).show();
                hTelor = telorlist[position];
                lv.setVisibility(View.GONE);
                getHandoverItemlist();
                telornam.setText(hTelor);

            }
        });


    }
    private void CreateMessage()
    {
        String message="";
        for (int i = 0; i < handOverlist1.size(); i++) {
            String CatalogName="",Design="",SerialNo="",PageNo="",Unit="",qty="",Price="";

            CatalogName=(handOverlist1.get(i).getCatalogName().toString());
            Design=(handOverlist1.get(i).getDesign().toString());
            SerialNo=(handOverlist1.get(i).getSerialNo().toString());
            PageNo=(String.valueOf(handOverlist1.get(i).getPageNo()));
            Unit=(handOverlist1.get(i).getUnit().toString());
            qty=(String.valueOf(handOverlist1.get(i).getAQty()));
            Price=(String.valueOf(handOverlist1.get(i).getPrice2()));

            message =message+hTelor  + "\nCatalog=" + CatalogName + "\nDesign=" + Design+"\nSerialNo="+SerialNo+ "\nPageNo=" + PageNo + "\nUnit=" + Unit+"\nQty="+qty;



        }
        shareMessage(message);
    }
    private void shareMessage(String message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));

    }
    private void createPDF() {

        Document doc = new Document();

        PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2, 2});
        table.getDefaultCell().

                setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell("Catalog Name");
        table.addCell("Design");
        table.addCell("SerialNo");
        table.addCell("PageNo");
        table.addCell("Unit");
        table.addCell("Aqty");
        // table.addCell("ReceivedBy");
        table.addCell("Price");
        //   table.addCell("Price");
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
                i < handOverlist1.size(); i++)


        {

            table.addCell(handOverlist1.get(i).getCatalogName().toString());
            table.addCell(handOverlist1.get(i).getDesign().toString());
            table.addCell(handOverlist1.get(i).getSerialNo().toString());
            table.addCell(String.valueOf(handOverlist1.get(i).getPageNo()));
            table.addCell(handOverlist1.get(i).getUnit().toString());
            table.addCell(String.valueOf(handOverlist1.get(i).getAQty()));
            table.addCell(String.valueOf(handOverlist1.get(i).getPrice2()));


        }

        try

        {
            // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            String path = getContext().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

            File dir = new File(String.valueOf(path));
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "HandoverItemList.pdf");

            FileOutputStream fOut = new FileOutputStream(file);


            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(hTelor);


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
            HeaderFooter pdfFooter = new HeaderFooter();
            doc.newPage();

            Toast.makeText(getContext(), "Created...", Toast.LENGTH_LONG).show();


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



        pdfOpenintent = new Intent(getContext(), PdfViewActivity.class);
        pdfOpenintent = pdfOpenintent.putExtra("PDFName","/HandoverItemList.pdf");
        startActivity( pdfOpenintent);
        //  openPdf();

    }

    void openPdf() {



        File file = new File(getContext().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(),
                "/HandoverItemList.pdf");
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        try {
            startActivity(Intent.createChooser(pdfOpenintent, "HandoverItemList.pdf"));
        } catch (ActivityNotFoundException e) {

        }

//        try {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                File file = new File(getContext().getFilesDir() + "/HandoverItemList.pdf");
//                Uri path = Uri.fromFile(file);
//
//                pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//
//                try {
//                    // getContext().startService(Intent.createChooser(pdfOpenintent, "ReceivedItemList.pdf"));
//                    getContext().startActivity(Intent.createChooser(pdfOpenintent, "HandoverItemList.pdf"));
//                    Log.e("IR", "No exception");
//                    Toast.makeText(getContext(),
//                            "Available to View PDF", Toast.LENGTH_SHORT).show();
//                } catch (ActivityNotFoundException e) {
//                    Log.e("IR", "error: " + e.getMessage());
//                    Toast.makeText(getContext(),
//                            "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                File file = new File(getContext().getFilesDir(),
//                        "/ReceivedItemList.pdf");
//
//
//
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    getActivity().startActivity(Intent.createChooser(pdfOpenintent, "HandoverItemList.pdf"));
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }


    private void getHandoverItemlist() {


        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GetHandoverItemlist().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token, hTelor);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class GetHandoverItemlist extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String telorname = params[1];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "HandOverItemlistGet/" + telorname.trim());
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Length", "0");
                builder.addHeader("Authorization", "Bearer " + accesstoken);


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {
            try {

                if (result.isEmpty()) {
                    progressDialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);

                } else {

                    Gson gson = new Gson();
                    listType = new TypeToken<List<HandOverModel>>() {
                    }.getType();

                    handOverlist1 = new Gson().fromJson(result, listType);
                    catalogname = new String[handOverlist1.size()];
                    for (int i = 0; i < handOverlist1.size(); i++) {
                        catalogname[i] = String.valueOf(handOverlist1.get(i).getCatalogName());

                    }
                    data = new String[handOverlist1.size()];
                    for (int i = 0; i < handOverlist1.size(); i++) {
                        data[i] = String.valueOf(handOverlist1.get(i).toString());

                    }


                    adapterh = new HandOverAdapter(getContext(), handOverlist1,hTelor);

                    lv1.setAdapter(adapterh);


                    r = result.toString().replaceAll("   ", "");

                    progressDialog.dismiss();
                    img2.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    private void getTelorList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GetTelorDetails().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private class GetTelorDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "HandOverTelorlistGet");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Length", "0");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                telorlist = new Gson().fromJson(result, listType);

                handoverListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_button_telorlist, R.id.label, telorlist);
                lv.setAdapter(handoverListAapter);


                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

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



