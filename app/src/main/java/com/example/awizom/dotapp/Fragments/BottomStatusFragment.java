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

import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;

public class BottomStatusFragment extends Fragment implements View.OnClickListener {

    private TextView pendingttoPlaceOrder, holD, handOverto, receivedby, pendingToreceivedMaterial, pendingtorecevefrometailor, cancelList, dispatchList;
    private Intent intent;
    private Fragment statuspendingOrderFragment;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        pendingttoPlaceOrder = view.findViewById(R.id.pendingtToPlaceOrder);
        holD = view.findViewById(R.id.hold);
       handOverto = view.findViewById(R.id.handOverTo);
        receivedby = view.findViewById(R.id.receivedby);
        pendingToreceivedMaterial = view.findViewById(R.id.pendingToreceivedMaterial);
       pendingtorecevefrometailor = view.findViewById(R.id.receivedFromTailor);
//        cancelList = view.findViewById(R.id.cancel);
//        dispatchList = view.findViewById(R.id.dispatch);

        pendingttoPlaceOrder.setOnClickListener(this);
        holD.setOnClickListener(this);
        handOverto.setOnClickListener(this);
       receivedby.setOnClickListener(this);
//        pendingtoPlaceOrder.setOnClickListener(this);
        pendingToreceivedMaterial.setOnClickListener(this);
        pendingtorecevefrometailor.setOnClickListener(this);
//        cancelList.setOnClickListener(this);
//        dispatchList.setOnClickListener(this);

        statuspendingOrderFragment = new CustomerListFrgment();
    }

    @Override
    public void onClick(View v) {
        Class fragmentClass = null;
        switch (v.getId()) {
            case R.id.pendingtToPlaceOrder:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "PandingToPlaceOrder");
                intent = intent.putExtra("ButtonName","Place Order");
                startActivity(intent);

//                Bundle bundle = new Bundle();
//                bundle.putString("NAME_KEY", "HandOverTo");
//                OrderListFragment myFragment = new OrderListFragment();
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//                getActivity().setTitle("Pending Order with advance");
//                fragment = statuspendingOrderFragment;
//                fragmentClass = OrderListFragment.class;
                break;
//
            case R.id.hold:
                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "Hold");
                startActivity(intent);

////                Bundle bundle11 = new Bundle();
////                bundle11.putString("NAME_KEY", "TelorName");
////                OrderListFragment myFragment1 = new OrderListFragment();
////                myFragment1.setArguments(bundle11);
////                getFragmentManager().beginTransaction().replace(R.id.container,myFragment1).commit();
//
                break;

            case R.id.handOverTo:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"PandingToHandOverTo");
                startActivity( intent );

//                Bundle bundle12 = new Bundle();
//                bundle12.putString("NAME_KEY", "PendingReceivedBy");
//                OrderListFragment myFragment2 = new OrderListFragment();
//                myFragment2.setArguments(bundle12);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment2).commit();

                break;

            case R.id.receivedFromTailor:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"PandingToReceivedFromTelor");
                startActivity( intent );

//                Bundle bundle13 = new Bundle();
//                bundle13.putString("NAME_KEY", "Pendingitemtoplaceholder");
//                OrderListFragment myFragment3 = new OrderListFragment();
//                myFragment3.setArguments(bundle13);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment3).commit();

                break;

            case R.id.pendingToreceivedMaterial:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"PandingToPlaceOrder");
                startActivity( intent );

//                Bundle bundle14 = new Bundle();
//                bundle14.putString("NAME_KEY", "PendingMaterialReceived");
//                OrderListFragment myFragment4 = new OrderListFragment();
//                myFragment4.setArguments(bundle14);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment4).commit();
                break;

            case R.id.receivedby:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"Dispatch");
                startActivity( intent );


//                Bundle bundle15 = new Bundle();
//                bundle15.putString("NAME_KEY", "CancelOrderlist");
//                OrderListFragment myFragment5 = new OrderListFragment();
//                myFragment5.setArguments(bundle15);
//                getFragmentManager().beginTransaction().replace(R.id.container,myFragment5).commit();
                break;
//
//            case R.id.dispatch:
//                intent = new Intent( getContext(), NewOrderListActivity.class );
//                intent = intent.putExtra( "FilterKey" ,"Dispatch");
//                startActivity( intent );
//
////                Bundle bundle16 = new Bundle();
////                bundle16.putString("NAME_KEY", "Dispatch");
////                OrderListFragment myFragment6 = new OrderListFragment();
////                myFragment6.setArguments(bundle16);
////                getFragmentManager().beginTransaction().replace(R.id.container,myFragment6).commit();
//                break;

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
