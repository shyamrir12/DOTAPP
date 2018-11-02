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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    String StatusName, orderid, customername, mobile, orderdate, advance, actualorder, filterkey,buttonname,telorlist,roomName;
    private EditText editReceivedBy;
    private Button okRecevedButton,canceLOrderButton;
    private Spinner handOvertoNameSpinner, tailorListNameSpinner;
    private String handOverToListSpinnerData[] = {"Telor", "Sofa Karigar", "Self Customer", "Wallpaper fitter"};
    //we are storing all the products in a list
    private List<String> roomList;

    public RoomListAdapter(Context mCtx, List<String> roomList, String StatusName, String orderid, String customername, String mobile, String orderdate, String advance, String actualorder, String filterkey,String buttonname,String telorlist) {
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
        this.buttonname = buttonname;
        this.telorlist = telorlist;
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
            String v1 = room.split("Total")[1].trim();

            if (filterkey.equals("PandingToPlaceOrder")||filterkey.equals("PandingToHandOverTo")||filterkey.equals("PandingToReceivedFromTelor")||filterkey.equals("PandingToReceiveMaterial")) {

                if(room.indexOf("~")!=-1) {
                    if (!room.split("~")[1].trim().equals("Hold")) {

                        holder.button_status.setVisibility( View.VISIBLE );
                        holder.button_status.setText( buttonname );


                    }
                }
                else {
                    if(!room.split("-Total ")[1].trim().equals("0.00"))
                    {

                        holder.button_status.setVisibility(View.VISIBLE);
                        holder.button_status.setText( buttonname );


                    }
                }



            } else if (filterkey.equals("Hold") && room.split("~")[1].trim().equals("Hold")) {
                if(!room.split("~")[0].split("-Total ")[1].trim().equals("0.00"))
                {

                    holder.button_status.setVisibility(View.VISIBLE);
                    holder.button_status.setText( buttonname );
                }

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
            super(view);
            this.mCtx = mCtx;
            this.roomList = roomList;

            itemView.setOnClickListener(this);

            roomname = itemView.findViewById(R.id.label);
            button_status = itemView.findViewById(R.id.button_status);


            roomname.setOnClickListener(this);
            button_status.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {

            int position = getAdapterPosition();
            String room = this.roomList.get(position);

            if (v.getId() == roomname.getId()) {

                if(room.trim().equals("Room Name")){
                    Toast.makeText(mCtx,"Please add rooms in this order",Toast.LENGTH_SHORT);
                }else {
                    Intent i = new Intent().setClass(mCtx, RoomDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    i = i.putExtra("RoomName", room.split("-")[0].trim());
                    i = i.putExtra("OrderID", Integer.valueOf(orderid));
                    i = i.putExtra("CustomerName", customername);
                    i = i.putExtra("Mobile", mobile);
                    i = i.putExtra("OrderDate", orderdate);
                    i = i.putExtra("Advance", Double.valueOf(advance));
                    i = i.putExtra("ActualOrder", actualorder);

                    i = i.putExtra("StatusName", StatusName);
                    i = i.putExtra("FilterKey", filterkey);
                    i = i.putExtra("ButtonName", buttonname);
                    i = i.putExtra("TailorList", telorlist);


                    mCtx.startActivity(i);
                }

            }
            if (v.getId() == button_status.getId()) {
                roomName=room.split("-")[0].trim();
               if( filterkey.equals("PandingToPlaceOrder")||filterkey.equals("PandingToReceiveMaterial")||filterkey.equals("Hold"))
                {
                    placeOrderPost(room.split("-")[0].trim());
                }
                else if(filterkey.equals("PandingToHandOverTo"))
               {
                   android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                   alertbox.setTitle("Do you want to change the status");
                   alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface arg0, int arg1) {
                           showDailogForHandOverTo(v);
                       }
                   });
                   alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface arg0, int arg1) {

                       }
                   });

                   alertbox.show();

               }
               else if (filterkey.equals("PandingToReceivedFromTelor"))
               {
                   AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                   alertbox.setTitle("Do you want to change the status");
                   alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface arg0, int arg1) {
                           showdailogForreceivedBy(v);
                       }
                   });
                   alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface arg0, int arg1) {

                       }
                   });

                   alertbox.show();
               }

            }


        }
        private void showdailogForreceivedBy(View v) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
            final View dialogView = inflater.inflate(R.layout.alert_for_received_tailor, null);
            dialogBuilder.setView(dialogView);

            editReceivedBy = dialogView.findViewById(R.id.receivedEditText);
            okRecevedButton = dialogView.findViewById(R.id.okReceivedButton);
            canceLOrderButton = dialogView.findViewById(R.id.cancelOrderButton);

            dialogBuilder.setTitle("Receibed By List");
            final android.support.v7.app.AlertDialog b = dialogBuilder.create();
            b.show();

            okRecevedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!editReceivedBy.getText().toString().isEmpty()) {
                            receiFromeTailorToListPost();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    b.dismiss();
                }
            });


        }
        private void showDailogForHandOverTo(View v) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
            final View dialogView = inflater.inflate(R.layout.handover_to_show_telo, null);
            dialogBuilder.setView(dialogView);

            handOvertoNameSpinner = dialogView.findViewById(R.id.handOverToNameSpinner);
            tailorListNameSpinner = dialogView.findViewById(R.id.tailorListSpinner);
            final Button buttonOk = dialogView.findViewById(R.id.buttonOk);
            final LinearLayout l2 = dialogView.findViewById(R.id.l2);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, handOverToListSpinnerData);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            handOvertoNameSpinner.setAdapter(spinnerArrayAdapter);
            progressDialog = new ProgressDialog(mCtx);

            String[] items =telorlist.split(",");
            ArrayAdapter<String> spinneArrayAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, items);
            spinneArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            tailorListNameSpinner.setAdapter(spinneArrayAdapter);

            dialogBuilder.setTitle("Hand Over List");
            final android.support.v7.app.AlertDialog b = dialogBuilder.create();
            b.show();

            if(!handOvertoNameSpinner.getSelectedItem().equals("Telor")){
                tailorListNameSpinner.setVisibility(View.GONE);
            }

//        handOvertoNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                tailorListNameSpinner.setVisibility(View.VISIBLE);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (String.valueOf(tailorListNameSpinner.getSelectedItem()).trim().length() > 0) {
                        try {
                            if(!handOvertoNameSpinner.getSelectedItem().equals("")) {
                                handOverToListPost(); // handOverToListPost();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        b.dismiss();
                    }


                }


            });


        }
    }

    private void placeOrderPost(String roomname) {

        try {
            new RoomListAdapter.PostPlaceOrderList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,StatusName, roomname);

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
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
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

    private void receiFromeTailorToListPost() {
        try {

            new receivedFrometailorToListPost().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token, StatusName, roomName, editReceivedBy.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class receivedFrometailorToListPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String roomname = params[2];
            String name = params[3];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", roomname);
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", statusname);
                parameters.add("ReceivedBy", name);


                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "There is no data available", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                }
            }
        }

    }


    private void handOverToListPost() {
        try {
            new PostHandOverToList().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token,StatusName,roomName,String.valueOf(handOvertoNameSpinner.getSelectedItem()), String.valueOf(tailorListNameSpinner.getSelectedItem()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class PostHandOverToList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // InputStream inputStream
            String accesstoken = params[0];
            String statusname = params[1];
            String roomname = params[2];
            String handoverto = params[3];
            String tailorname = params[4];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusPostNew");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("RoomName", roomname);
                parameters.add("OrderItemID", "0");
                parameters.add("StatusName", statusname);
                parameters.add("HandOverTo", handoverto);
                parameters.add("TelorName", tailorname);


                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(mCtx, "There is no data available", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                }
            }
        }
    }

}
