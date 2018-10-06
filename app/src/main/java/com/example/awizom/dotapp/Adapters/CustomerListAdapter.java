package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CustomerModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;

    //we are storing all the products in a list
    private List<CustomerModel> customeritemList;


    public CustomerListAdapter(Context mCtx, List<CustomerModel> orderitemList) {
        this.mCtx = mCtx;
        this.customeritemList = orderitemList;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_list_fragment, null);
        return new OrderItemViewHolder(view, mCtx, customeritemList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        CustomerModel customer = customeritemList.get(position);
        try {
            //holder.textViewPINo.setText("PINo \n"+Integer.toString( order.getPINo()));
            holder.c_name.setText(customer.getCustomerName());
            holder.c_address.setText(customer.getAddress());
            holder.c_contact.setText(customer.getMobile());
            holder.i_name.setText(customer.getInteriorName());
            holder.i_contact.setText(customer.getInteriorMobile());
            holder.i_address.setText(customer.getAddress());

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return customeritemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        AlertDialog.Builder alert;
        private  Context mCtx;

        TextView c_name, c_contact, c_address, i_name, i_contact,i_address;
        TextView OrderItemID, MaterialType, Price2, Qty, AQty;

        //we are storing all the products in a list
        private List<CustomerModel> orderitemList;


        public OrderItemViewHolder(View itemView, Context mCtx, List<CustomerModel> orderitemList) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            // CatelogOrderDetailModel catelogOrderDetailModel = new CatelogOrderDetailModel();
            c_name = itemView.findViewById(R.id.customerName);
            c_contact = itemView.findViewById(R.id.contact);
            c_address = itemView.findViewById(R.id.address);
            i_name = itemView.findViewById(R.id.interiorName);
            i_address = itemView.findViewById(R.id.interiorAddress);
            i_contact = itemView.findViewById(R.id.interiorContact);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CustomerModel orderitem = this.orderitemList.get(position);

        }

        @Override
        public boolean onLongClick(View v) {

            int position = getAdapterPosition();
            CustomerModel orderitem = this.orderitemList.get(position);

            if (v.getId() == itemView.getId()) {
                // showUpdateDeleteDialog(order);
                try{

                } catch (Exception E) {
                    E.printStackTrace();}
                Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


    }
}
