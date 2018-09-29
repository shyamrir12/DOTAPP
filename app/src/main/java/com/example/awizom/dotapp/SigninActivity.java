package com.example.awizom.dotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signinButton;
    private EditText userName,passWord;
    private TextView signupHere;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
    }
    private void initView() {
        userName = findViewById(R.id.userId);
        passWord = findViewById(R.id.addreSS);
        signinButton = findViewById(R.id.signinButton);
        signinButton.setOnClickListener(this);
        signupHere = findViewById(R.id.signupHere);
        signupHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signinButton :
                if(validation()){
                    startActivity(intent = new Intent(this,HomeActivity.class));
                }
                break;
            case R.id.signupHere :
                    startActivity(intent = new Intent(this,SinUpActivity.class));
                break;
        }
    }

    private boolean validation() {
        if(userName.getText().toString().isEmpty() &&  passWord.getText().toString().isEmpty() ){
            Toast.makeText(this,"Filed can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
