package com.example.awizom.dotapp.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Fragments.UserListFragment;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.Models.UserModel;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.OrderItemViewHolder> {
    Boolean bln;
    private Context mCtx;
    ProgressDialog progressDialog;
    String userId;
    int pos;
    UserModel um;
    private Fragment userListFragment;
    Fragment fragment = null;

    String activeuser = "False";
    //we are storing all the products in a list
    private List<UserModel> useritemList;


    public UserListAdapter(Context mCtx, List<UserModel> useritemList) {
        this.mCtx = mCtx;
        this.useritemList = useritemList;
        userListFragment = new UserListFragment();
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_list, null);
        return new OrderItemViewHolder(view, mCtx, useritemList);


    }


    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {

        UserModel user = useritemList.get(position);
        try {

            holder.username.setText(user.getUserName());


            holder.roleid.setText(user.getRoleId());

            if (user.isActive())
                holder.active.setText("De-Activate");
            else
                holder.active.setText("Activate");


            holder.userid.setText(user.getUserId());


            // holder.active.setText(toString(user.isActive()));

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private String toString(boolean active) {


        return active ? "true" : "false";
    }


    @Override
    public int getItemCount() {
        return useritemList.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final Context mCtx;
        TextView username, roleid, userid;

        Button active;
        private List<UserModel> useritemList;


        public OrderItemViewHolder(View view, Context mCtx, List<UserModel> useritemList) {
            super(view);
            this.mCtx = mCtx;
            this.useritemList = useritemList;

            itemView.setOnClickListener(this);

            username = itemView.findViewById(R.id.username);
            roleid = itemView.findViewById(R.id.roleid);
            userid = itemView.findViewById(R.id.userid);
            active = itemView.findViewById(R.id.btn1);

            active.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Class fragmentClass = null;
            int position = getAdapterPosition();
            UserModel useritem = this.useritemList.get(position);
            // pos=position;
            //um=useritem;
            if (v.getId() == active.getId()) {
                userId = useritem.getUserId();
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("are you sure want to active");
                alertbox.setTitle("Active Status");
                alertbox.setIcon(R.drawable.admin);

                alertbox.setNeutralButton("Yes",
                        new DialogInterface.OnClickListener() {
                            Class fragmentClass = null;


                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                if (active.getText().equals("Activate"))
                                    activeuser = "True";
                                postUserList();


                            }


                        });
                alertbox.setPositiveButton("No", null);

                alertbox.show();
            }


        }

        public void active(boolean active) {


        }
    }


    private void postUserList() {
        try {
            progressDialog.setMessage("loading...");
            progressDialog.show();
            new PostUserDetails().execute(SharedPrefManager.getInstance(mCtx).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class PostUserDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "UserActivePost/" + userId + "/" + activeuser);
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


    public void modifyItem(final int position, final UserModel model) {
        useritemList.set(position, model);
        notifyItemChanged(position);

    }

}

