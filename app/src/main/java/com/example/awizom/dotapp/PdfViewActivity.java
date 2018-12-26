package com.example.awizom.dotapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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

        File dir = new File(PdfViewActivity.this.getFilesDir() + "/ReceivedItemList.pdf");
        Uri file = Uri.fromFile(dir);
//        Uri file = Uri.parse("file:///android_asset/ReceivedItemList.pdf");



        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient());

        webview.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + file );


    }
}
