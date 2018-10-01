package com.example.awizom.dotapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ReportPendingOrderFragment extends Fragment implements View.OnClickListener {

    private EditText cName,cContact,cAddress,interioName,interioContact;
    private Button addCustomer,cancelCustomer;
    private Intent intent;
    private ProgressDialog progressDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_add_layout, container, false);
        initView(view);
        return view;

    }
    private void initView(View view) {
        cName = view.findViewById(R.id.customerName);
        cContact = view.findViewById(R.id.contact);
        cAddress = view.findViewById(R.id.password);
        interioName = view.findViewById(R.id.confrmPassword);
        interioContact = view.findViewById(R.id.interiormobile);

        addCustomer = view.findViewById(R.id.updateButton);
        cancelCustomer = view.findViewById(R.id.cancelButton);
        addCustomer.setOnClickListener(this);
        cancelCustomer.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.updateButton:
                customerAddPost();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }
    }

    private void finish() {
        return;
    }

    private void customerAddPost() {

        String name = cName.getText().toString().trim();
        String contact = cContact.getText().toString().trim();
        String address = cAddress.getText().toString().trim();
        String intename = interioName.getText().toString().trim();
        String intecontact = interioContact.getText().toString().trim();


    }



}