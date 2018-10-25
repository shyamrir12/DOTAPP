package com.example.awizom.dotapp.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Models.Room;
import com.example.awizom.dotapp.Models.UserModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;

import java.util.List;

public class RoomListAdapter  extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    String StatusName,orderid,customername,mobile,orderdate,advance,actualorder;

    //we are storing all the products in a list
    private List<String> roomList;
    public RoomListAdapter(Context mCtx, List<String> roomList,String StatusName,String orderid,String customername,String mobile,String orderdate,String advance,String actualorder ) {
        this.mCtx = mCtx;
        this.roomList = roomList;
        this.StatusName = StatusName;

        this.orderid = orderid;
        this.customername = customername;
        this.mobile = mobile;
        this.orderdate = orderdate;
        this.advance = advance;
        this.actualorder = actualorder;

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

                Toast.makeText(mCtx, StatusName, Toast.LENGTH_SHORT).show();
            }


        }
    }
}
