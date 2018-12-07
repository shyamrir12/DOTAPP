package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SearchDetailListActivity;

public class BottomSearchfragment extends Fragment implements View.OnClickListener {

    private TextView searchByName, searchByNumber;
    private Intent intent;
    Fragment fragment = null;
    private DataOrder orderitem;
    private CardView number_cardview,name_cardview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_search_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        orderitem = new DataOrder();
        searchByName = view.findViewById(R.id.searchbyName);
        searchByNumber = view.findViewById(R.id.searchbyNumber);
        name_cardview = view.findViewById(R.id.search_by_name_cardview);
        number_cardview = view.findViewById(R.id.search_by_number_cardview);
        searchByName.setOnClickListener(this);
        searchByNumber.setOnClickListener(this);
        number_cardview.setOnClickListener(this);
        name_cardview.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbyName:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Name");
                intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                intent = intent.putExtra("InputTypeName", "ByName");

                startActivity(intent);
                break;
            case R.id.searchbyNumber:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Number");
                intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                intent = intent.putExtra("InputTypeName", "ByNumber");
                startActivity(intent);
                break;
            case R.id.search_by_name_cardview:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Name");
                intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                intent = intent.putExtra("InputTypeName", "ByName");

                startActivity(intent);
                break;
            case R.id.search_by_number_cardview:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Number");
                intent = intent.putExtra("OrderID", String.valueOf(orderitem.OrderID));
                intent = intent.putExtra("InputTypeName", "ByNumber");
                startActivity(intent);
                break;
        }
    }
}

