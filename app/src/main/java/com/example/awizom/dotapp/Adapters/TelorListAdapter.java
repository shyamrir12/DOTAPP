package com.example.awizom.dotapp.Adapters;

import android.content.Context;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.TelorListFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.HomeActivity;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.TelorModel;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class TelorListAdapter extends RecyclerView.Adapter<TelorListAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;

    //we are storing all the products in a list
    private List<TelorModel> telorList;


    public TelorListAdapter(Context mCtx, List<TelorModel> telorlist) {
        this.mCtx = mCtx;
        this.telorList = telorlist;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public TelorListAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.telor_list, null);
        return new TelorListAdapter.OrderItemViewHolder(view, mCtx, telorList);
    }

    @Override
    public void onBindViewHolder(@NonNull TelorListAdapter.OrderItemViewHolder holder, int position) {
        TelorModel telor = telorList.get(position);
        try {
            //holder.textViewPINo.setText("PINo \n"+Integer.toString( order.getPINo()));
            holder.t_name.setText(telor.getTelorName());


        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return telorList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        AlertDialog.Builder alert;
        private Context mCtx;

        TextView t_name;
        ImageButton add;

        //we are storing all the products in a list
        private List<TelorModel> telorList;


        public OrderItemViewHolder(View itemView, final Context mCtx, List<TelorModel> telorList) {
            super(itemView);
            this.mCtx = mCtx;
            this.telorList = telorList;

//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);

            // CatelogOrderDetailModel catelogOrderDetailModel = new CatelogOrderDetailModel();
            t_name = itemView.findViewById(R.id.telorname);
            add = itemView.findViewById(R.id.updateButton);
            add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setIcon(R.drawable.ic_warning_black_24dp);
                    alertbox.setTitle("Add Customer");
                    final EditText input = new EditText(mCtx);
                    input.setHeight(100);
                    input.setWidth(340);
                    input.setGravity(Gravity.LEFT);

                    input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    alertbox.setView(input);

                    alertbox.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // finish used for destroyed activity

                            // System.exit(0);
                            postTelorList();

                        }
                    });

                    alertbox.show();
                }


                private void postTelorList() {
                    try {
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        new PostTelorDetails().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token);


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                class PostTelorDetails extends AsyncTask<String, Void, String> {
                    @Override
                    protected String doInBackground(String... params) {

                        String accesstoken = params[0];

                        String json = "";
                        try {

                            OkHttpClient client = new OkHttpClient();
                            Request.Builder builder = new Request.Builder();
                            builder.url(AppConfig.BASE_URL_API + "TelorPost/" + "TelorName/" + "OldName");
                            builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                            builder.addHeader("Accept", "application/json");
                            builder.addHeader("Authorization", "Bearer " + accesstoken);
                            FormBody.Builder parameters = new FormBody.Builder();
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
                            Toast.makeText(mCtx
                                    , jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                            if (jsonbodyres.getStatus() == true) {
                                // modifyItem(pos,um);

                                progressDialog.dismiss();
                            }

                            progressDialog.dismiss();
                        }


                    }


                }


            });
        }
    }
}