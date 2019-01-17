package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.HomeActivity;
import com.example.awizom.dotapp.Models.Catelog;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.PdfViewActivity;
import com.example.awizom.dotapp.R;
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
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private AutoCompleteTextView catlogName, design;
    private EditText price;
    private Context mCtx;
    ProgressDialog progressDialog;
    String actualorder;
    ArrayAdapter<String> designadapter;
    private Spinner unitSpinner,materialType;

    private List<CatelogOrderDetailModel> orderitemList;
    DataOrder orderitem;
    CatelogOrderDetailModel catelogOrderDetailModel;
    private String StatusName,filterkey,buttonname,tailorList;
    private EditText editReceivedBy;
    private Button okRecevedButton,canceLOrderButton,neWCancelButton,newHoldButton;
    private Spinner handOvertoNameSpinner, tailorListNameSpinner;
    private String handOverToListSpinnerData[] = {"Telor", "Sofa Karigar", "Self Customer", "Wallpaper fitter"};
    String orderItemId;
    private EditText editElight, editRoman, editAplot, ElightPrice, RomanPrice, APlotPrice;
    private TextView elight, roman, aPlat, elightPrice, romanPrice, aPlotPrice, totalAmount;
    private LinearLayout elightLayout,elightPValueLayout;
    private ImageButton sendButton,printButton;
    String CatalogName="",Design="",SerialNo="",PageNo="",Unit="",qty="",Price="",message="",roomName;
    private Intent pdfOpenintent;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int curposition;
    CatelogOrderDetailModel currentitem;

    public OrderItemAdapter(Context mCtx, List<CatelogOrderDetailModel> orderitemList, String actualorder,
                            String filterkey, String StatusName, String buttonname,String tailorList,String roomName) {
        this.mCtx = mCtx;
        this.orderitemList = orderitemList;
        this.actualorder = actualorder;
        this.StatusName=StatusName;
        this.filterkey=filterkey;
        this.tailorList=tailorList;
        this.buttonname=buttonname;
        this.roomName = roomName;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_item_layout, null);
        return new OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        CatelogOrderDetailModel order = orderitemList.get(position);
        try {
            //holder.textViewPINo.setText("PINo \n"+Integer.toString( order.getPINo()));
            holder.serialNo.setText("SNO\n" + order.getSerialNo());
            holder.catlogName.setText("Catalog\n" + order.getCatalogName());
            holder.design.setText("Design\n" + order.getDesign());
            holder.pageNo.setText("PageNo\n" + Integer.toString(order.getPageNo()));
            holder.price.setText("MRP\n" +order.getPrice());

            holder.MaterialType.setText("Material\n" + order.getMaterialType());
            holder.Price2.setText("Price\n" + order.getPrice2());
            holder.Qty.setText("Qty\n" + Double.toString(order.getQty()));
            holder.AQty.setText("AQty\n" + Double.toString(order.getAQty()));
            holder.unit.setText("Unit\n" + order.getOrderUnit());

            holder.elight.setText( "Elight\n" + "Q. " +Double.toString(order.getElight())+"P. " + Double.toString(order.getElightPrice()));
            holder.aplot.setText( "APlot\n" + "Q. " +Double.toString(order.getAPlat())+"P. " + Double.toString(order.getAPlatPrice()));
            holder.roman.setText("Roman\n"+"SF. " +Double.toString(order.getRoman()) +"P. " + Double.toString(order.getRomanPrice()));

            holder.elightPrice.setText("P. " + Double.toString(order.getElightPrice()));
            holder.elightAplot.setText("P. " + Double.toString(order.getAPlatPrice()));
            holder.elightRoman.setText("P. " + Double.toString(order.getRomanPrice()));



            if(!order.getMaterialType().equals("Curtain")){
                elightPValueLayout.setVisibility(View.GONE);
            }



            if (actualorder.equals("ActualOrder")) {
                holder.AQty.setVisibility(View.VISIBLE);
                holder.Qty.setVisibility(View.GONE);
            }
            if(roomName.equals("blank"))
            {
                if (filterkey.equals("PandingToPlaceOrder")||filterkey.equals("Hold") )
                {
                    if(!order.isOrderPlaced())
                    {
                        holder.buttonStatus.setVisibility( View.VISIBLE );
                        holder.buttonStatus.setText( buttonname );
                    }

                }
                else if(filterkey.equals("PandingToReceiveMaterial"))
                {
                    if(order.isOrderPlaced()&&!order.isMaterialReceived())
                    {
                        holder.buttonStatus.setVisibility( View.VISIBLE );
                        holder.buttonStatus.setText( buttonname );
                    }
                }
                else if(filterkey.equals("PandingToHandOverTo")) {

                    if(order.isMaterialReceived()&&order.isOrderPlaced()&&order.getHandOverTo().equals( "" ))
                    {
                        holder.buttonStatus.setVisibility( View.VISIBLE );
                        holder.buttonStatus.setText( buttonname );
                    }

                }
                else if(filterkey.equals("PandingToReceivedFromTelor")) {

                    if(order.getHandOverTo().equals( "Telor" )&&order.isMaterialReceived()&&order.isOrderPlaced()&& !order.isReceivedFromTalor())
                    {
                        holder.buttonStatus.setVisibility( View.VISIBLE );
                        holder.buttonStatus.setText( buttonname );
                    }

                }
            }



        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return orderitemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        AlertDialog.Builder alert;
        private Context mCtx;
        Button buttonStatus;
        TextView catlogName, serialNo, design, pageNo, price, unit,QTy,price2,aQty;
        TextView OrderItemID, MaterialType, Price2, Qty, AQty,elight,aplot,roman,elightPrice,elightAplot,elightRoman;


        //we are storing all the products in a list
        private List<CatelogOrderDetailModel> orderitemList;


        public OrderItemViewHolder(View itemView, Context mCtx, List<CatelogOrderDetailModel> orderitemList) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mSwipeRefreshLayout = itemView.findViewById(R.id.swipeRefreshLayout);


            // CatelogOrderDetailModel catelogOrderDetailModel = new CatelogOrderDetailModel();
            serialNo = itemView.findViewById(R.id.sno);
            catlogName = itemView.findViewById(R.id.cName);
            design = itemView.findViewById(R.id.c_design);
            pageNo = itemView.findViewById(R.id.c_pageNo);
            price = itemView.findViewById(R.id.c_pricec);

            buttonStatus = itemView.findViewById(R.id.buttonstatus);
            buttonStatus.setOnClickListener(this);


            MaterialType = itemView.findViewById(R.id.materialtype);
            Price2 = itemView.findViewById(R.id.price2);
            Qty = itemView.findViewById(R.id.qty);
            AQty = itemView.findViewById(R.id.aQty);
            unit = itemView.findViewById(R.id.unit);

            elight = itemView.findViewById(R.id.elight);
            aplot = itemView.findViewById(R.id.aplot);
            roman = itemView.findViewById(R.id.roman);
            elightPrice = itemView.findViewById(R.id.elightPrice);
            elightAplot = itemView.findViewById(R.id.elightAplot);
            elightRoman = itemView.findViewById(R.id.elightRoman);
            elightPValueLayout = itemView.findViewById(R.id.b0);
            sendButton = itemView.findViewById(R.id.sendButton);
            printButton=itemView.findViewById(R.id.printButton);
            neWCancelButton = itemView.findViewById(R.id.newCancelButton);
            neWCancelButton.setOnClickListener(this);
            newHoldButton = itemView.findViewById(R.id.NewholdButton);
            newHoldButton.setOnClickListener(this);
            printButton.setOnClickListener(this);
//            sendButton.setVisibility(View.GONE);

            if(!roomName.equals("blank"))
            {
                buttonStatus.setVisibility(View.GONE);
            }
            else {
                if(filterkey.equals("Hold")){

                    neWCancelButton.setVisibility(View.VISIBLE);
                }
                if(filterkey.equals("PandingToPlaceOrder")){

                    newHoldButton.setVisibility(View.VISIBLE);
                }
            }

            sendButton.setOnClickListener(this);


        }

        @Override
        public void onClick(final View v) {
            int position = getAdapterPosition();
            curposition=getAdapterPosition();
            CatelogOrderDetailModel orderitem = this.orderitemList.get(position);
            currentitem=this.orderitemList.get(position);
            message =  "\nCatalog = " + orderitem.getCatalogName()+"\nS.No. = " + orderitem.getSerialNo() +
                    "\nDesign = " + orderitem.getDesign()+"\nPageNo = " + Integer.toString(orderitem.getPageNo()) + "\nMRP = " +orderitem.getPrice()+"\nMaterial = " + orderitem.getMaterialType()
                    +"\nPrice = " + orderitem.getPrice2() +"\nQty = " + Double.toString(orderitem.getQty()) + "\nAQty = " + Double.toString(orderitem.getAQty())
                    + "\nUnit = " + orderitem.getOrderUnit() + "\nElight" + "Q. " +Double.toString(orderitem.getElight())+"P. " + Double.toString(orderitem.getElightPrice())
                    + "\nAPlot" + "Q. " +Double.toString(orderitem.getAPlat())+"P. " + Double.toString(orderitem.getAPlatPrice())
                    +"\nRoman"+"SF. " +Double.toString(orderitem.getRoman()) +"P. " + Double.toString(orderitem.getRomanPrice())
                    +"P. " + Double.toString(orderitem.getElightPrice()) + "P. " + Double.toString(orderitem.getAPlatPrice())
                    +"P. " + Double.toString(orderitem.getRomanPrice());


            orderItemId=String.valueOf(orderitem.getOrderItemID());
            if (v.getId() == buttonStatus.getId()) {

                if( filterkey.equals("PandingToPlaceOrder")||filterkey.equals("PandingToReceiveMaterial")||filterkey.equals("Hold"))
                { android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setTitle("Do you want to change the status");
                    alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {


                            placeOrderPost(message);
                        }
                    });
                    alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    alertbox.show();

                }
                else if(filterkey.equals("PandingToHandOverTo"))
                {
                    android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setTitle("Do you want to change the status");
                    alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            showDailogForHandOverTo(v,message);
                        }
                    });
                    alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    alertbox.show();

                }
                else if (filterkey.equals("PandingToReceivedFromTelor"))
                {
                    android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setTitle("Do you want to change the status");
                    alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            showdailogForreceivedBy(v,message);
                        }
                    });
                    alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    alertbox.show();
                }

            } if (v.getId() == neWCancelButton.getId()) {
                android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("Do you want to change the status");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        cancelPlaceOrderPost(message);
                    }
                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                alertbox.show();

            } if (v.getId() == newHoldButton.getId()) {
                android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("Do you want to change the status");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                       holdPlaceOrderPost(message);
                    }
                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                alertbox.show();

            }if (v.getId() == sendButton.getId()) {

                message = "\nCatalog = " + orderitem.getCatalogName()+
                        "\nS.No. = " + orderitem.getSerialNo() +
                        "\nDesign = " + orderitem.getDesign()+
                        "\nPageNo = " + Integer.toString(orderitem.getPageNo())+
                        "\nQty = " + Double.toString(orderitem.getAQty()) +
                        "\nUnit = " + orderitem.getOrderUnit()+
                        "\nRegards=" +SharedPrefManager.getInstance(mCtx).getUser().getUserName();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                mCtx.startActivity(Intent.createChooser(shareIntent, "SHARE"));
            }
            if (v.getId() == printButton.getId())
            {


                Document doc = new Document();

                PdfPTable table = new PdfPTable(new float[]{2,2, 2, 2, 2, 2,2});
                table.getDefaultCell().

                        setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell("Catalog Name");
                table.addCell("Design");
                table.addCell("SerialNo");
                table.addCell("PageNo");
                table.addCell("Qty");
                table.addCell("Unit");
                table.addCell( "UserName" );
                // table.addCell("ReceivedBy");
//                table.addCell("Regards");
                //   table.addCell("Price");
                table.setHeaderRows(1);
                PdfPCell[] cells = table.getRow(0).getCells();
                for (
                        int j = 0;
                        j < cells.length; j++)

                {
                    cells[j].setBackgroundColor(BaseColor.GRAY);
                }




                {

                    table.addCell(orderitem.getCatalogName().toString());
                    table.addCell(orderitem.getDesign());
                    table.addCell( orderitem.getSerialNo());
                    table.addCell(Integer.toString(orderitem.getPageNo()));
                    table.addCell(Double.toString(orderitem.getAQty()));
                    table.addCell(orderitem.getOrderUnit());
                    table.addCell(SharedPrefManager.getInstance(mCtx).getUser().getUserName());


                }

                try

                {
                    // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

                    String path = mCtx.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

                    File dir = new File(String.valueOf(path));
                    if (!dir.exists())
                        dir.mkdirs();

                    Log.d("PDFCreator", "PDF Path: " + path);

                    File file = new File(dir, "OrderItemAdapter.pdf");

                    FileOutputStream fOut = new FileOutputStream(file);


                    PdfWriter.getInstance(doc, fOut);

                    //open the document
                    doc.open();

                    Paragraph p1 = new Paragraph("Regards by :-" + SharedPrefManager.getInstance(mCtx).getUser().getUserName() + " " + filterkey);


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
                    OrderItemAdapter.HeaderFooter pdfFooter = new OrderItemAdapter.HeaderFooter();
                    doc.newPage();

                    Toast.makeText(mCtx, "Created...", Toast.LENGTH_LONG).show();


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

                pdfOpenintent = new Intent();
                pdfOpenintent = new Intent(mCtx, PdfViewActivity.class);
                pdfOpenintent = pdfOpenintent.putExtra("PDFName","/OrderItemAdapter.pdf");
                pdfOpenintent = pdfOpenintent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK ) ;
                mCtx.startActivity( pdfOpenintent);
            }
        }

        private void showdailogForreceivedBy(View v,String message) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
            final View dialogView = inflater.inflate(R.layout.alert_for_received_tailor, null);
            dialogBuilder.setView(dialogView);

            editReceivedBy = dialogView.findViewById(R.id.receivedEditText);
            okRecevedButton = dialogView.findViewById(R.id.okReceivedButton);
            canceLOrderButton = dialogView.findViewById(R.id.cancelOrderButton);

            dialogBuilder.setTitle("Receibed By List");
            final android.support.v7.app.AlertDialog b = dialogBuilder.create();
            b.show();
          //  shareApp(mCtx,message);
            okRecevedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!editReceivedBy.getText().toString().isEmpty()) {
                            receiFromeTailorToListPost();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    b.dismiss();
                }
            });


        }
        private void showDailogForHandOverTo(View v, String message) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
            final View dialogView = inflater.inflate(R.layout.handover_to_show_telo, null);
            dialogBuilder.setView(dialogView);

            handOvertoNameSpinner = dialogView.findViewById(R.id.handOverToNameSpinner);
            tailorListNameSpinner = dialogView.findViewById(R.id.tailorListSpinner);
            final Button buttonOk = dialogView.findViewById(R.id.buttonOk);
            final LinearLayout l2 = dialogView.findViewById(R.id.l2);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, handOverToListSpinnerData);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            handOvertoNameSpinner.setAdapter(spinnerArrayAdapter);
            progressDialog = new ProgressDialog(mCtx);

            String[] items =tailorList.split(",");
            ArrayAdapter<String> spinneArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, items);
            spinneArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            tailorListNameSpinner.setAdapter(spinneArrayAdapter);

            dialogBuilder.setTitle("Hand Over List");
            final android.support.v7.app.AlertDialog b = dialogBuilder.create();
            b.show();

            if(!handOvertoNameSpinner.getSelectedItem().equals("Telor")){
                tailorListNameSpinner.setVisibility(View.GONE);
            }
           // shareApp(mCtx,message);
//        handOvertoNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                tailorListNameSpinner.setVisibility(View.VISIBLE);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (String.valueOf(tailorListNameSpinner.getSelectedItem()).trim().length() > 0) {
                        try {
                            if(!handOvertoNameSpinner.getSelectedItem().equals("")) {
                                handOverToListPost(); // handOverToListPost();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        b.dismiss();
                    }


                }


            });


        }

        @Override
        public boolean onLongClick(View v) {

            int position = getAdapterPosition();
            CatelogOrderDetailModel orderitem = this.orderitemList.get(position);

            if (v.getId() == itemView.getId()) {

                if(!filterkey.equals("PandingToReceiveMaterial") &&
                        !filterkey.equals("PandingToHandOverTo")&& !filterkey.equals("PandingToReceivedFromTelor") &&
                        !filterkey.equals("Hold")&& !filterkey.equals("Dispatch")) {
                    initViewByAlertdailog(orderitem, v);
                }else if(filterkey.equals("PandingToPlaceOrder")){
                    initViewByAlertdailog(orderitem, v);
                }
                try {

                } catch (Exception E) {
                    E.printStackTrace();
                }

            }
            return true;
        }

        private void initViewByAlertdailog(CatelogOrderDetailModel orderitem, View v) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
            final View dialogView = inflater.inflate(R.layout.add_dailog_layout, null);
            dialogBuilder.setView(dialogView);

            final String OrderItemID = String.valueOf(orderitem.getOrderItemID());
            final String orderRoomId = String.valueOf(orderitem.getOrderRoomID());
            final String catalogID = String.valueOf(orderitem.getCatalogID());
            final String QTY = String.valueOf(orderitem.getQty());
            final String AQTY = String.valueOf(orderitem.getAQty());
            final String orderRoomName = orderitem.getRoomName();
            final String orderID = String.valueOf(orderitem.getOrderID());
            final EditText s_no = dialogView.findViewById(R.id.sNo);
            catlogName = dialogView.findViewById(R.id.catlogName);
            design = dialogView.findViewById(R.id.design);
             pageNo = dialogView.findViewById(R.id.pageNo);
            price = dialogView.findViewById(R.id.price);
            price2 = dialogView.findViewById(R.id.price2);
            materialType = dialogView.findViewById(R.id.materialType);
             QTy = dialogView.findViewById(R.id.qTy);
             aQty = dialogView.findViewById(R.id.aQty);
            unitSpinner = dialogView.findViewById(R.id.unit);

//            editElight = dialogView.findViewById(R.id.editElight);
//            editRoman = dialogView.findViewById(R.id.editRoman);
//            editAplot = dialogView.findViewById(R.id.editAplot);
//            ElightPrice = dialogView.findViewById(R.id.ElightPrice);
//            RomanPrice = dialogView.findViewById(R.id.RomanPrice);
//            APlotPrice = dialogView.findViewById(R.id.APlotPrice);



            elight = dialogView.findViewById(R.id.ediElight);
            roman = dialogView.findViewById(R.id.ediRoman);
            aPlat = dialogView.findViewById(R.id.ediAplot);
            elightPrice = dialogView.findViewById(R.id.elighPrice);
            romanPrice = dialogView.findViewById(R.id.romaPrice);
            aPlotPrice = dialogView.findViewById(R.id.aPlotPrice);
            elightLayout = dialogView.findViewById(R.id.elightLinearLayout);
            elightPValueLayout = dialogView.findViewById(R.id.b0);
            elightLayout.setVisibility(View.GONE);



            if (actualorder.equals("ActualOrder")) {
                QTy.setVisibility(dialogView.GONE);
                aQty.setVisibility(dialogView.VISIBLE);
            }


            s_no.setText(orderitem.getSerialNo());
            catlogName.setText(orderitem.getCatalogName());
            design.setText(orderitem.getDesign());
            price2.setText(Double.toString(orderitem.getPrice2()));
            pageNo.setText(Integer.toString(orderitem.getPageNo()));
            price.setText(Double.toString(orderitem.getPrice()));


            //Price2.setText(Integer.toString(order.getPrice2()));
            QTy.setText(Double.toString(orderitem.getQty()));
            aQty.setText(Double.toString(orderitem.getAQty()));
            elight.setText(Double.toString(orderitem.getElight()));
            roman.setText(Double.toString(orderitem.getRoman()));
            aPlat.setText(Double.toString(orderitem.getAPlat()));
            elightPrice.setText(Double.toString(orderitem.getElightPrice()));
            aPlotPrice.setText(Double.toString(orderitem.getAPlatPrice()));
            romanPrice.setText(Double.toString(orderitem.getRomanPrice()));



            if (orderitem.getUnit().trim().length() > 0 || orderitem.getMaterialType().trim().length() > 0  ) {
                unitSpinner.setSelection(((ArrayAdapter<String>) unitSpinner.getAdapter()).getPosition(orderitem.getOrderUnit()));
                materialType.setSelection(((ArrayAdapter<String>) materialType.getAdapter()).getPosition(orderitem.getMaterialType().toString()));
                if(!materialType.getSelectedItem().toString().equals("Curtain")){
                    elightLayout.setVisibility(View.GONE);
                }else if(materialType.getSelectedItem().toString().equals("Curtain")){
                    elightLayout.setVisibility(View.VISIBLE);
                }
            }



            final Button buttonAdd = (Button) dialogView.findViewById(R.id.add);
            final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
            dialogBuilder.setTitle("Edit Order Item");
            final AlertDialog b = dialogBuilder.create();
            b.show();
          /*  catlogName.addTextChangedListener(new TextWatcher() {
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
            });*/


            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validation()) {

                        try {
                    String snumber = s_no.getText().toString();
                    String catlogname = catlogName.getText().toString();
                    String desiGn = design.getText().toString();
                    String page_no = pageNo.getText().toString();
                    String priCe = price.getText().toString();
                    String priCe2 = price2.getText().toString();
                    String qTy = QTy.getText().toString();
                    String aqty = aQty.getText().toString();
                    String materialtype = materialType.getSelectedItem().toString();
                    String unIt = unitSpinner.getSelectedItem().toString();

                    String eligt = elight.getText().toString();
                    String romn = roman.getText().toString();
                    String aplot = aPlat.getText().toString();
                    String elightprice = elightPrice.getText().toString();
                    String romanprice = romanPrice.getText().toString();
                    String aplotprice = aPlotPrice.getText().toString();;


                    if (actualorder.equals("ActualOrder")) {
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        new OrderItemAdapter.POSTOrder().execute(OrderItemID, materialtype, priCe2, QTY, aqty, unIt, orderRoomId, catlogname, snumber, desiGn, page_no, priCe, unIt, catalogID, "", orderID.trim()
                                ,eligt,romn,aplot,elightprice,romanprice,aplotprice , SharedPrefManager.getInstance(mCtx).getUser().access_token);

                    } else {
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        new OrderItemAdapter.POSTOrder().execute(OrderItemID, materialtype, priCe2, qTy, qTy, unIt, orderRoomId, catlogname, snumber, desiGn, page_no, priCe, unIt, catalogID, "",
                                orderID.trim()   ,eligt,romn,aplot,elightprice,romanprice,aplotprice,SharedPrefManager.getInstance(mCtx).getUser().access_token);

                    }


                    b.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        private boolean validation() {

            boolean status=true;
            if ((catlogName.getText().toString().isEmpty())) {
                catlogName.setError("Catalog name is required!");
                status=false;
            } else if (design.getText().toString().isEmpty()) {
                design.setError("Design is required!");status=false;
            }
            else if (aQty.getText().toString().isEmpty()) {
                aQty.setText( "1" );status=true;
            }
            else if (pageNo.getText().toString().isEmpty()) {
                pageNo.setText( "1" );status=true;
            }
            else if (QTy.getText().toString().isEmpty()) {
                QTy.setText( "1" );status=true;
            }
            else if (price.getText().toString().isEmpty()) {
                price.setText( "0" );status=true;
            }
            else if (price2.getText().toString().isEmpty()) {
                price2.setText( "0" );status=true;
            }
            else if (elight.getText().toString().isEmpty()) {
                elight.setText( "0" );status=true;
            } else if (roman.getText().toString().isEmpty()) {
                roman.setText( "0" );status=true;
            } else if ((aPlat.getText().toString().isEmpty())) {
                aPlat.setText( "0" );status=true;
            }
            else if ((elightPrice.getText().toString().isEmpty())) {
                elightPrice.setText( "0" );status=true;
            }else if ((romanPrice.getText().toString().isEmpty())) {
                romanPrice.setText( "0" );status=true;
            }else if ((aPlotPrice.getText().toString().isEmpty())) {
                aPlotPrice.setText( "0" );status=true;
            }



            return status;

        }
    }

    public static void shareApp(Context context, String message)
    {
        //  final String appPackageName = context.getPackageName();
//    Intent sendIntent = new Intent();
//    sendIntent.setAction(Intent.ACTION_SEND);
//    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    sendIntent.putExtra(Intent.EXTRA_TEXT, message );
//    sendIntent.setType("text/plain");
//    context.startActivity(sendIntent);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(shareIntent, "SHARE"));

    }

    private void getCatalogDesignSingle() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new getCatalogDesign().execute(catlogName.getText().toString(), design.getText().toString(), SharedPrefManager.getInstance(mCtx).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<Catelog>() {
                }.getType();
                Catelog catelogdesign = new Gson().fromJson(result, listType);
                if (catelogdesign != null) {
                    price.setText(String.valueOf(catelogdesign.getPrice()));
                    if (catelogdesign.getUnit().trim().length() > 0) {
                        unitSpinner.setSelection(((ArrayAdapter<String>) unitSpinner.getAdapter()).getPosition(catelogdesign.getUnit().toString()));
                        materialType.setSelection(((ArrayAdapter<String>) materialType.getAdapter()).getPosition(catelogdesign.getMaterialType().toString()));

                    }
                }
                //Getting the instance of AutoCompleteTextView
            }
        }
    }

    private void getDesignList() {
        try {
             progressDialog.setMessage("loading...");
             progressDialog.show();
            new getDesign().execute(catlogName.getText().toString(), SharedPrefManager.getInstance(mCtx).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
             progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                String[] designlist = new Gson().fromJson(result, listType);
                designadapter = new ArrayAdapter<String>(mCtx, android.R.layout.select_dialog_item, designlist);
                design.setThreshold(1);//will start working from first character
                design.setAdapter(designadapter);//setting the adapter data into the AutoCompleteTextView

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

            String elight = params[16];
            String roman = params[17];
            String aPlat = params[18];
            String elightprice = params[19];
            String romanprice = params[20];
            String aPlatprice = params[21];
            String accesstoken = params[22];

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

                parameters.add("Elight", elight);
                parameters.add("Roman", roman);
                parameters.add("APlat", aPlat);
                parameters.add("ElightPrice", elightprice);
                parameters.add("RomanPrice", romanprice);
                parameters.add("APlatPrice", aPlatprice);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                 progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx.getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                   //   getMyOrder();

                }
                 progressDialog.dismiss();
            }
        }
    }

    private void cancelPlaceOrderPost( String message) {
       // shareApp(mCtx,message);
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostPlaceOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,"Cancel",orderItemId);

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }
    private void holdPlaceOrderPost( String message) {
        // shareApp(mCtx,message);
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new HoldPostPlaceOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,"Hold",orderItemId);

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }
    private class HoldPostPlaceOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String orderItemId = params[2];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", "0");
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", orderItemId);
                parameters.add("StatusName", statusname);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
//                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    progressDialog.dismiss();
                    orderitemList.remove( curposition );
                    notifyItemRemoved(curposition  );
//                    Intent intent = new Intent(mCtx, HomeActivity.class);
//                    intent = intent.putExtra("Message",message);
//                    intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mCtx.startActivity(intent);

                }
                //       progressDialog.dismiss();
            }
        }
    }
    private void placeOrderPost( String message) {
       // shareApp(mCtx,message);
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostPlaceOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,StatusName,orderItemId);

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostPlaceOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String orderItemId = params[2];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", "0");
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", orderItemId);
                parameters.add("StatusName", statusname);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                //      progressDialog.dismiss();
//                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {

                    progressDialog.dismiss();
                    orderitemList.remove( curposition );
                    notifyItemRemoved(curposition  );

//                        Intent intent = new Intent(mCtx, HomeActivity.class);
//                        intent = intent.putExtra("Message",message);
//                        intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mCtx.startActivity(intent);

                }
                //       progressDialog.dismiss();
            }
        }
    }

    private void receiFromeTailorToListPost() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new receivedFrometailorToListPost().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, StatusName,orderItemId, editReceivedBy.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class receivedFrometailorToListPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String orderItemId = params[2];
            String name = params[3];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", "0");
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", orderItemId);
                parameters.add("StatusName", statusname);
                parameters.add("ReceivedBy", name);


                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "There is no data available", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    progressDialog.dismiss();
                    orderitemList.remove( curposition );
                    notifyItemRemoved(curposition  );
//                    Intent intent = new Intent(mCtx, HomeActivity.class);
//                    intent = intent.putExtra("Message",message);
//                    intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mCtx.startActivity(intent);
                }
            }
        }

    }


    private void handOverToListPost() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostHandOverToList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,StatusName,orderItemId,String.valueOf(handOvertoNameSpinner.getSelectedItem()), String.valueOf(tailorListNameSpinner.getSelectedItem()));
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostHandOverToList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String orderItemId = params[2];
            String handoverto = params[3];
            String tailorname = params[4];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", "0");
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", orderItemId);
                parameters.add("StatusName", statusname);
                parameters.add("HandOverTo", handoverto);
                parameters.add("TelorName", tailorname);


                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "There is no data available", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    progressDialog.dismiss();
                    orderitemList.remove( curposition );
                    notifyItemRemoved(curposition  );
//                    Intent intent = new Intent(mCtx, HomeActivity.class);
//                    intent = intent.putExtra("Message",message);
//                    intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mCtx.startActivity(intent);
                }
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
