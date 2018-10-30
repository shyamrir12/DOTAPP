package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.dotapp.AfterCreateActivity;
import com.example.awizom.dotapp.Config.AppConfig;
import com.example.awizom.dotapp.Helper.SharedPrefManager;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.R;
import com.example.awizom.dotapp.SigninActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BottomOrderFragment extends Fragment implements View.OnClickListener {

    private CardView cardViewFirst, cardViewSecond, cardViewthird;
    private TextView pendingOrderList, pendingOrderCreate, cancelOrder;
    private Intent intent;
    private Fragment pendinOrderListFragment, orderCreate;
    Fragment fragment = null;
    String[] values;
    String pandingForAdv = "0";
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_activity_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSwipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                statusCountGETmethodCall();
            }
        });
        statusCountGETmethodCall();

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
                intent = intent.putExtra("StatusName", "CreateOrder ");
                // intent = intent.putExtra( "FilterKey" ,"PendingOrderList");
                startActivity(intent);
                // getActivity().setTitle("Order Create");
                //   fragment = orderCreate;
                //  fragmentClass = AfterCreateOrderoFragment.class;

                break;
            case R.id.pendingOrder:
                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "pandingForAdv");
                intent = intent.putExtra("ButtonName", "Cancel Order");
                intent = intent.putExtra("StatusName", "Cancel");
                intent = intent.putExtra("DailogMessage", "Do you want to change the status");


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

    private void statusCountGETmethodCall() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            new statusCountGET().execute(SharedPrefManager.getInstance(getContext()).getUser().access_token);
        } catch (Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }

    private class statusCountGET extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String accesstoken = params[0];

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "StatusCountGet");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                SharedPrefManager.getInstance(getContext()).logout();
                Intent login = new Intent(getContext(), SigninActivity.class);
                startActivity(login);


            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                    Toast.makeText(getContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<String[]>() {
                    }.getType();
                    values = gson.fromJson(result, listType);
                    pandingForAdv = "Panding For Advance "+ " (" + values[0].split("=")[1] + ")";
                    pendingOrderList.setText(pandingForAdv);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

}
