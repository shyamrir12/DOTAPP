package com.example.awizom.dotapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Models.DataOrder;
import com.example.awizom.dotapp.NewOrderListActivity;
import com.example.awizom.dotapp.OrderActivity;
import com.example.awizom.dotapp.R;

public class BottomStatusFragment extends Fragment implements View.OnClickListener {

    private TextView pendingttoPlaceOrder, holD, handOverto, receivedby, pendingToreceivedMaterial, pendingtorecevefrometailor, cancelList, dispatchList;
    private Intent intent;
    private Fragment statuspendingOrderFragment;
    Fragment fragment = null;
    DataOrder orderitem;
    private static final String[] paths = {"Telor", "Sofa Karigar", "Self Customer", "wallpaper fitter"};
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
        receivedby = view.findViewById(R.id.dispatch);
        pendingToreceivedMaterial = view.findViewById(R.id.pendingToreceivedMaterial);
       pendingtorecevefrometailor = view.findViewById(R.id.receivedFromTailor);


        pendingttoPlaceOrder.setOnClickListener(this);
        holD.setOnClickListener(this);
        handOverto.setOnClickListener(this);
       receivedby.setOnClickListener(this);
        pendingToreceivedMaterial.setOnClickListener(this);
        pendingtorecevefrometailor.setOnClickListener(this);
          orderitem= new DataOrder();

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
                intent = intent.putExtra("StatusName", "OrderPlaced");
                startActivity(intent);
                break;

            case R.id.hold:

                intent = new Intent(getContext(), NewOrderListActivity.class);
                intent = intent.putExtra("FilterKey", "Hold");
                intent = intent.putExtra("ButtonName","Place Order");
                startActivity(intent);
                break;


            case R.id.handOverTo:
                showDailog();



//                intent = new Intent( getContext(), NewOrderListActivity.class );
//                intent = intent.putExtra( "FilterKey" ,"PandingToHandOverTo");
//                intent = intent.putExtra("ButtonName","");
//                startActivity( intent );


                break;

            case R.id.receivedFromTailor:

                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"PandingToReceivedFromTelor");
                intent = intent.putExtra("ButtonName","Received");
                intent = intent.putExtra("StatusName", "ReceivedFromTalor");
                startActivity( intent );
                break;

            case R.id.pendingToreceivedMaterial:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"PandingToReceiveMaterial");
                intent = intent.putExtra("ButtonName","Received Order");
                startActivity( intent );
                break;

            case R.id.dispatch:
                intent = new Intent( getContext(), NewOrderListActivity.class );
                intent = intent.putExtra( "FilterKey" ,"Dispatch");
                intent = intent.putExtra("ButtonName","Reset");
                startActivity( intent );
                break;

        }

    }

    private void showDailog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.handover_to_show_telo, null);
        dialogBuilder.setView(dialogView);

        final Spinner handOvertoNameSpinner =  dialogView.findViewById(R.id.handOverToNameSpinner);
        final Spinner tailorListNameSpinner =  dialogView.findViewById(R.id.tailorListSpinner);
        final Button buttonOk = dialogView.findViewById(R.id.buttonOk);


        dialogBuilder.setTitle("Tailor List");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                b.dismiss();

            }


        });



    }


}
