package com.example.awizom.dotapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.Result;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import java.lang.reflect.Type;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TelorListFragment extends Fragment {


    //   String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
    //  ProgressDialog progressDialog;
    ListView lv;
    ImageButton img;
    // List <TelorModel> list1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Handler handler = new Handler();
    Runnable refresh;
    private Button add, cancel;
    private EditText t_name,old_t_name;
    ArrayAdapter<String> telorListAapter;
    String[] telorlist;
    private String telornamet,telorname_old;
    Document doc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telor_list_item, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        //    progressDialog = new ProgressDialog(getActivity());
        //  progressDialog.setMessage("Please wait while loading telors");
        lv = view.findViewById(R.id.telorList);
        img = view.findViewById(R.id.updateButton);
        //img2=view.findViewById(R.id.updateButton1);
        //   img3=view.findViewById(R.id.updateButton2);
        //     lv = view.findViewById(R.id.telorList);
        doc = new Document();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.telor_dialog, null);
                alertbox.setView(dialogView);

                t_name = dialogView.findViewById(R.id.sNo);
                add = dialogView.findViewById(R.id.add);
                cancel = dialogView.findViewById(R.id.cancelButton);
                alertbox.setTitle("Add telor");
                final AlertDialog b = alertbox.create();
                b.show();

                //   telorname=t_name.getText().toString();


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        postTelorList();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();


                    }
                });

            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getTelorList();
            }
        });
        getTelorList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.telor_dialog_edittelor, null);
                alertbox.setView(dialogView);

                t_name = dialogView.findViewById(R.id.sNo);
                old_t_name = dialogView.findViewById(R.id.oldname);


                add = dialogView.findViewById(R.id.add);
                cancel = dialogView.findViewById(R.id.cancelButton);
                alertbox.setTitle("Modify telor");
                final AlertDialog b = alertbox.create();
                b.show();


                t_name.setText(telorlist[position]);
                old_t_name.setHint(telorlist[position]);
                old_t_name.setVisibility(View.GONE);
                old_t_name.setText(telorlist[position]);


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postTelorListEdit();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();

                    }
                });


            }
        });
    }


    private void postTelorListEdit() {
        telornamet = t_name.getText().toString();
        telorname_old = old_t_name.getText().toString();

        try {

            new PostTelorDetailsEdit().execute(telornamet.trim(), telorname_old.trim(), SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class PostTelorDetailsEdit extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String telorname = params[0];
            String telorname_old = params[1];
            String accesstoken = params[2];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "TelorPost/" + telorname + "/" + telorname_old);
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
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                //progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getContext()
                        , jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    // modifyItem(pos,um);
                }
            }
        }
    }


    private void postTelorList() {
        telornamet = t_name.getText().toString();

        try {
            //progressDialog.setMessage("loading...");
            //progressDialog.show();
            new PostTelorDetails().execute(telornamet.trim(), SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            //progressDialog.dismiss();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    class PostTelorDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String telorname = params[0];
            String accesstoken = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "TelorPost/" + telorname + "/" + "blank");
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
                //  progressDialog.dismiss();
                Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                //progressDialog.dismiss();
                Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getContext()
                        , jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                if (jsonbodyres.getStatus() == true) {
                    // modifyItem(pos,um);

                    //  progressDialog.dismiss();
                }

                //progressDialog.dismiss();
            }


        }


    }


    private void getTelorList() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new GetTelorDetails().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private class GetTelorDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "TelorListGet");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Length", "0");
                builder.addHeader("Authorization", "Bearer " + accesstoken);

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
            if (result.isEmpty()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<String[]>() {
                }.getType();
                telorlist = new Gson().fromJson(result, listType);
                telorListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_button_telorlist, R.id.label, telorlist);
                lv.setAdapter(telorListAapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }catch(Exception e){
                e.printStackTrace();
            }

        }
    }


}
