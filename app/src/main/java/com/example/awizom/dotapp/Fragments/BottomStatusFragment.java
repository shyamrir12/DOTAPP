package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SigninActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BottomStatusFragment extends Fragment implements View.OnClickListener {

    private TextView pendingttoPlaceOrder, holD, handOverto, dispatch, pendingToreceivedMaterial, pendingtorecevefrometailor, cancelList, dispatchList;
    private Intent intent;
    private Fragment statuspendingOrderFragment;
    Fragment fragment = null;
    DataOrder orderitem;
    private String handOverToListSpinnerData[] = {"Telor", "Sofa Karigar", "Self Customer", "Wallpaper fitter"};
    private ProgressDialog progressDialog;
    private String countValue = "";
    private TextView countValuependingToHandOverShow,countValueDispatchShow,
            countValueHoldShow,countValueReceeMaterialShow,countValuependingToRefromtailorShow,countValuePlaceHolderShow;
    private String[] countValueSplitData;
    String[] values;
    String PandingToPlaceOrder="0",Hold="0",PandingToReceiveMaterial="0",PandingToHandOverTo="0",PandingToReceivedFromTelor="0",Dispatch="0";
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mSwipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                statusCountGETmethodCall();
            }
        });
        statusCountGETmethodCall();

        pendingttoPlaceOrder = view.findViewById(R.id.pendingtToPlaceOrder);
        holD = view.findViewById(R.id.hold);
        handOverto = view.findViewById(R.id.handOverTo);
        dispatch = view.findViewById(R.id.dispatch);
        pendingToreceivedMaterial = view.findViewById(R.id.pendingToreceivedMaterial);
        pendingtorecevefrometailor = view.findViewById(R.id.receivedFromTailor);
        pendingttoPlaceOrder.setOnClickListener(this);
        holD.setOnClickListener(this);
        handOverto.setOnClickListener(this);
        dispatch.setOnClickListener(this);
        pendingToreceivedMaterial.setOnClickListener(this);
        pendingtorecevefrometailor.setOnClickListener(this);
        statuspendingOrderFragment = new CustomerListFrgment();

    }

    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
        switch (v.getId()) {
            case R.id.pendingtToPlaceOrder:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "PandingToPlaceOrder");
                intent = intent.putExtra("ButtonName", "Place Order");
                intent = intent.putExtra("StatusName", "OrderPlaced");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");


                startActivity(intent);
                break;

            case R.id.hold:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "Hold");
                intent = intent.putExtra("ButtonName", "Place Order");
                intent = intent.putExtra("StatusName", "OrderPlaced");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");

                startActivity(intent);
                break;


            case R.id.handOverTo:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "PandingToHandOverTo");
                intent = intent.putExtra("ButtonName", "HandOverTo");
                intent = intent.putExtra("StatusName", "HandOverTo");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");

                startActivity(intent);
                break;

            case R.id.receivedFromTailor:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "PandingToReceivedFromTelor");
                intent = intent.putExtra("ButtonName", "Received");
                intent = intent.putExtra("StatusName", "ReceivedFromTelor");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");

                startActivity(intent);
                break;

            case R.id.pendingToreceivedMaterial:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "PandingToReceiveMaterial");
                intent = intent.putExtra("ButtonName", "Received Order");
                intent = intent.putExtra("StatusName", "MaterialReceived");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");

                startActivity(intent);
                break;

            case R.id.dispatch:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "Dispatch");
                intent = intent.putExtra("ButtonName", "Reset");
                intent = intent.putExtra("StatusName", "Reset");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");

                startActivity(intent);
                break;

        }

    }

    private void showDailog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.handover_to_show_telo, null);
        dialogBuilder.setView(dialogView);

        final Spinner handOvertoNameSpinner = dialogView.findViewById(R.id.handOverToNameSpinner);
        final Spinner tailorListNameSpinner = dialogView.findViewById(R.id.tailorListSpinner);
        final Button buttonOk = dialogView.findViewById(R.id.buttonOk);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, handOverToListSpinnerData);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        handOvertoNameSpinner.setAdapter(spinnerArrayAdapter);
        progressDialog = new ProgressDialog(getContext());

        dialogBuilder.setTitle("Hand Over List");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.dismiss();

            }


        });


    }

    private void statusCountGETmethodCall() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new statusCountGET().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class statusCountGET extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = params[0];

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "StatusCountGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                SharedPrefManager.getInstance(getContext()).logout();
                Intent login = new Intent(getContext(), SigninActivity.class);
                startActivity(login);


            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                    Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<String[]>(){}.getType();
                    values= gson.fromJson(result, listType);

                    PandingToPlaceOrder ="Panding To Place Order "+" ("+ values[1].split("=")[1] +")";
                    pendingttoPlaceOrder.setText(PandingToPlaceOrder);

                    Hold ="Hold"+" ("+ values[2].split("=")[1] +")";
                    holD.setText(Hold);

                    PandingToReceiveMaterial ="Panding To Receive Material "+" ("+ values[3].split("=")[1] +")";
                    pendingToreceivedMaterial.setText(PandingToReceiveMaterial);

                    PandingToHandOverTo ="Panding To Hand Over "+" ("+ values[4].split("=")[1] +")";
                    handOverto.setText(PandingToHandOverTo);

                    PandingToReceivedFromTelor ="Panding To Received "+" ("+ values[5].split("=")[1] +")";
                    pendingtorecevefrometailor.setText(PandingToReceivedFromTelor);

                    Dispatch ="Dispatch "+" ("+ values[6].split("=")[1] +")";
                    dispatch.setText(Dispatch);
                    mSwipeRefreshLayout.setRefreshing(false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }


}
