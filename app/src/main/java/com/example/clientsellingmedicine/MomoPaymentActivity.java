package com.example.clientsellingmedicine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.models.MomoResponse;

import java.net.URISyntaxException;

public class MomoPaymentActivity extends AppCompatActivity {
    private Context mContext;
    WebView wv_payment;

    ProgressBar progressBar;
    ImageView ivBack;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.momo_payment);

        addControl();
        addEvents();

    }

    private void addControl() {
        wv_payment = findViewById(R.id.wv_payment);
        ivBack = findViewById(R.id.ivBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void addEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });


        Intent intent = getIntent();
        MomoResponse momoResponse = ( MomoResponse) intent.getSerializableExtra("momoResponse");
        wv_payment.getSettings().setLoadWithOverviewMode(true);
        wv_payment.getSettings().setUseWideViewPort(true);
        wv_payment.loadUrl(momoResponse.getUrlPayment());
        wv_payment.getSettings().setJavaScriptEnabled(true);
        wv_payment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Trang web bắt đầu tải
                progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                // Trang web đã sẵn sàng để hiển thị
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("momo://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                            return true;
                        }
                    } catch (URISyntaxException | ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }



        });
    }

}
