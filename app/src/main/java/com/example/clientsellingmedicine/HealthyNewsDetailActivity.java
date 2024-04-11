package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class HealthyNewsDetailActivity extends AppCompatActivity {
    private Context mContext;
    WebView wvHealthyNews;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.healthy_news_detail);

        addControl();
        addEvents();

    }

    private void addControl() {
    }

    private void addEvents() {
    }

}
