package com.example.awizom.dotapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import com.example.awizom.dotapp.Models.Room;
import com.example.awizom.dotapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RoomNameListFragment extends Fragment {


    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<String> roomListAapter;
   List<Room> roomlist;
    RecyclerView recyclerView;
    ListView lv;
    private Button add, cancel;
    private List<String> roomArrayList;
    private EditText r_name, old_r_name;
    private String rName;
    ImageButton img;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_list_item, container, false);
        initView(view);
        return view;

    }
    private void initView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        lv = view.findViewById(R.id.roomList);
        img = view.findViewById(R.id.updateButton);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getRoomListName();
            }
        });

        getRoomListName();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.room_dialog_editor, null);
                alertbox.setView(dialogView);


                r_name = dialogView.findViewById(R.id.sNo);
                add = dialogView.findViewById(R.id.add);
                cancel = dialogView.findViewById(R.id.cancelButton);
                alertbox.setTitle("Add Room");
                final AlertDialog b = alertbox.create();
                b.show();

                r_name = dialogView.findViewById(R.id.rname);
                old_r_name = dialogView.findViewById(R.id.oldname);
                old_r_name.setVisibility(View.GONE);


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        postRoomNameList();
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

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                final AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
//                LayoutInflater inflater = getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.room_dialog_editor, null);
//                alertbox.setView(dialogView);
//                add = dialogView.findViewById(R.id.add);
//                cancel = dialogView.findViewById(R.id.cancelButton);
//
//                alertbox.setTitle("Modify room");
//                final AlertDialog b = alertbox.create();
//                b.show();
//
//                r_name = dialogView.findViewById(R.id.rname);
//                old_r_name = dialogView.findViewById(R.id.oldname);
//                r_name.setText(roomArrayList.get(position));
//                old_r_name.setHint(roomArrayList.get(position));
//                old_r_name.setVisibility(View.GONE);
//                old_r_name.setText(roomArrayList.get(position));
//
//                add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        postRoomNameList();
//                    }
//                });
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        b.dismiss();
//                    }
//                });
//            }
//        });
    }

    private void postRoomNameList() {
          rName = r_name.getText().toString();
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new PostRoomNameEdit().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token,rName.trim());


        } catch (Exception e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    class PostRoomNameEdit extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {


            String accesstoken = params[0];
            String r_name = params[1];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API  + "RoomPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("RoomName", r_name);
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
                    Type listType = new TypeToken<List<Room>>() {}.getType();
                    roomlist = new Gson().fromJson(result, listType);
                    roomArrayList = new ArrayList<String>();

                    for( Room rm : roomlist){
                        roomArrayList.add( rm.getRoomName()) ;
                    }

                    roomListAapter = new ArrayAdapter<String>(getContext(), R.layout.layout_roomlist, R.id.label, roomArrayList);
                    lv.setAdapter(roomListAapter);
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
