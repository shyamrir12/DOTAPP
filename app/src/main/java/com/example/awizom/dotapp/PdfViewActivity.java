package com.example.awizom.dotapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        initView();
    }

    private void initView() {
        webview = findViewById(R.id.pdfView);


        String path = getApplicationContext().getExternalFilesDir(
                Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath()+ "/ReceivedItemList.pdf";
          webview.getSettings().setJavaScriptEnabled(true);
           webview.getSettings().setAllowFileAccess(true);




    }
}
