package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        Class fragmentClass = null;
        handOverList = view.findViewById(R.id.handOverList);
        ReceivedList = view.findViewById(R.id.receivedItemList);


        pendinOrderListFragment = new OrderListFragment();
       // orderCreate = new AfterCreateOrderoFragment();

        handOverList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new HandOverTelorList();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();

//                Bundle bundle1 = new Bundle();
//                bundle1.putString("NAME_KEY", "Tailor detail");
//                HandOverTelorList myFragment = new HandOverTelorList();
//                myFragment.setArguments(bundle1);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
            }
        });
        ReceivedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment1= new ReceivedTelorlist();
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                transaction1.replace(R.id.fragment_container, fragment1); // fragment container id in first parameter is the  container(Main layout id) of Activity
                transaction1.addToBackStack(null);  // this will manage backstack
                transaction1.commit();

//                Bundle bundle12 = new Bundle();
//                bundle12.putString("NAME_KEY", "Select Tailor");
//                ReceivedTelorlist myFragment2 = new ReceivedTelorlist();
//                myFragment2.setArguments(bundle12);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();


            }
        });

//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.home_container, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
//        switch (v.getId()) {
//            case R.id.handOverList:
//                Fragment fragment= new HandOverTelorList();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
//                transaction.addToBackStack(null);  // this will manage backstack
//                transaction.commit();
//
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("NAME_KEY", "Tailor detail");
//                OrderListFragment myFragment = new OrderListFragment();
//                myFragment.setArguments(bundle1);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//
//                break;
//            case R.id.receivedItemList:
//
//                Fragment fragment1= new ReceivedTelorlist();
//                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
//                transaction1.replace(R.id.fragment_container, fragment1); // fragment container id in first parameter is the  container(Main layout id) of Activity
//                transaction1.addToBackStack(null);  // this will manage backstack
//                transaction1.commit();
//
//                Bundle bundle12 = new Bundle();
//                bundle12.putString("NAME_KEY", "Select Tailor");
//                OrderListFragment myFragment2 = new OrderListFragment();
//                myFragment2.setArguments(bundle12);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.home_container, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
