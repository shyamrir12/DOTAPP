package com.example.awizom.dotapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomNameListFragment extends Fragment {


    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<String> roomListAapter;
    String[] roomlist;
    private ListView lv;

    private EditText t_name, old_t_name;
    private Button add, cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telor_list_item, container, false);
        initView(view);
        return view;

    }
    private void initView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        lv = view.findViewById(R.id.roomList);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getRoomListName();
            }
        });

        getRoomListName();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.room_dialog_editor, null);
                alertbox.setView(dialogView);

                t_name = dialogView.findViewById(R.id.sNo);
                old_t_name = dialogView.findViewById(R.id.oldname);


                add = dialogView.findViewById(R.id.add);
                cancel = dialogView.findViewById(R.id.cancelButton);
                alertbox.setTitle("Modify Room");
                final AlertDialog b = alertbox.create();
                b.show();


                t_name.setText(roomlist[position]);
                old_t_name.setHint(roomlist[position]);
                old_t_name.setVisibility(View.GONE);
                old_t_name.setText(roomlist[position]);


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // postTelorListEdit();
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

    private void getRoomListName() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new GetRoomListName().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);


        } catch (Exception e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetRoomListName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String accesstoken = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "RoomGet");
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
                    roomlist = new Gson().fromJson(result, listType);
                    roomListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_roomlist, R.id.label, roomlist);
                    lv.setAdapter(roomListAapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
