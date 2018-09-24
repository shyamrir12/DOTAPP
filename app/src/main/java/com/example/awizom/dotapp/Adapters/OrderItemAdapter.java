package com.example.awizom.dotapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {

    private Context mCtx;

    //we are storing all the products in a list
    private List<CatelogOrderDetailModel> orderList;
    private CatelogOrderDetailModel catelogOrderDetailModel;

    public OrderItemAdapter(Context mCtx, List<CatelogOrderDetailModel> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_item_layout, null);
        return new OrderViewHolder(view, mCtx, orderList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CatelogOrderDetailModel order = orderList.get(position);
        try {

            //holder.textViewPINo.setText("PINo \n"+Integer.toString( order.getPINo()));
            holder.serialNo.setText(order.getSerialNo());
            holder.catlogName.setText(order.getCatalogName());
            holder.design.setText(order.getDesign());
            holder.pageNo.setText(Integer.toString(order.getPageNo()));
            holder.price.setText(Integer.toString(order.getPrice()));


            holder.OrderItemID.setText(Integer.toString(order.getOrderItemID()));
            holder.MaterialType.setText(order.getMaterialType());
            holder.Price2.setText(Integer.toString(order.getPrice2()));
            holder.Qty.setText(Integer.toString(order.getQty()));
            holder.AQty.setText(Integer.toString(order.getAQty()));
        } catch (Exception E) {
            E.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private final Context mCtx;
        AlertDialog.Builder alert;
        TextView catlogName, serialNo, design, pageNo, price;
        TextView OrderItemID, MaterialType, Price2, Qty, AQty;

        //we are storing all the products in a list
        private List<CatelogOrderDetailModel> orderList;


        public OrderViewHolder(View itemView, Context mCtx, List<CatelogOrderDetailModel> orderList) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderList = orderList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            catelogOrderDetailModel = new CatelogOrderDetailModel();
            serialNo = itemView.findViewById(R.id.sno);
            catlogName = itemView.findViewById(R.id.cName);
            design = itemView.findViewById(R.id.c_design);
            pageNo = itemView.findViewById(R.id.c_pageNo);
            price = itemView.findViewById(R.id.c_pricec);

            OrderItemID = itemView.findViewById(R.id.orderItemID);
            MaterialType = itemView.findViewById(R.id.materialType);
            Price2 = itemView.findViewById(R.id.price2);
            Qty = itemView.findViewById(R.id.qty);
            AQty = itemView.findViewById(R.id.aQty);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CatelogOrderDetailModel order = this.orderList.get(position);

        }

        @Override
        public boolean onLongClick(View v) {

            int position = getAdapterPosition();
            CatelogOrderDetailModel order = this.orderList.get(position);

            if (v.getId() == itemView.getId()) {
                // showUpdateDeleteDialog(order);
            }
            return true;
        }


    }
}
