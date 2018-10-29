package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

        switch (v.getId()) {
            case R.id.searchbyName:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Name");
                startActivity(intent);
                break;
            case R.id.searchbyNumber:
                intent = new Intent(getContext(), SearchDetailListActivity.class);
                intent = intent.putExtra("StatusName", "Search By Number");
                startActivity(intent);
                break;
        }
    }
}

