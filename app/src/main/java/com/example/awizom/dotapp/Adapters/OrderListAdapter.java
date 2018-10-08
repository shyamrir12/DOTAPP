package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.OrderListFragment;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    private List<DataOrder> orderitemList;

    public OrderListAdapter(Context mCtx, List<DataOrder> orderitemList) {
        this.mCtx = mCtx;
        this.orderitemList = orderitemList;
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

            holder.ordername.setText("Name\n" + order.getCustomerName());
            holder.orderaddress.setText("Address\n " + order.getAddress());
            holder.ordercontact.setText("Mobile\n " + order.getMobile());
            holder.orderdate.setText("Date\n " + order.getOrderDate().split("T")[0]);
            holder.orderamount.setText("Advance\n " + Double.toString(order.getAdvance()));
            holder.totalamount.setText("Amount\n " + Double.toString(order.getTotalAmount()));

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

        private TextView ordername, orderaddress, ordercontact, orderdate, orderamount, totalamount, textviewStatus;
        private Button statusOrder;
        private List<DataOrder> orderitemList;


        public OrderItemViewHolder(View view, Context mCtx, List<DataOrder> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ordername = view.findViewById(R.id.textViewCustomerName);
            orderaddress = view.findViewById(R.id.textViewCustomerAddress);
            ordercontact = view.findViewById(R.id.textViewMobile);
            orderdate = view.findViewById(R.id.textViewOrderDate);
            orderamount = view.findViewById(R.id.textViewAdvance);
            totalamount = view.findViewById(R.id.textViewATotalAmount);
            textviewStatus = view.findViewById(R.id.textViewStatus);
            textviewStatus.setOnClickListener(this);
            statusOrder = view.findViewById(R.id.buttonOP);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DataOrder orderitem = this.orderitemList.get(position);
            if (v.getId() == textviewStatus.getId()) {
                try {

                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new POSTStatus().execute(String.valueOf(orderitem.getOrderID()), "0", "0", "0", "0", "0", "", "", "");
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

                }
            }
            if (v.getId() == statusOrder.getId()) {

                dept = "Order Placed";
                messageDisplayOfStatus(orderitem);
            }
        }

        private boolean messageDisplayOfStatus(final DataOrder order) {
            alert = new AlertDialog.Builder(mCtx);
            alert.setTitle(dept);
            alert.setMessage("Are you sure you want to change the " + dept + " status to OK?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        //String res="";
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        String OP = "false", MR = "false", RFT = "false", DESP = "false", CEN = "false";
                        if (order.isOrderPlaced())
                            OP = "true";
                        if (order.isMaterialReceived())
                            MR = "true";
                        if (order.isReceivedFromTalor())
                            RFT = "true";
                        if (order.isDispatch())
                            DESP = "true";
                        if (order.isCancel())
                            CEN = "true";

                        if (dept.equals("Order Placed")) {
                            new POSTStatus().execute(String.valueOf(order.getOrderID()), "true", MR, RFT, DESP, CEN, order.getHandOverTo(), order.getTelorName(), order.getReceivedBy());

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                        // System.out.println("Error: " + e);
                    }


                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                    dialog.cancel();
                }
            });
            alert.show();

            return true;
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            DataOrder orderitem = this.orderitemList.get(position);
            if (v.getId() == itemView.getId()) {
                try {
                } catch (Exception E) {
                    E.printStackTrace();
                }
                Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        private class POSTStatus extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String orderid = params[0];
                String OrderPlaced = params[1];
                String json = "";
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(AppConfig.BASE_URL_API + "OrderStatusPost");
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    builder.addHeader("Accept", "application/json");

                    FormBody.Builder parameters = new FormBody.Builder();
                    parameters.add("OrderID", orderid);

                    parameters.add("OrderPlaced", OrderPlaced);
                    builder.post(parameters.build());


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
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                    if (jsonbodyres.getStatus() == true) {
                    }
                    progressDialog.dismiss();

                }


            }

        }


    }
}
