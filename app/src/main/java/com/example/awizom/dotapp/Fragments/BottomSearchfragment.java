package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SearchDetailListActivity;

public class BottomSearchfragment extends Fragment implements View.OnClickListener {

    private TextView searchByName, searchByNumber;
    private Intent intent;

    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_search_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        searchByName = view.findViewById(R.id.searchbyName);
        searchByNumber = view.findViewById(R.id.searchbyNumber);

        searchByName.setOnClickListener(this);
        searchByNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Class fragmentClass = null;
        switch (v.getId()) {

            case R.id.searchbyName:
                getActivity().setTitle("Order Create");
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("FilterKey", "orderCreate");
                startActivity(intent);
                break;
            case R.id.searchbyNumber:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("FilterKey", "pandingForAdv");
                intent = intent.putExtra("ButtonName","Cancel Order");
                intent = intent.putExtra("StatusName", "Cancel");
                intent = intent.putExtra("DailogMessage","Do you want to change the status");
                startActivity(intent);
                break;
        }
    }
}

