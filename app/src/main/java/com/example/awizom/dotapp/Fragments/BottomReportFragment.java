package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.dotapp.CustomerActivity;
import com.example.awizom.dotapp.OrderBottomActivity;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.ReportActivity;
import com.example.awizom.dotapp.StatusActivity;

public class BottomReportFragment extends Fragment implements View.OnClickListener {

    private TextView pendingorder, pendingcustomer, pendingreceived, pendingitemToPlace;
    private Intent intent;
    private Fragment reportpendingOrderFragment;
    private CardView cardViewFirst, cardViewSecond, cardViewthird, cardViewFourth;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_activity_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        pendingorder = view.findViewById(R.id.pendingOrder);
        pendingcustomer = view.findViewById(R.id.pendingCustomer);
        pendingreceived = view.findViewById(R.id.pendingReceived);
        pendingitemToPlace = view.findViewById(R.id.pendingItemToPlace);

        cardViewFirst = view.findViewById(R.id.first_cardview);
        cardViewSecond = view.findViewById(R.id.second_cardview);
        cardViewthird = view.findViewById(R.id.third_cardview);
        cardViewFourth = view.findViewById(R.id.fourth_cardView);

        pendingorder.setOnClickListener(this);
        pendingcustomer.setOnClickListener(this);
        pendingreceived.setOnClickListener(this);
        pendingitemToPlace.setOnClickListener(this);

        reportpendingOrderFragment = new OrderListFragment();

    }


    @Override
    public void onClick(View v) {

        Class fragmentClass = null;
        switch (v.getId()){
            case R.id.pendingOrder:
                Bundle bundle = new Bundle();
                bundle.putString("NAME_KEY", "PendingOrderwithadvance");
                OrderListFragment myFragment = new OrderListFragment();
                myFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();

                getActivity().setTitle("Pending Order with advance");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingCustomer:
                Bundle bundle11 = new Bundle();
                bundle11.putString("NAME_KEY", "PendingCustomerwithnoadvance");
                OrderListFragment myFragment1 = new OrderListFragment();
                myFragment1.setArguments(bundle11);
                getFragmentManager().beginTransaction().replace(R.id.container,myFragment1).commit();

                getActivity().setTitle("Pending Customer with no advance");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingReceived:

                Bundle bundle12 = new Bundle();
                bundle12.putString("NAME_KEY", "PendingMaterialReceived");
                OrderListFragment myFragment2 = new OrderListFragment();
                myFragment2.setArguments(bundle12);
                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();

                getActivity().setTitle("Pending Material Received");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
                break;

            case R.id.pendingItemToPlace:

                Bundle bundle13 = new Bundle();
                bundle13.putString("NAME_KEY", "Pendingitemtoplaceholder");
                OrderListFragment myFragment3 = new OrderListFragment();
                myFragment3.setArguments(bundle13);
                getFragmentManager().beginTransaction().replace(R.id.container,myFragment3).commit();

                getActivity().setTitle("Pending item to placeholder");
                fragment = reportpendingOrderFragment;
                fragmentClass = OrderListFragment.class;
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
