package com.example.awizom.dotapp.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.Room;
import com.example.awizom.dotapp.Models.UserModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomListAdapter  extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    String StatusName,orderid,customername,mobile,orderdate,advance,actualorder,filterkey;

    //we are storing all the products in a list
    private List<String> roomList;
    public RoomListAdapter(Context mCtx, List<String> roomList,String StatusName,String orderid,String customername,String mobile,String orderdate,String advance,String actualorder ,String filterkey) {
        this.mCtx = mCtx;
        this.roomList = roomList;
        this.StatusName = StatusName;

        this.orderid = orderid;
        this.customername = customername;
        this.mobile = mobile;
        this.orderdate = orderdate;
        this.advance = advance;
        this.actualorder = actualorder;
        this.filterkey = filterkey;
        progressDialog = new ProgressDialog(mCtx);
    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_button_roomlist, null);
        return new RoomViewHolder(view, mCtx, roomList);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        String room = roomList.get(position);
        try {

            holder.roomname.setText(room);
            if(filterkey.equals( "PandingToPlaceOrder" ))
            {
                holder.button_status.setVisibility( View.VISIBLE );
            }
            else  if(filterkey.equals( "Hold" ) && room.split( "~"  )[1].trim().equals( "Hold" ) )
            {
                holder.button_status.setVisibility( View.VISIBLE );
            }


        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final Context mCtx;
        TextView roomname;

        Button button_status;
        private List<String> roomList;


        public RoomViewHolder(View view, Context mCtx, List<String> roomList) {
            super( view );
            this.mCtx = mCtx;
            this.roomList = roomList;

            itemView.setOnClickListener( this );

            roomname = itemView.findViewById( R.id.label );
            button_status = itemView.findViewById( R.id.button_status );


            roomname.setOnClickListener( this );
            button_status.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            String room = this.roomList.get( position );

            if (v.getId() == roomname.getId()) {

                Intent i = new Intent().setClass(mCtx, RoomDetailsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i = i.putExtra("RoomName",room.split( "-" )[0].trim());
                i = i.putExtra("OrderID", Integer.valueOf(orderid));
                i = i.putExtra("CustomerName",customername);
                i = i.putExtra("Mobile",mobile);
                i = i.putExtra("OrderDate",orderdate);
                i = i.putExtra("Advance",Double.valueOf(advance));
                i = i.putExtra("ActualOrder",actualorder);


                mCtx.startActivity(i);



            }
            if (v.getId() == button_status.getId()) {

                //Toast.makeText(mCtx, StatusName, Toast.LENGTH_SHORT).show();
                placeOrderPost(room.split( "-" )[0].trim());
            }


        }
    }
    private void placeOrderPost(String roomname) {

        try {
            new RoomListAdapter.PostPlaceOrderList().execute( SharedPrefManager.getInstance(mCtx).getUser().access_token, "OrderPlaced",roomname);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostPlaceOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String roomname = params[2];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url( AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", roomname);
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", statusname);
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
