package com.example.awizom.dotapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.AfterCreateOrderoFragment;
import com.example.awizom.dotapp.Fragments.OrderListFragment;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private List<DataOrder> orderitemList;
    String filterKey;


    public OrderListAdapter(Context mCtx, List<DataOrder> orderitemList, String filterKey) {
        this.mCtx = mCtx;
        this.orderitemList = orderitemList;
        this.filterKey = filterKey;
        progressDialog = new ProgressDialog(mCtx);
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
        DataOrder order = orderitemList.get(position);
        try {
            holder.ordername.setText("Name\n" + order.getCustomerName().trim());
            holder.orderaddress.setText("Address\n " + order.getAddress().trim());
            holder.ordercontact.setText("Mobile\n " + order.getMobile().trim());
            holder.orderdate.setText("Date\n " + order.getOrderDate().split("T")[0].trim());
            holder.orderamount.setText("Advance\n " + Double.toString(order.getAdvance()).trim());
            holder.totalamount.setText("Amount\n " + Double.toString(order.getTotalAmount()).trim());
            holder.textviewStatus.setText( filterKey );
            if(order.getOrderStatusID()==0)
            {
                holder.textviewStatus.setVisibility( View.GONE );
                holder.statusOrder.setVisibility( View.GONE );
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
        String dept;

        private TextView ordername, orderaddress, ordercontact, orderdate, orderamount, totalamount, textviewStatus,status;
        private Button statusOrder,buttonOrder,buttonActualOrder;
        private List<DataOrder> orderitemList;


        public OrderItemViewHolder(View view, Context mCtx, List<DataOrder> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ordername = view.findViewById(R.id.textViewCustomerName);
            status = view.findViewById(R.id.textViewStatus);
            orderaddress = view.findViewById(R.id.textViewCustomerAddress);
            ordercontact = view.findViewById(R.id.textViewMobile);
            orderdate = view.findViewById(R.id.textViewOrderDate);
            orderamount = view.findViewById(R.id.textViewAdvance);
            totalamount = view.findViewById(R.id.textViewATotalAmount);
            textviewStatus = view.findViewById(R.id.textViewStatus);
            textviewStatus.setOnClickListener(this);
            statusOrder = view.findViewById(R.id.buttonOP);
            buttonOrder = view.findViewById(R.id.buttonOrder);
            buttonActualOrder = view.findViewById(R.id.buttonActualOrder);
            statusOrder.setOnClickListener( this );
            buttonOrder.setOnClickListener( this );
            buttonActualOrder.setOnClickListener( this );
            textviewStatus.setText("List");
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DataOrder orderitem = this.orderitemList.get(position);
            if (v.getId() == textviewStatus.getId()) {

            }
            if (v.getId() == statusOrder.getId()) {


            }
            if (v.getId() == buttonOrder.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra( "OrderID" ,String.valueOf(   orderitem.OrderID));
                i = i.putExtra( "ActualOrder" ,"");
                mCtx.startActivity( i );
            }
            if (v.getId() == buttonActualOrder.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra( "OrderID" ,String.valueOf(   orderitem.OrderID));
                i = i.putExtra( "ActualOrder" ,"ActualOrder");
                mCtx.startActivity( i );
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            DataOrder orderitem = this.orderitemList.get(position);
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
}
