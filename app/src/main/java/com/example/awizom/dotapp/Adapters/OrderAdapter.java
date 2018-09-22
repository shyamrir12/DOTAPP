package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.Room;
import com.example.awizom.dotapp.OrderActivity;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder > {

    private Context mCtx;
    ProgressDialog progressDialog;
    //we are storing all the products in a list
    private List<DataOrder> orderList;

    public OrderAdapter(Context mCtx, List<DataOrder> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
        progressDialog = new ProgressDialog(mCtx);


    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_order_list, null);
        return new OrderViewHolder(view, mCtx, orderList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DataOrder order = orderList.get(position);

        holder.TextViewCustomerName.setText(order.getCustomerName());
        holder.textViewMobile.setText(order.getMobile());
        holder.textViewOrderDate.setText(order.getOrderDate().split("T")[0]);
        holder.textViewAdvance.setText("Advance " + Double.toString(order.getAdvance()));
        holder.textHandOverTo.setText("H O T\n" + order.getHandOverTo());
        holder.textTelorName.setText("Telor Name\n" + order.getTelorName());
        holder.textReceivedBy.setText("Rec By\n" + order.getReceivedBy());
        if (order.getRoomList().trim().length() > 0) {


            String sampleString = order.getRoomList();
            String[] items = sampleString.split(",");


// Application of the Array to the Spinner
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, items);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            holder.spinner.setAdapter(spinnerArrayAdapter);


        }
        //  linerdept,L2,linerstatus;

       /*if(order.getDrawing().trim().length()==0)
            Glide.with(mCtx).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRA0vf_EXkL0RKmM5718bM1M7742qvMsRCEwvoLbOeiBTACc4kJYA").into(holder.imageView);
        else
            Glide.with(mCtx).load(order.getDrawing()).into(holder.imageView);*/
        //OP,MR,RFT,disp,reject,linerdept;

        if (order.isCancel()) {
            holder.linerdept.setBackgroundColor(Color.RED);

        } else {
            if (order.isOrderPlaced()) {
                holder.OP.setBackgroundColor(Color.GREEN);
                holder.MR.setBackgroundColor(Color.parseColor("#00BFFF"));
            }
            if (order.isMaterialReceived()) {

                holder.MR.setBackgroundColor(Color.GREEN);
                holder.RFT.setBackgroundColor(Color.parseColor("#00BFFF"));
            }
            if (order.isReceivedFromTalor()) {
                holder.RFT.setBackgroundColor(Color.GREEN);
                holder.disp.setBackgroundColor(Color.parseColor("#00BFFF"));
            }

            if (order.isDispatch()) {
                holder.disp.setBackgroundColor(Color.GREEN);
            }

        }
        if (order.getOrderStatusID() == 0) {

            holder.linerdept.setVisibility(View.GONE);
            holder.linerstatus.setVisibility(View.GONE);

        } else {
            holder.textViewAddStatus.setVisibility(View.GONE);

        }
        if (order.getOrderID() == 0) {
            holder.L2.setVisibility(View.GONE);
        } else {
            holder.textViewAddOrder.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        AlertDialog.Builder alert;
        String id, dept, deptcolname, status;
        Spinner spinner;
        TextView TextViewCustomerName, textViewMobile, textViewAddOrder, textViewAddStatus, textViewOrderDate, textViewAdvance, textViewAddRoom,
                textHandOverTo, textTelorName, textReceivedBy;

        Button button, buttonOP, buttonMR, buttonRFT, buttonDisp, buttonReject;

        LinearLayout OP, MR, RFT, disp, reject, linerdept, L2, linerstatus;
        private Context mCtx;
        //we are storing all the products in a list
        private List<DataOrder> orderList;


        public OrderViewHolder(View itemView, Context mCtx, List<DataOrder> orderList) {
            super(itemView);
            this.mCtx = mCtx;
            this.orderList = orderList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            TextViewCustomerName = itemView.findViewById(R.id.TextViewCustomerName);
            textViewMobile = itemView.findViewById(R.id.textViewMobile);
            textViewAddOrder = itemView.findViewById(R.id.textViewAddOrder);
            textViewAddStatus = itemView.findViewById(R.id.textViewAddStatus);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewAdvance = itemView.findViewById(R.id.textViewAdvance);
            textViewAddRoom = itemView.findViewById(R.id.textViewAddRoom);
            textHandOverTo = itemView.findViewById(R.id.textHandOverTo);
            textTelorName = itemView.findViewById(R.id.textTelorName);
            textReceivedBy = itemView.findViewById(R.id.textReceivedBy);

            spinner = itemView.findViewById(R.id.spinner);

            OP = itemView.findViewById(R.id.OP);
            MR = itemView.findViewById(R.id.MR);
            RFT = itemView.findViewById(R.id.RFT);

            disp = itemView.findViewById(R.id.disp);
            reject = itemView.findViewById(R.id.reject);
            linerdept = itemView.findViewById(R.id.linerdept);

            L2 = itemView.findViewById(R.id.L2);
            linerstatus = itemView.findViewById(R.id.linerstatus);
            textViewAddOrder.setOnClickListener(this);
            textViewAddStatus.setOnClickListener(this);
            textViewAddRoom.setOnClickListener(this);
            buttonOP = itemView.findViewById(R.id.buttonOP);
            buttonOP.setOnClickListener(this);
            buttonMR = itemView.findViewById(R.id.buttonMR);
            buttonMR.setOnClickListener(this);
            buttonRFT = itemView.findViewById(R.id.buttonRFT);
            buttonRFT.setOnClickListener(this);
            buttonDisp = itemView.findViewById(R.id.buttonDisp);
            buttonDisp.setOnClickListener(this);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            buttonReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DataOrder order = this.orderList.get(position);
            if (v.getId() == textViewAddOrder.getId()) {
                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new OrderAdapter.POSTOrder().execute(String.valueOf(order.getCustomerID()));
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
                }
            }
            if (v.getId() == textViewAddStatus.getId()) {
                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new OrderAdapter.POSTStatus().execute(String.valueOf(order.getOrderID()));
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
                }
            }
            if (v.getId() == textViewAddRoom.getId()) {
               showUpdateDeleteDialog(order.getOrderID(),order.getARoomList());
            }


          /*  if (v.getId() ==textViewPartyName.getId()) {
                Intent intent=new Intent(mCtx, DrawingActivity.class);
                intent.putExtra("filename",order.getId());
                intent.putExtra("livefilepath",order.getDrawing());
                intent.putExtra("pino",order.getPINo());
                intent.putExtra("partyname",order.getPartyName());
                this.mCtx.startActivity(intent);
            }
            if (v.getId() == buttonCut.getId()) {
                id=order.getId();
                dept="CUT Department";
                deptcolname="deptcut";
                status="RFG";
                updateArtist();
            }
            if (v.getId() == buttonGrind.getId()) {
                id=order.getId();
                dept="GRIND Department";
                deptcolname="deptgrind";
                status="RFF";
                updateArtist();
            }
            if (v.getId() == buttonFab.getId()) {
                id=order.getId();
                dept="FAB Department";
                deptcolname="deptfab";
                status="RFT";
                updateArtist();
            }
            if (v.getId() == buttonTemp.getId()) {
                id=order.getId();
                dept="TEMP Department";
                deptcolname="depttemp";
                status="RFD";
                updateArtist();
            }
            if (v.getId() == buttonDisp.getId()) {
                id=order.getId();
                dept="DISP Department";
                deptcolname="deptdisp";
                status="OC";
                updateArtist();
            }
            if (v.getId() == buttonReject.getId()) {
                id=order.getId();
                dept="Reject Department";
                deptcolname="deptdreject";
                status="Reject";
                updateArtist();
            }*/

            // uploadData.add(new DataWorkOrder("", PartyName, Location, PINo, workOrderNo, GlassSpecificationThick, GlassSpecificationColor, GlassSpecificationBTD, SizeIn, SizeMm, ActualSize, Hole, Cut, Qty, AreaInSQM, OrderDate, 0, Remark));
            // showUpdateDeleteDialog();
        }

        /*  private boolean updateArtist() {
              alert = new AlertDialog.Builder(mCtx);
              alert.setTitle(dept);
              alert.setMessage("Are you sure you want to change the "+dept+" status to OK?");
              alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      // continue with delete
                      //getting the specified artist reference
                      dataorder = FirebaseDatabase.getInstance().getReference("orders").child(id);
                      //updating artist
                      dataorder.child(deptcolname).setValue(true);
                      dataorder.child("remark").setValue(status);
                      Toast.makeText(mCtx, dept+" status changed to OK", Toast.LENGTH_LONG).show();
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
  */
        @Override
        public boolean onLongClick(View v) {

            int position = getAdapterPosition();
            DataOrder order = this.orderList.get(position);


            if (v.getId() == itemView.getId()) {

                // showUpdateDeleteDialog(order);
            }
            return true;
        }
        private void showUpdateDeleteDialog(final long orderid,String aroomlist) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);


            LayoutInflater inflater = LayoutInflater.from(mCtx);

            final View dialogView = inflater.inflate(R.layout.room_layout, null);
            dialogBuilder.setView(dialogView);

            final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);

            String[] items = aroomlist.split(",");
              ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, items);
              spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
               spinner.setAdapter(spinnerArrayAdapter);

            final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddOrder);
            final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

            dialogBuilder.setTitle("Add Room");
            final AlertDialog b = dialogBuilder.create();
            b.show();


            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (String.valueOf(spinner.getSelectedItem()).trim().length()>0) {
                        try {

                            progressDialog.setMessage("loading...");
                            progressDialog.show();
                            new OrderAdapter.POSTRoom().execute(String.valueOf(orderid), String.valueOf(spinner.getSelectedItem()).trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                            // System.out.println("Error: " + e);
                        }
                        b.dismiss();
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

    }
        private class POSTOrder extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                //     InputStream inputStream
                String customerid = params[0];
                String json = "";
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(AppConfig.BASE_URL_API + "OrderPost");
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    builder.addHeader("Accept", "application/json");
                    //builder.addHeader("Authorization", "Bearer " + accesstoken);

                    FormBody.Builder parameters = new FormBody.Builder();
                    parameters.add("OrderID", "0");
                    parameters.add("CustomerID", customerid);

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
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                    if (jsonbodyres.getStatus() == true) {
                        //getMyOrder();
                    }
                    progressDialog.dismiss();

                }


            }

        }

        private class POSTStatus extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                //     InputStream inputStream
                String orderid = params[0];
                String json = "";
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(AppConfig.BASE_URL_API + "OrderStatusPost");
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    builder.addHeader("Accept", "application/json");
                    //builder.addHeader("Authorization", "Bearer " + accesstoken);

                    FormBody.Builder parameters = new FormBody.Builder();
                    parameters.add("OrderID", orderid);


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
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                    if (jsonbodyres.getStatus() == true) {
                        //getMyOrder();

                    }
                    progressDialog.dismiss();

                }


            }

        }

        private class POSTRoom extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                //     InputStream inputStream
                String orderid = params[0];
                String roomname = params[1];
                String json = "";
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(AppConfig.BASE_URL_API + "OrderRoomPost");
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    builder.addHeader("Accept", "application/json");
                    //builder.addHeader("Authorization", "Bearer " + accesstoken);

                    FormBody.Builder parameters = new FormBody.Builder();
                    parameters.add("OrderID", orderid);
                    parameters.add("RoomName", roomname);


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
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                    if (jsonbodyres.getStatus() == true) {
                        //getMyOrder();
                    }
                    progressDialog.dismiss();

                }


            }

        }

    // This removes the data from our Dataset and Updates the Recycler View.
    private void removeItem(DataOrder infoData) {

        int currPosition = orderList.indexOf(infoData);
        orderList.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    // This method adds(duplicates) a Object (item ) to our Data set as well as Recycler View.
    private void addItem(int position, DataOrder infoData) {

        orderList.add(position, infoData);
        notifyItemInserted(position);

    }
    public void modifyItem(final int position, final DataOrder model) {
        orderList.set(position, model);
        notifyItemChanged(position);
    }
}