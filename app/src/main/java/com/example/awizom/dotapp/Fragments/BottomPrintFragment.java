package com.example.awizom.dotapp.Fragments;

import android.app.ActionBar;
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

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;

public class BottomPrintFragment extends Fragment implements View.OnClickListener {

    private TextView handOverList, ReceivedList;
    private Intent intent;
    private Fragment handoverFragment, tailorListfragment;
    Fragment fragment = null;
    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_print_fragment_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Class fragmentClass = null;
        handOverList = view.findViewById(R.id.handOverList);
        ReceivedList = view.findViewById(R.id.receivedItemList);

        cardViewFirst = view.findViewById(R.id.order_create_cardview);
        cardViewSecond = view.findViewById(R.id.order_pending_cardview);

        handOverList.setOnClickListener(this);
        cardViewSecond.setOnClickListener(this);

        cardViewFirst.setOnClickListener(this);
        ReceivedList.setOnClickListener(this);

        handoverFragment = new HandOverTelorList();
        tailorListfragment = new TelorListFragment();


        //    pendinOrderListFragment = new OrderListFragment();
        // orderCreate = new AfterCreateOrderoFragment();
//
//        handOverList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment= new HandOverTelorList();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
//                transaction.addToBackStack(null);  // this will manage backstack
//                transaction.commit();
//
//            }
//        });
//        ReceivedList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                getActivity().setTitle("Telor");
//
//                Fragment fragment1= new ReceivedTelorlist();
//                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
//                transaction1.replace(R.id.fragment_container, fragment1); // fragment container id in first parameter is the  container(Main layout id) of Activity
//                transaction1.addToBackStack(null);  // this will manage backstack
//                transaction1.commit();
//
//
//            }
    }

    @Override
    public void onClick(View v) {

        Class fragmentClass = null;
        switch (v.getId()) {

            case R.id.order_create_cardview:
                getActivity().setTitle("Handover");
                fragmentClass = HandOverTelorList.class;
                break;

            case R.id.handOverList:
                // getActivity().setTitle("Handover Telor List");
//
//                Bundle bundle2 = new Bundle();
//                bundle2.putString("NAME_KEY", "Handover Telor List");
//                HandOverTelorList myFragment2 = new HandOverTelorList();
//               myFragment2.setArguments(bundle2);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();
//
                getActivity().setTitle("Handover");
                fragmentClass = HandOverTelorList.class;
                break;

            case R.id.order_pending_cardview:
                fragmentClass = ReceivedTelorlist.class;
                break;
            case R.id.receivedItemList:
                //               getActivity().setTitle("Telor List");

//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_KEY", "Handover Telor List");
//                HandOverTelorList myFragment = new HandOverTelorList();
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//
                fragmentClass = ReceivedTelorlist.class;
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
