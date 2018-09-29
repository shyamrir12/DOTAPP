package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.R;

public class CustomerListFrgment extends Fragment{
    private  TextView orderPlace,receiVed,handOver,receivedTailor,receivedBy,mTextMessage;
    private Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_list_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}