package com.example.clientsellingmedicine;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.clientsellingmedicine.Adapter.rewardPointsHistoryAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        setContentView(R.layout.forgot_password_screen);
        setContentView(R.layout.payment_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
    }
    private void addEvents() {
    }

}

