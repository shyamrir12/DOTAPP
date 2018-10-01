package com.example.awizom.dotapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.awizom.dotapp.R;

public class OrderCreateFragment extends Fragment implements View.OnClickListener {

    private EditText customerName,customerDate,customerAdvance;
    private Button addOrder;
    private Intent intent;
    private ProgressDialog progressDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_create_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        customerName = view.findViewById(R.id.customerName);
        customerDate = view.findViewById(R.id.contact);
        customerAdvance = view.findViewById(R.id.password);

        addOrder = view.findViewById(R.id.addOrder);
        addOrder.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.updateButton:
                addOrderPost();
                break;
        }
    }

    private void addOrderPost() {

    }



}
