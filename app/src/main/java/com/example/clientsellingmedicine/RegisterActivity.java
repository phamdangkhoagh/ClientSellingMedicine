package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.register_screen);

        addControl();
        addEvents();

    }

    private void addControl() {}
    private void addEvents() {}
}
