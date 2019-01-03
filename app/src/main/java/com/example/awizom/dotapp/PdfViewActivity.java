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
    private String PDFName;
    File dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        initView();
    }

    private void initView() {

        webview = findViewById(R.id.pdfView);
        PDFName = getIntent().getExtras().getString("PDFName","");

        if(PDFName.contains("/OrderItemList.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        } else if(PDFName.contains("/HandoverItemList.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        }else if(PDFName.contains("/ReceivedItemList.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        }

        else if(PDFName.contains("/OrderItemAdapter.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        } else if(PDFName.contains("/OrderListAdapter.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        }

        else if(PDFName.contains("/NewOrderList.pdf")){
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);
        }

        else {
            dir = new File(PdfViewActivity.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + PDFName);

        }

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
