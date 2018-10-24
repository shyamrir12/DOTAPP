package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static java.lang.System.exit;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private List<DataOrder> orderitemList;
    String filterKey;
    private String valueButtonname;
    private  DataOrder orderitem;
    DataOrder order;
    private String statusName;



    public OrderListAdapter(Context mCtx, List<DataOrder> orderitemList, String filterKey,String valueButtonname,String statusName) {
        this.mCtx = mCtx;
        this.orderitemList = orderitemList;
        this.filterKey = filterKey;
        this.valueButtonname = valueButtonname;
        this.statusName=statusName;
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
            canceLOrderButton.setVisibility(View.GONE);

            try {
                if (!totalamount.getText().equals("0.0")) {
                    canceLOrderButton.setVisibility(View.VISIBLE);

                }
                else if (totalamount.getText().equals("0.0")){
                    canceLOrderButton.setVisibility(View.GONE);


                }
            }
            catch (Exception e)
            {e.printStackTrace();
            }


        }




        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
          orderitem = this.orderitemList.get(position);

            if (v.getId() == buttonOrder.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                i = i.putExtra("ActualOrder", "");
                mCtx.startActivity(i);
            }
            if (v.getId() == buttonActualOrder.getId()) {


                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                i = i.putExtra("ActualOrder", "ActualOrder");
                mCtx.startActivity(i);
            }
            if (v.getId() == canceLOrderButton.getId()) {
                cancelOrderListPost();
//                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//                alertbox.setIcon(R.drawable.ic_warning_black_24dp);
//                alertbox.setTitle("Do You Want to Cancel");
//                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                       //    exit(0);
//
//                        cancelOrderListPost();
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

    private void cancelOrderListPost() {

        try {

       //     progressDialog.setMessage("loading...");
      //      progressDialog.show();
            new PostCancelOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,statusName);

        } catch (Exception e) {

            e.printStackTrace();
     //       progressDialog.dismiss();
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
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew" );
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", String.valueOf(orderitem.getOrderID()));
                parameters.add("RoomName","blank" );
                parameters.add("OrderItemId" ,"0");
                parameters.add("StatusName",statusname);
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
                Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {


                }
        //       progressDialog.dismiss();
            }
        }
    }
}
