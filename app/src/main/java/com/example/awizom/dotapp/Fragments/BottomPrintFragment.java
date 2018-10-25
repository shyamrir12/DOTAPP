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

public class BottomPrintFragment extends Fragment implements View.OnClickListener{

    private TextView handOverList, ReceivedList;
    private Intent intent;
    private Fragment pendinOrderListFragment, orderCreate;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_print_fragment_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        handOverList = view.findViewById(R.id.handOverList);
        ReceivedList = view.findViewById(R.id.receivedItemList);


        pendinOrderListFragment = new OrderListFragment();
        orderCreate = new AfterCreateOrderoFragment();

        handOverList.setOnClickListener(this);
        ReceivedList.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
        switch (v.getId()) {
            case R.id.orderCreate:


                break;
            case R.id.pendingOrder:


                startActivity(intent);
        }
    }
}
