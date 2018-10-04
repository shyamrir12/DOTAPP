package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.Adapters.OrderItemAdapter;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Models.CatelogOrderDetailModel;
import com.example.awizom.dotapp.Models.ElightBottomModel;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.RoomDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomerListFrgment extends Fragment{

    private  TextView customername,customeraddress,customercontact,interiorname,interiorcontact;
    private Intent intent;
    ProgressDialog progressDialog;
    CatelogOrderDetailModel catelogOrderDetailModel;
    List<CatelogOrderDetailModel> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
        customername = view.findViewById(R.id.customerName);
        customeraddress = view.findViewById(R.id.address);
        customercontact = view.findViewById(R.id.contact);
        interiorname = view.findViewById(R.id.interiorName);
        interiorcontact = view.findViewById(R.id.interiorContact);


    }

}