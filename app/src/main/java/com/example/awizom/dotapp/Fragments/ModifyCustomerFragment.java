package com.example.awizom.dotapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.awizom.dotapp.R;

public class ModifyCustomerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_edit_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}
