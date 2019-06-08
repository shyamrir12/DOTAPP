package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.awizom.dotapp.R;

public class BottomCustomerFragment extends Fragment implements View.OnClickListener {

    private TextView customerList, customerModify, customerAdd;
    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private Intent intent;
    private Fragment addCustomerFragment, modifyCustomerFragment, listCustomerFragment;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_activity_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        customerList = view.findViewById(R.id.customerList);
        customerModify = view.findViewById(R.id.modifyCustomer);
        customerAdd = view.findViewById(R.id.addCustomer);


        cardViewFirst = view.findViewById(R.id.first_cardview);
        cardViewSecond = view.findViewById(R.id.second_cardview);
        cardViewthird = view.findViewById(R.id.third_cardview);

        addCustomerFragment = new AddCustomerFragment();
        modifyCustomerFragment = new ModifyCustomerFragment();
        listCustomerFragment = new CustomerListFrgment();

        customerList.setOnClickListener(this);
        customerModify.setOnClickListener(this);
        customerAdd.setOnClickListener(this);

        cardViewFirst.setOnClickListener(this);
        cardViewSecond.setOnClickListener(this);
        cardViewthird.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        Class fragmentClass = null;
        switch (v.getId()) {

            case R.id.first_cardview:
                fragmentClass = AddCustomerFragment.class;
                break;
            case R.id.second_cardview:
                getActivity().setTitle("Modify Customer");
                fragment = modifyCustomerFragment;
                fragmentClass = ModifyCustomerFragment.class;
                break;
            case R.id.third_cardview:
                getActivity().setTitle("Customer List");
                fragmentClass = CustomerListFrgment.class;
                break;

//            case R.id.orderCreate:
//                if ((SharedPrefManager.getInstance(getActivity()).getUser().getUserRole().contains("Admin")) ||
//                        (SharedPrefManager.getInstance(getActivity()).getUser().getUserRole().contains("User"))) {
//                    getActivity().setTitle("Add Customer");
//                    fragmentClass = AddCustomerFragment.class;
//
//                } else {
//                    Toast.makeText(getActivity(), "User Is Not Permitted", Toast.LENGTH_SHORT).show();
//                }
//                break;

            case R.id.modifyCustomer:
                getActivity().setTitle("Modify Customer");
                fragment = modifyCustomerFragment;
                fragmentClass = ModifyCustomerFragment.class;
                break;

            case R.id.customerList:
                getActivity().setTitle("Customer List");
                fragmentClass = CustomerListFrgment.class;
                break;


            case R.id.addCustomer:
                fragmentClass = AddCustomerFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
