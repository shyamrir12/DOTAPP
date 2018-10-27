package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class HandOverAdapter extends RecyclerView.Adapter<HandOverAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private String[] handoveritemList;
TextView telor;
    //we are storing all the products in a list
    private List<HandOverModel> handoveritemlist;


    public HandOverAdapter(Context mCtx, List<HandOverModel> handoverlist) {
        this.mCtx = mCtx;
        this.handoveritemlist = handoverlist;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.handoverlist, null);
        return new OrderItemViewHolder(view, mCtx, handoveritemlist);

    }


    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        HandOverModel handover = handoveritemlist.get(position);
        try {
           // holder.telor.setText(handover.getTelorName());
            holder.snum.setText(handover.getSerialNo());
            holder.a_qty.setText(String.valueOf( handover.getAQty()));
            holder.catalog.setText(handover.getCatalogName());
            holder.design.setText(handover.getDesign());
            holder.page_no.setText(String.valueOf(handover.getPageNo()));
            holder.aunt.setText(handover.getUnit());

          //  holder.price.setText(String.valueOf( handover.getPrice()).toString());
            holder.price.setText(String.valueOf(handover.getPrice2()));
          //  holder.serialNo.setText(String.valueOf(handover.getSerialNo()));


        } catch (Exception E) {
            E.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return handoveritemlist.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private Context mCtx;
        TextView a_qty, catalog, design, page_no, price, price2,telor,snum,aunt;

        //we are storing all the products in a list
        private List<HandOverModel> hndovritemList;


        public OrderItemViewHolder(View itemView, final Context mCtx, List<HandOverModel> handoveritemlist) {
            super(itemView);
            this.mCtx = mCtx;
            this.hndovritemList = handoveritemlist;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
         //   telor = itemView.findViewById(R.id.tlr);
            snum = itemView.findViewById(R.id.snum);
            aunt = itemView.findViewById(R.id.unt3);
            a_qty = itemView.findViewById(R.id.qty);
            catalog = itemView.findViewById(R.id.catlogName);
            design = itemView.findViewById(R.id.design);
            page_no = itemView.findViewById(R.id.pgn);
            price = itemView.findViewById(R.id.princ);
         //   price2 = itemView.findViewById(R.id.princ2);
           // receivedBy = itemView.findViewById(R.id.receivedby);


            //Button btn1 = itemView.findViewById(R.id.bton1);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            HandOverModel hndoitem = this.hndovritemList.get(position);
            //createPdf(handoveritemlist.get(position).toString());




            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Let's go for a trip to "
                    + hndoitem );
            intent.setType("text/plain");
            mCtx.startActivity(Intent.createChooser(intent, "Send To"));
                }






        private void createPdf(String sometext){
            // create a new document
            PdfDocument document = new PdfDocument();
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawCircle(50, 50, 30, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(sometext, 80, 50, paint);
            //canvas.drawt
            // finish the page
            document.finishPage(page);
// draw text on the graphics object of the page
            // Create Page 2
            pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();
            paint = new Paint();
            paint.setColor(Color.BLUE);
            canvas.drawCircle(100, 100, 100, paint);
            document.finishPage(page);
            // write the document content
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path+"test-2.pdf";
            File filePath = new File(targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));
            //    Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("main", "error "+e.toString());
           //     Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
            }
            // close the document
            document.close();
        }

    @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            HandOverModel hndoitem = this.hndovritemList.get(position);


        if (v.getId() == itemView.getId()) {
            // showUpdateDeleteDialog(order);
            try {

            } catch (Exception E) {
                E.printStackTrace();
            }
            Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
        }
    }
