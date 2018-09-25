package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    AlertDialog b;
    //we are storing all the products in a list
    private List<CatelogOrderDetailModel> orderList;
    private CatelogOrderDetailModel catelogOrderDetailModel;

    public OrderItemAdapter(Context mCtx, List<CatelogOrderDetailModel> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
        progressDialog = new ProgressDialog(mCtx);
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
            holder.serialNo.setText("SNO\n"+order.getSerialNo());
            holder.catlogName.setText("Catalog\n"+order.getCatalogName());
            holder.design.setText("Design\n"+order.getDesign());
            holder.pageNo.setText("PageNo\n"+Integer.toString(order.getPageNo()));
            holder.price.setText("MRP\n"+Integer.toString(order.getPrice()));

            holder.MaterialType.setText("Material\n"+order.getMaterialType());
            holder.Price2.setText("Price\n"+Integer.toString(order.getPrice2()));
            holder.Qty.setText("Qty\n"+Integer.toString(order.getQty()));
            holder.AQty.setText("AQty\n"+Integer.toString(order.getAQty()));
            holder.unit.setText("Unit\n"+order.getOrderUnit());
        } catch (Exception E) {
            E.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        AlertDialog.Builder alert;
        private final Context mCtx;

        TextView catlogName, serialNo, design, pageNo, price,unit;
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


            MaterialType = itemView.findViewById(R.id.materialtype);
            Price2 = itemView.findViewById(R.id.price2);
            Qty = itemView.findViewById(R.id.qty);
            AQty = itemView.findViewById(R.id.aQty);
            unit = itemView.findViewById(R.id.unit);

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
                try{
                    initViewByAlertdailog(order);
                } catch (Exception E) {
                    E.printStackTrace();}
                //Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


    }
    private void initViewByAlertdailog(CatelogOrderDetailModel order) {
        //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx, R.style.Theme_AppCompat_NoActionBar);
        AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder( mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.add_dailog_layout, null);
        dialogBuilder.setView(dialogView);
        final String OrderItemID=String.valueOf( order.getOrderItemID());
        final String orderRoomId=String.valueOf( order.getOrderRoomID());
        final String catalogID=String.valueOf( order.getCatalogID());
        final String orderRoomName= order.getRoomName();
        final String orderID=String.valueOf( order.getOrderID());
        final EditText s_no = dialogView.findViewById(R.id.sNo);
        final EditText catlogName = dialogView.findViewById(R.id.catlogName);
        final EditText design = dialogView.findViewById(R.id.design);
        final EditText  pageNo = dialogView.findViewById(R.id.pageNo);
        final EditText  price = dialogView.findViewById(R.id.price);
        final EditText   price2 = dialogView.findViewById(R.id.price2);
        final Spinner materialType = dialogView.findViewById(R.id.materialType);
        final EditText   qty = dialogView.findViewById(R.id.qTy);
        final EditText   aQty = dialogView.findViewById(R.id.aQty);
        final Spinner  unitSpinner = dialogView.findViewById(R.id.unit);


        s_no.setText(order.getSerialNo());
        catlogName.setText(order.getCatalogName());
        design.setText(order.getDesign());
        pageNo.setText(Integer.toString(order.getPageNo()));
        price.setText(Integer.toString(order.getPrice()));
        materialType.setSelection(((ArrayAdapter<String>)materialType.getAdapter()).getPosition(order.getMaterialType()));

      //Price2.setText(Integer.toString(order.getPrice2()));
        qty.setText(Integer.toString(order.getQty()));
        aQty.setText(Integer.toString(order.getAQty()));
        unitSpinner.setSelection(((ArrayAdapter<String>)unitSpinner.getAdapter()).getPosition(order.getOrderUnit()));


        final Button buttonAdd = (Button) dialogView.findViewById(R.id.add);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        dialogBuilder.setTitle("Edit Order Item");
         b = dialogBuilder.create();
        b.show();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String snumber = s_no.getText().toString();
                    String catlogname = catlogName.getText().toString();
                    String desiGn = design.getText().toString();
                    String page_no = pageNo.getText().toString();
                    String priCe = price.getText().toString();
                    String priCe2 = price2.getText().toString();
                    String qTy = qty.getText().toString();
                    String aqty = aQty.getText().toString();
                    String materialtype = materialType.getSelectedItem().toString();
                    String unIt =  unitSpinner.getSelectedItem().toString();
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new OrderItemAdapter. POSTOrder().execute(OrderItemID,materialtype,priCe2,qTy,"0",unIt,orderRoomId,catlogname,snumber,desiGn,page_no,priCe,unIt,catalogID,orderRoomName.trim(),orderID.trim());


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
                }
                b.dismiss();

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
    private class POSTOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //     InputStream inputStream
            String orderItemId = params[0];
            String materialtype = params[1];
            String priCe2 = params[2];
            String qTy = params[3];
            String aqty = params[4];
            String orderUnit = params[5];
            String orderRoomId = params[6];

            String catlogname = params[7];
            String snumber = params[8];
            String desiGn = params[9];
            String page_no = params[10];
            String priCe = params[11];
            String unit = params[12];
            String catalogID = params[13];
            String roomName = params[14];
            String orderID = params[15];




            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API+"OrderItemPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderItemID", orderItemId);
                parameters.add("MaterialType",materialtype);
                parameters.add("Price2", priCe2);
                parameters.add("Qty", qTy);
                parameters.add("AQty", aqty);
                parameters.add("OrderUnit", orderUnit);
                parameters.add("OrderRoomID", orderRoomId);


                parameters.add("CatalogName", catlogname);
                parameters.add("SerialNo", snumber);
                parameters.add("Design", desiGn);
                parameters.add("PageNo", page_no);
                parameters.add("Price", priCe);

                parameters.add("Unit", unit);
                parameters.add("CatalogID", catalogID);

                parameters.add("RoomName",roomName.trim());
                parameters.add("OrderID",orderID.trim());



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
//               getMyOrder();

                }
                progressDialog.dismiss();
            }
        }
    }
}
