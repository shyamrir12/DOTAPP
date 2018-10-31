package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Adapters.CustomerListAdapter;
import com.example.awizom.dotapp.Adapters.HandOverAdapter;
import com.example.awizom.dotapp.Adapters.TelorListAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.TelorModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ReceivedTelorlist extends Fragment {

    ProgressDialog progressDialog;
    ListView lv;
    // ImageButton img2,img3;
    RecyclerView lv1;
    // List <TelorModel> list1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Handler handler = new Handler();
    Runnable refresh;
    private Button add, cancel;
    private EditText t_name, old_t_name;
    ArrayAdapter<String> telorListAapter;
    String[] telorlist;
    private String telornamet, telorname_old, hTelor;
    List<HandOverModel> list1;
    private String[] catalogname;
    HandOverAdapter adapterh;
    private String r, str;
    Type listType;

    HandOverModel handover = new HandOverModel();

    private String title;
    PdfPCell cell;
    Bundle gt;
    View rootView;
    TextView telornam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.received_telor_list, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        getActivity().setTitle("Telor");

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while loading telors");
        lv = view.findViewById(R.id.telorList);
        lv1 = view.findViewById(R.id.rcyclr);
        lv1.setHasFixedSize(true);
        lv1.setLayoutManager(new LinearLayoutManager(getActivity()));

        telornam = view.findViewById(R.id.telorname);


        //  img2=view.findViewById(R.id.updateButton1);
        //  img3=view.findViewById(R.id.updateButton2);

//        title = getArguments().getString("NAME_KEY").toString();
        //img2.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //   public void onClick(View v) {

        ///       createPDF();

        ///     }
        //   });

//        img3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openPdf();
//            }
//        });

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
                lv.setVisibility(View.GONE);
                hTelor = telorlist[position];
                //     Toast.makeText(getActivity(), telorlist[position], Toast.LENGTH_SHORT).show();

                getreceivedTelorList();
                telornam.setText(hTelor);
            }
        });
    }

//    void openPdf()
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
//
//        File file = new File(path, "demo.pdf");
//
//        intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
//        startActivity(intent);
//    }
////    private void createPDF() {
//
//        Document doc = new Document();
//
//        PdfPTable table = new PdfPTable(new float[]{2, 1, 2, 2, 1, 2});
//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell("Catalog Name");
//        table.addCell("Design");
//        table.addCell("SerialNo");
//        table.addCell("Price");
//        table.addCell("ReceivedBy");
//        table.addCell("Unit");
//        //   table.addCell("Price");
//        table.setHeaderRows(1);
//        PdfPCell[] cells = table.getRow(0).getCells();
//        for (int j = 0; j < cells.length; j++) {
//            cells[j].setBackgroundColor(BaseColor.GRAY);
//        }
//
//
//
//
//
//        for (int i = 1; i < 5; i++) {
//            table.addCell(catalogname.toString() + i);
//            table.addCell("Design:" + i);
//            table.addCell("SerialNo:" + i);
//            table.addCell("Price:" + i);
//            table.addCell("ReceivedBy:" + i);
//            table.addCell("Unit:" + i);
//        }
//
//        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
//
//            File dir = new File(path);
//            if (!dir.exists())
//                dir.mkdirs();
//
//            Log.d("PDFCreator", "PDF Path: " + path);
//
//            File file = new File(dir, "demo.pdf");
//            FileOutputStream fOut = new FileOutputStream(file);
//
//            PdfWriter.getInstance(doc, fOut);
//
//            //open the document
//            doc.open();
//
//            Paragraph p1 = new Paragraph(hTelor);
//
//            /* You can also SET FONT and SIZE like this */
//            Font paraFont1 = new Font(Font.FontFamily.HELVETICA, 50, Color.GREEN);
//            p1.setAlignment(Paragraph.ALIGN_CENTER);
//
//            p1.setFont(paraFont1);
//
//            doc.add(p1);
//
//            doc.add(table);
//            //     t=t_name.getText();
//            /* Create Paragraph and Set Font */
//            Paragraph p2 = new Paragraph(r.toString());
//
//            /* Create Set Font and its Size */
//            Font paraFont = new Font(Font.FontFamily.COURIER);
//            paraFont.setSize(16);
//            p2.setAlignment(Paragraph.ALIGN_CENTER);
//            p2.setFont(paraFont);
//
//            //add paragraph to document
//            doc.add(p2);
//
//
//            Paragraph p3 = new Paragraph("This is an example of a simple paragraph");
//
//            /* You can also SET FONT and SIZE like this */
//            Font paraFont3 = new Font(Font.FontFamily.HELVETICA, 14.0f, Color.GREEN);
//            p3.setAlignment(Paragraph.ALIGN_CENTER);
//            p3.setFont(paraFont3);
//
//            doc.add(p3);
//
//            /* Inserting Image in PDF */
////            ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_warning);
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
////            Image myImg = Image.getInstance(stream.toByteArray());
////            myImg.setAlignment(Image.MIDDLE);
////
////            //add image to document
////            doc.add(myImg);
//
//            //set footer
//            Phrase footerText = new Phrase("This is an example of a footer");
//            HeaderFooter pdfFooter = new HeaderFooter();
//            doc.newPage();
//
//            Toast.makeText(getContext(), "Created...", Toast.LENGTH_LONG).show();
//
//
//
//
//
//        } catch (DocumentException de) {
//            Log.e("PDFCreator", "DocumentException:" + de);
//        } catch (IOException e) {
//            Log.e("PDFCreator", "ioException:" + e);
//        } finally {
//            doc.close();
//        }
//
//
//    }

    private void getreceivedTelorList() {

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new GetReceivedTelorDetails().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token, hTelor);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class GetReceivedTelorDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {


            String accesstoken = params[0];
            String telorname = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "ReceivedItemlistGet/" + telorname);
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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {


                Gson gson = new Gson();
                listType = new TypeToken<List<HandOverModel>>() {

                }.getType();

                list1 = new Gson().fromJson(result, listType);
                adapterh = new HandOverAdapter(getContext(), list1);

                lv1.setAdapter(adapterh);


//                catalogname = new String[list1.size()];
//
//                for (int i = 0; i < list1.size(); i++) {
//                    catalogname[i] = String.valueOf(list1.get(i).getCatalogName());
//
//                }


//                r = result.toString().replaceAll("   ", "");

                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);


            }

        }


    }


    private void postTelorList() {
        telornamet = t_name.getText().toString();

        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostTelorDetails().execute(telornamet.trim(), SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class PostTelorDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String telorname = params[0];
            String accesstoken = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "TelorPost/" + telorname + "/" + "blank");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {


                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getContext()
                        , jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    // modifyItem(pos,um);

                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
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
                builder.url(AppConfig.BASE_URL_API + "ReceivedTelorlistGet");
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

                telorListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_button_telorlist, R.id.label, telorlist);
                lv.setAdapter(telorListAapter);

                progressDialog.dismiss();
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