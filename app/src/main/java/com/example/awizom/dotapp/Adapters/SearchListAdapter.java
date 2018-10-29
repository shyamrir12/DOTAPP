package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SearchDetailListActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchItemViewHolder>{
    private Context mCtx;
    private List<DataOrder> searchDataOrders;
    private String statusName;
    private DataOrder searchViewData;
    private String statusMessage;
    AlertDialog.Builder alertbox;
    public SearchListAdapter(Context mCtx, List<DataOrder> searchItemsDataOrders,String statusName) {
        this.mCtx = mCtx;
        this.statusName = statusName;
        this.searchDataOrders = searchItemsDataOrders;
    }
    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_list, null);
        return new SearchItemViewHolder(view, mCtx, searchDataOrders);
    }


    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
            searchViewData = searchDataOrders.get(position);
        try {
            holder.c_name.setText("Name\n" + searchViewData.getCustomerName().trim());
            holder.c_contact.setText("Address\n " + searchViewData.getMobile().trim());
            holder.c_address.setText("Mobile\n " + searchViewData.getAddress().trim());
            holder.c_oredrDate.setText("Date\n " + searchViewData.getOrderDate().split("T")[0].trim());
            holder.c_advance.setText("Advance\n " + Double.toString(searchViewData.getAdvance()).trim());
            holder.c_totalAmount.setText("Amount\n " + Double.toString(searchViewData.getTotalAmount()).trim());


        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return searchDataOrders.size();
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        AlertDialog.Builder alert;
        private Context mCtx;
        private List<DataOrder> searchDataOrdersList;
        private TextView c_name, c_contact, c_address, c_oredrDate, c_advance,  c_totalAmount;
        Button orderButton,actualOrderButton,checkstatusButton;

        public SearchItemViewHolder(View itemView, Context mCtx, List<DataOrder> searchItemsDataOrders) {
            super(itemView);
            this.mCtx = mCtx;
            this.searchDataOrdersList = searchItemsDataOrders;

            itemView.setOnClickListener(this);

            c_name = itemView.findViewById(R.id.textViewCustomerName);
            c_address = itemView.findViewById(R.id.textViewCustomerAddress);
            c_contact = itemView.findViewById(R.id.textViewMobile);
            c_oredrDate = itemView.findViewById(R.id.textViewOrderDate);
            c_advance = itemView.findViewById(R.id.textViewAdvance);
            c_totalAmount = itemView.findViewById(R.id.textViewATotalAmount);

            orderButton = itemView.findViewById(R.id.buttonOrder);
            actualOrderButton = itemView.findViewById(R.id.buttonActualOrder);
            checkstatusButton = itemView.findViewById(R.id.checkStatusButton);

            orderButton.setOnClickListener(this);
            actualOrderButton.setOnClickListener(this);
            checkstatusButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DataOrder orderitem = this.searchDataOrdersList.get(position);
            if (v.getId() == checkstatusButton.getId()) {
                alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                getSearchList();


            }
            if (v.getId() == orderButton.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                mCtx.startActivity(i);
            }
            if (v.getId() == actualOrderButton.getId()) {

                Intent i = new Intent().setClass(mCtx, AfterCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                mCtx.startActivity(i);
            }


        }



    }


    private void getSearchList() {
        try {

            new GetSearchDetails().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetSearchDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String accesstoken = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "OrderStatusByOrderIDGet/" + searchViewData.getOrderID());
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
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
                Toast.makeText(mCtx, "There is no data available" +
                        "", Toast.LENGTH_SHORT).show();
            } else {

                alertbox.setTitle(result);
                alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alertbox.show();
                //statusMessage = result;
            }
        }

    }

}