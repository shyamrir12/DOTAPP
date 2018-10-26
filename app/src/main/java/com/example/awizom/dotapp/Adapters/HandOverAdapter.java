package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.R;

import java.util.List;


public class HandOverAdapter extends RecyclerView.Adapter<HandOverAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;

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
            //holder.textViewPINo.setText("PINo \n"+Integer.toString( order.getPINo()));
            holder.a_qty.setText((int) handover.getAQty());
            holder.catalog.setText(handover.getCatalogName());
            holder.design.setText(handover.getDesign());
            holder.page_no.setText(handover.getPageNo());
            holder.price.setText((int) handover.getPrice());
            holder.price2.setText((int) handover.getPrice2());
            holder.receivedBy.setText(handover.getReceivedBy());
            holder.receivedFromTalor.setEnabled(true);

            holder.serialNo.setText(handover.getSerialNo());
            holder.telorname.setText(handover.getTelorName());
            holder.unit.setText(handover.getUnit());

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return handoveritemlist.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        AlertDialog.Builder alert;
        private Context mCtx;

        TextView a_qty, catalog, design, page_no, price, price2,receivedBy,receivedFromTalor,serialNo,telorname,unit;

        //we are storing all the products in a list
        private List<HandOverModel> orderitemList;


        public OrderItemViewHolder(View itemView, Context mCtx, List<HandOverModel> handoveritemlist) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderitemList = handoveritemlist;


            // CatelogOrderDetailModel catelogOrderDetailModel = new CatelogOrderDetailModel();
            a_qty = itemView.findViewById(R.id.aqty);
            catalog = itemView.findViewById(R.id.c_name);
            design = itemView.findViewById(R.id.design);
            page_no = itemView.findViewById(R.id.pageno);
            price = itemView.findViewById(R.id.price);
            price2 = itemView.findViewById(R.id.price2);

            receivedBy = itemView.findViewById(R.id.receivedby);
            receivedFromTalor = itemView.findViewById(R.id.receivetelor);
            serialNo = itemView.findViewById(R.id.sno);

            telorname = itemView.findViewById(R.id.tname);
            unit = itemView.findViewById(R.id.unt);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            HandOverModel orderitem = this.orderitemList.get(position);

        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            HandOverModel orderitem = this.orderitemList.get(position);

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
