package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CouponActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.coupon_screen);

        addControl();
        addEvents();

    }

    private void addControl() {}
    private void addEvents() {}
}
