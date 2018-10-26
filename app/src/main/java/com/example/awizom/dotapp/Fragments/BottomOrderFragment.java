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

import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;

public class BottomOrderFragment extends Fragment implements View.OnClickListener {

    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private TextView pendingOrderList, pendingOrderCreate, cancelOrder;
    private Intent intent;
    private Fragment pendinOrderListFragment, orderCreate;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_activity_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        cardViewFirst = view.findViewById(R.id.order_pending_cardview);
        cardViewSecond = view.findViewById(R.id.order_create_cardview);
//        cardViewthird = view.findViewById(R.id.order_cancel_cardview);


        pendingOrderList = view.findViewById(R.id.pendingOrder);
        pendingOrderCreate = view.findViewById(R.id.orderCreate);
//        cancelOrder = view.findViewById(R.id.cancelOrder);


        pendinOrderListFragment = new OrderListFragment();
      //  orderCreate = new AfterCreateOrderoFragment();

        cardViewFirst.setOnClickListener(this);
        cardViewSecond.setOnClickListener(this);
//        cardViewthird.setOnClickListener(this);

        pendingOrderList.setOnClickListener(this);
        pendingOrderCreate.setOnClickListener(this);
//        cancelOrder.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
        switch (v.getId()) {

//            case R.id.order_create_cardview:
//                getActivity().setTitle("Order Create");
//                fragment = orderCreate;
//                fragmentClass = AfterCreateOrderoFragment.class;
//                break;
//            case R.id.order_pending_cardview:
//
//               intent = new Intent( getContext(), NewOrderListActivity.class );
//               intent = intent.putExtra( "FilterKey" ,"PendingOrderList");
//               startActivity( intent );
//
////                Bundle bundle2 = new Bundle();
////                bundle2.putString("NAME_KEY", "PendingOrderList");
////                OrderListFragment myFragment2 = new OrderListFragment();
////                myFragment2.setArguments(bundle2);
////                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();
//
////                getActivity().setTitle("Pending List");
////                fragment = pendinOrderListFragment;
////                fragmentClass = OrderListFragment.class;
//                break;
//            case R.id.order_cancel_cardview:
//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_KEY", "CancelOrderList");
//                OrderListFragment myFragment = new OrderListFragment();
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//
//
//                getActivity().setTitle("Cancel List");
//                fragment = pendinOrderListFragment;
//                fragmentClass = OrderListFragment.class;
//                break;
            case R.id.orderCreate:
                getActivity().setTitle("Order Create");
                intent = new Intent(getContext(), AfterCreateActivity.class);
                intent = intent.putExtra("FilterKey", "orderCreate");
                // intent = intent.putExtra( "FilterKey" ,"PendingOrderList");
                startActivity(intent);
                // getActivity().setTitle("Order Create");
                //   fragment = orderCreate;
                //  fragmentClass = AfterCreateOrderoFragment.class;

                break;
            case R.id.pendingOrder:
                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "pandingForAdv");
                intent = intent.putExtra("ButtonName","Cancel Order");
                intent = intent.putExtra("StatusName", "Cancel");
                intent = intent.putExtra("DailogMessage","Do you want to place for advance");

                startActivity(intent);
                //  getActivity().getFragmentManager().popBackStack();

//                Bundle bundle3 = new Bundle();
//                bundle3.putString("NAME_KEY", "PendingOrderList");
//                OrderListFragment myFragment3 = new OrderListFragment();
//                myFragment3.setArguments(bundle3);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment3).commit();
//
//
//                getActivity().setTitle("Pending List");
//                fragment = pendinOrderListFragment;
//                fragmentClass = OrderListFragment.class;
                break;
//            case R.id.cancelOrder:
//
//                intent = new Intent( getContext(), NewOrderListActivity.class );
//                intent = intent.putExtra( "FilterKey" ,"CancelOrderList");
//                startActivity( intent );
             /*   Bundle bundle1 = new Bundle();
                bundle1.putString("NAME_KEY", "CancelOrderList");
                OrderListFragment myFragment1 = new OrderListFragment();
                myFragment1.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.container,myFragment1).commit();
                getActivity().setTitle("Cancel List");
                fragment = pendinOrderListFragment;
                fragmentClass = OrderListFragment.class;*/
//                break;
        }
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
