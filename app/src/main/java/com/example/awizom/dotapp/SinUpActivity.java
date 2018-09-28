package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SinUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signupButton;
    private EditText userName,passWord,cnfrmPassWord;
    private TextView loginHere;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_layout);
        initView();
    }

    private void initView() {
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.pwd);
        cnfrmPassWord = findViewById(R.id.cpwd);
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
        loginHere = findViewById(R.id.signupHere);
        loginHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton :
                if(validation()) {
                    startActivity(intent = new Intent(this, HomeActivity.class));
                }
                break;
            case R.id.signupHere :
                startActivity(intent = new Intent(this,SinUpActivity.class));
                break;
        }
    }

    private boolean validation() {
        if(userName.getText().toString().isEmpty() &&  passWord.getText().toString().isEmpty() &&  cnfrmPassWord.getText().toString().isEmpty()){
            Toast.makeText(this,"Filed can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }return true;
    }
}

