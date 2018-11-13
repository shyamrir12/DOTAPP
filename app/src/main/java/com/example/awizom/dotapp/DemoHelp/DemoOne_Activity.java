package com.example.awizom.dotapp.DemoHelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.awizom.dotapp.R;

public class DemoOne_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button next;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_image);
        initView();
    }

    private void initView() {
        next = findViewById(R.id.nextButton);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        startActivity(intent = new Intent(this,DemoTwoActivity.class));

    }
}
