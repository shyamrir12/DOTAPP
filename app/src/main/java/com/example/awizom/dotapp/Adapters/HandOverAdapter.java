package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.R;

import java.util.List;


public class HandOverAdapter extends RecyclerView.Adapter<HandOverAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private LinearLayout linearLayout;

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

            holder.a_qty.setText(String.valueOf( handover.getAQty()));
            holder.catalog.setText(handover.getCatalogName());
            holder.design.setText(handover.getDesign());
            holder.page_no.setText(String.valueOf(handover.getPageNo()));

            holder.price.setText(String.valueOf( handover.getPrice()).toString());
            holder.price2.setText(String.valueOf(handover.getPrice2()));
            holder.serialNo.setText(String.valueOf(handover.getSerialNo()));
            holder.unit.setText(String.valueOf(handover.getUnit()));

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
        TextView a_qty, catalog, design, page_no, price, price2,receivedBy,serialNo,unit;

        //we are storing all the products in a list
        private List<HandOverModel> orderitemList;


        public OrderItemViewHolder(View itemView, Context mCtx, List<HandOverModel> handoveritemlist) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderitemList = handoveritemlist;

            a_qty = itemView.findViewById(R.id.qty);
            catalog = itemView.findViewById(R.id.catlogName);
            design = itemView.findViewById(R.id.design);
            page_no = itemView.findViewById(R.id.pageo);
            price = itemView.findViewById(R.id.price);
            price2 = itemView.findViewById(R.id.pric2);
            receivedBy = itemView.findViewById(R.id.receivedby);
            serialNo = itemView.findViewById(R.id.sno);
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
