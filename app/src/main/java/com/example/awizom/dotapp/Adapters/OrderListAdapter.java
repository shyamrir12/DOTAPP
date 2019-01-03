package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.ItemListActivity;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.PdfViewActivity;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
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
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private List<DataOrder> orderitemList;
    String filterKey;
    private String valueButtonname;
    private DataOrder orderitem;
    private DataOrder order;
    private String statusName,dailogMessage;
    private String handOverToListSpinnerData[] = {"Telor", "Sofa Karigar", "Self Customer", "Wallpaper fitter"};
    private Spinner handOvertoNameSpinner, tailorListNameSpinner;
    private EditText editReceivedBy;
    private Button okRecevedButton,canceLOrderButton;
    private ImageButton print,share;
    private String message="";
    private Intent pdfOpenintent;
    private HandOverModel orderitemcatlog;

    public OrderListAdapter(Context mCtx, List<DataOrder> orderitemList, String filterKey, String valueButtonname, String statusName) {
        this.mCtx = mCtx;
        this.orderitemList = orderitemList;
        this.filterKey = filterKey;
        this.valueButtonname = valueButtonname;
        this.statusName = statusName;
        this.dailogMessage = dailogMessage;
        progressDialog = new ProgressDialog(mCtx);
        String a = SharedPrefManager.getInstance(mCtx).getUser().access_token;

    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_list, null);
        return new OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        try {
            holder.ordername.setText("Name\n" + order.getCustomerName().trim());
            holder.orderaddress.setText("Address\n " + order.getAddress().trim());
            holder.ordercontact.setText("Mobile\n " + order.getMobile().trim());
            holder.orderdate.setText("Date\n " + order.getOrderDate().split("T")[0].trim());
            holder.orderamount.setText("Advance\n " + Double.toString(order.getAdvance()).trim());
            holder.totalamount.setText("Amount\n " + Double.toString(order.getTotalAmount()).trim());

            if( Double.toString(order.getTotalAmount()).equals("0.0")){
                holder.canceLOrderButton.setVisibility(View.GONE);
            }
//            else if(filterKey.equals("PandingToPlaceOrder")){
//                holder.buttonActualOrder.setVisibility(View.GONE);
//                holder.buttonOrder.setVisibility(View.GONE);
//            }else if(filterKey.equals("PandingToReceiveMaterial")){
//                holder.buttonActualOrder.setVisibility(View.GONE);
//                holder.buttonOrder.setVisibility(View.GONE);
//            }else if(filterKey.equals("PandingToHandOverTo")){
//                holder.buttonActualOrder.setVisibility(View.GONE);
//                holder.buttonOrder.setVisibility(View.GONE);
//            }else if(filterKey.equals("PandingToReceivedFromTelor")){
//                holder.buttonActualOrder.setVisibility(View.GONE);
//                holder.buttonOrder.setVisibility(View.GONE);
//            }else if(filterKey.equals("Hold")){
//                holder.buttonOrder.setVisibility(View.GONE);
//                holder.buttonActualOrder.setVisibility(View.GONE);
//            }else if(filterKey.equals("Dispatch")){
//               holder.buttonOrder.setVisibility(View.GONE);
//               holder.buttonActualOrder.setVisibility(View.GONE);
//           }else{
//               holder.buttonOrder.setVisibility(View.VISIBLE);
//               holder.buttonActualOrder.setVisibility(View.VISIBLE);
//           }


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
        String dept;
        private TextView ordername, orderaddress, ordercontact, orderdate, orderamount, totalamount, textviewStatus, status;
        private Button statusOrder, buttonOrder, buttonActualOrder, canceLOrderButton;
        private List<DataOrder> orderitemList;


        public OrderItemViewHolder(View view, Context mCtx, List<DataOrder> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ordername = view.findViewById(R.id.textViewCustomerName);
            //status = view.findViewById(R.id.textViewStatus);
            orderaddress = view.findViewById(R.id.textViewCustomerAddress);
            ordercontact = view.findViewById(R.id.textViewMobile);
            orderdate = view.findViewById(R.id.textViewOrderDate);
            orderamount = view.findViewById(R.id.textViewAdvance);
            totalamount = view.findViewById(R.id.textViewATotalAmount);
            buttonOrder = view.findViewById(R.id.buttonOrder);
            buttonActualOrder = view.findViewById(R.id.buttonActualOrder);
            canceLOrderButton = view.findViewById(R.id.cancelOrderButton);
            buttonOrder.setOnClickListener(this);
            buttonActualOrder.setOnClickListener(this);
            canceLOrderButton.setOnClickListener(this);
            canceLOrderButton.setText(valueButtonname);

            orderitemcatlog = new HandOverModel();
            share = view.findViewById(R.id.share);
            print = view.findViewById(R.id.print);
            share.setOnClickListener(this);
            print.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {
            int position = getAdapterPosition();
            orderitem = this.orderitemList.get(position);
      if (v.getId() == buttonOrder.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                i = i.putExtra("ActualOrder", "");
                i = i.putExtra("FilterKey", filterKey);
                i = i.putExtra("StatusName", statusName);
                i = i.putExtra("ButtonName", valueButtonname);
                mCtx.startActivity(i);
            }
            if (v.getId() == buttonActualOrder.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                i = i.putExtra("ActualOrder", "ActualOrder");
                i = i.putExtra("FilterKey", filterKey);
                i = i.putExtra("StatusName", statusName);
                i = i.putExtra("ButtonName", valueButtonname);
                mCtx.startActivity(i);
            }
            if (v.getId() == canceLOrderButton.getId()) {

                if (filterKey.equals("PandingToHandOverTo"))

                {
                    if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                            (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("HandOver")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                    {


                        Intent intent = new Intent(mCtx, ItemListActivity.class);
                        intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                        intent = intent.putExtra("FilterKey", filterKey);
                        intent = intent.putExtra("StatusName", statusName);
                        intent = intent.putExtra("ButtonName", valueButtonname);
                        intent = intent.putExtra("TailorList", orderitem.getTelorList());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
//                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                        alertbox.setTitle("Do you want to change the status");
//                        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                //showDailogForHandOverTo(v);
//
//                            }
//                        });
//                        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//                        alertbox.show();
                    }
                    else {

                        Toast.makeText(v.getContext(), " Not Permitted ", Toast.LENGTH_SHORT).show();
                        return;}


                } else if (filterKey.equals("PandingToReceivedFromTelor"))

                {
                    if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                            (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Receive")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                    {

                        Intent intent = new Intent(mCtx, ItemListActivity.class);
                        intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                        intent = intent.putExtra("FilterKey", filterKey);
                        intent = intent.putExtra("StatusName", statusName);
                        intent = intent.putExtra("ButtonName", valueButtonname);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
//                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                        alertbox.setTitle("Do you want to change the status");
//                        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                //showdailogForreceivedBy(v);
//
//                            }
//                        });
//                        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//
//                        alertbox.show();
                    }
                    else {

                        Toast.makeText(v.getContext(), " Not Permitted ", Toast.LENGTH_SHORT).show();
                        return;}

                } else if (filterKey.equals("Dispatch")) {
                    if (((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                            (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Dispatch")))) {
//
//                        Intent intent = new Intent(mCtx, ItemListActivity.class);
//                        intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
//                        intent = intent.putExtra("FilterKey", filterKey);
//                        intent = intent.putExtra("StatusName", statusName);
//                        intent = intent.putExtra("ButtonName", valueButtonname);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mCtx.startActivity(intent);

                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                        alertbox.setTitle("Do you want to change the status");
                        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                          dispatchListPost();

                            }
                        });
                        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        alertbox.show();

                    } else {

                        Toast.makeText(v.getContext(), "User is Not Permitted ", Toast.LENGTH_SHORT).show();
                        return;


                    }
                } else if (filterKey.equals("PandingToReceiveMaterial")) {
                    if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                            (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("MaterialReceive")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                    {

                        Intent intent = new Intent(mCtx, ItemListActivity.class);
                        intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                        intent = intent.putExtra("FilterKey", filterKey);
                        intent = intent.putExtra("StatusName", statusName);
                        intent = intent.putExtra("ButtonName", valueButtonname);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
//                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                        alertbox.setTitle("Do you want to change the status");
//                        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                               // cancelOrderListPost();
//
//                            }
//                        });
//                        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//
//                        alertbox.show();
                    }
                    else {

                        Toast.makeText(v.getContext(), " Not Permitted ", Toast.LENGTH_SHORT).show();
                        return;}
                } else if (filterKey.equals("pandingForAdv")) {

                        if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                                (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Advance")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                         {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                            alertbox.setTitle("Do you want to change the status");
                            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancelOrderListPost();

                                }
                            });
                            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                            alertbox.show();


//                             Intent intent = new Intent(mCtx, ItemListActivity.class);
//                             intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
//                             intent = intent.putExtra("FilterKey", filterKey);
//                             intent = intent.putExtra("StatusName", statusName);
//                             intent = intent.putExtra("ButtonName", valueButtonname);
//                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                             mCtx.startActivity(intent);

                        }
                        else {
                            Toast.makeText(v.getContext(), " Not Permitted ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                } else if (filterKey.equals("Hold")) {


                        if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                                (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Hold")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                        {
//                            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                            alertbox.setTitle("Do you want to change the status");
//                            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                  //  cancelOrderListPost();
//
//                                }
//                            });
//
//                            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//
//                                }
//                            });
//
//                            alertbox.show();

                            Intent intent = new Intent(mCtx, ItemListActivity.class);
                            intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                            intent = intent.putExtra("FilterKey", filterKey);
                            intent = intent.putExtra("StatusName", statusName);
                            intent = intent.putExtra("ButtonName", valueButtonname);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mCtx.startActivity(intent);
                        }
                        else {
                            Toast.makeText(v.getContext(), " Not Permitted ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                } else if (filterKey.equals("PandingToPlaceOrder")) {
                        if ((((SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("Admin")) ||
                                (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("PlaceOrder")))) || (SharedPrefManager.getInstance(mCtx).getUser().userRole.contains("User")))
                        {

                            Intent intent = new Intent(mCtx, ItemListActivity.class);
                            intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                            intent = intent.putExtra("FilterKey", filterKey);
                            intent = intent.putExtra("StatusName", statusName);
                            intent = intent.putExtra("ButtonName", valueButtonname);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mCtx.startActivity(intent);
//                            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                            alertbox.setTitle("Do you want to change the status");
//                            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                  //  cancelOrderListPost();
//
//                                }
//                            });
//
//                            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//
//                                }
//                            });
//                            alertbox.show();

                        }


                    } else {
//                        cancelOrderListPost();
                    Intent intent = new Intent(mCtx, ItemListActivity.class);
                    intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                    intent = intent.putExtra("FilterKey", filterKey);
                    intent = intent.putExtra("StatusName", statusName);
                    intent = intent.putExtra("ButtonName", valueButtonname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);
                    }
//                     AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                     alertbox.setIcon(R.drawable.ic_warning_black_24dp);
//                     alertbox.setTitle("Do You Want to Cancel");
//                     alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                       //    exit(0);
//                        Intent intent = new Intent(mCtx, ItemListActivity.class);
//                        intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
//                        intent = intent.putExtra("FilterKey", filterKey);
//                        intent = intent.putExtra("StatusName", statusName);
//                        intent = intent.putExtra("ButtonName", valueButtonname);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mCtx.startActivity(intent);
//                       // cancelOrderListPost();
//                    }
//                });
//
//                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        // Nothing will be happened when clicked on no button
//                        // of Dialog
//                    }
//                });
//
//                alertbox.show();
                }  if (v.getId() == share.getId()) {
                                message = "\nCustomerName = " + orderitem.getCustomerName()+
                                            "\nAddress = " + orderitem.getAddress() +
                                             "\nMobile = " + orderitem.getMobile()+
                                             "\nDate = " + orderitem.getOrderDate()+
                                             "\nAdvance = " + orderitem.getAdvance() +
                                             "\nAmount = " + orderitem.getTotalAmount();

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                mCtx.startActivity(Intent.createChooser(shareIntent, "SHARE"));

                     }
             if (v.getId() == print.getId()) {

                    Document doc = new Document();

                PdfPTable table = new PdfPTable(new float[]{2, 2,2, 2, 2,2,2});
                table.getDefaultCell().

                        setHorizontalAlignment(Element.ALIGN_CENTER);




                table.addCell("CustomerName");
                table.addCell("Address");
                table.addCell("Mobile");
                table.addCell("Item");
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




                {

                    table.addCell(orderitem.getCustomerName().toString());
                    table.addCell( orderitem.getAddress());
                    table.addCell(orderitem.getMobile());
                   table.addCell(orderitem.getRoomList().split("-")[0].trim());
                    table.addCell(orderitem.getOrderDate().split("T")[0].trim());
                    table.addCell(String.valueOf(orderitem.getAdvance()));
                    table.addCell(String.valueOf(orderitem.getTotalAmount()));



                }

                try

                {
                    // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

                    String path = mCtx.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

                    File dir = new File(String.valueOf(path));
                    if (!dir.exists())
                        dir.mkdirs();

                    Log.d("PDFCreator", "PDF Path: " + path);

                    File file = new File(dir, "OrderListAdapter.pdf");

                    FileOutputStream fOut = new FileOutputStream(file);


                    PdfWriter.getInstance(doc, fOut);

                    //open the document
                    doc.open();

                    Paragraph p1 = new Paragraph("Regards by :-" + SharedPrefManager.getInstance(mCtx).getUser().getUserName());


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
                    OrderListAdapter.HeaderFooter pdfFooter = new OrderListAdapter.HeaderFooter();
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
                pdfOpenintent = pdfOpenintent.putExtra("PDFName","/OrderListAdapter.pdf");

                mCtx.startActivity( pdfOpenintent);

                       }

            }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            orderitem = this.orderitemList.get(position);
            if (v.getId() == itemView.getId()) {
                try {
                    //  Toast.makeText(mCtx,String.valueOf(  orderitem.OrderID), Toast.LENGTH_SHORT).show();
                } catch (Exception E) {
                    E.printStackTrace();
                }
                // Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }

    private void showDailogForHandOverTo(View v) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
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

        String[] items = orderitem.getTelorList().split(",");
        ArrayAdapter<String> spinneArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, items);
        spinneArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        tailorListNameSpinner.setAdapter(spinneArrayAdapter);

        dialogBuilder.setTitle("Hand Over List");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        if(!handOvertoNameSpinner.getSelectedItem().equals("Telor")){
            tailorListNameSpinner.setVisibility(View.GONE);
        }

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
                            handOverToListPost();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    b.dismiss();
                }
            }


        });


    }


    private void showdailogForreceivedBy(View v) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
        final View dialogView = inflater.inflate(R.layout.alert_for_received_tailor, null);
        dialogBuilder.setView(dialogView);

        editReceivedBy = dialogView.findViewById(R.id.receivedEditText);
        okRecevedButton = dialogView.findViewById(R.id.okReceivedButton);
        canceLOrderButton = dialogView.findViewById(R.id.cancelOrderButton);



        dialogBuilder.setTitle("Receibed By List");
        final AlertDialog b = dialogBuilder.create();
        b.show();

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

    private void cancelOrderListPost() {

        try {
            new PostCancelOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, statusName);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostCancelOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", String.valueOf(orderitem.getOrderID()));
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", statusname);
                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //      progressDialog.dismiss();
//                Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
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
                if (jsonbodyres.getStatus() == true) {}
                //       progressDialog.dismiss();
            }
        }
    }

    private void handOverToListPost() {
        try {
            new PostHandOverToList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, String.valueOf(orderitem.getOrderID()), statusName, String.valueOf(handOvertoNameSpinner.getSelectedItem()), String.valueOf(tailorListNameSpinner.getSelectedItem()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostHandOverToList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String orderid = params[1];
            String statusname = params[2];
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
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", "0");
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
                }
            }
        }
    }


    private void receiFromeTailorToListPost() {
        try {

            new receivedFrometailorToListPost().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, String.valueOf(orderitem.getOrderID()), statusName, editReceivedBy.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class receivedFrometailorToListPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String orderid = params[1];
            String statusname = params[2];
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
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", "0");
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
                }
            }
        }
    }

    private void dispatchListPost() {
        try {

            new dispatchListPost().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, String.valueOf(orderitem.getOrderID()), statusName);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class dispatchListPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String orderid = params[1];
            String statusname = params[2];


            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", "blank");
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", statusname);



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
