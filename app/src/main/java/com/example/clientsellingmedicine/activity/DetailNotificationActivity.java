package com.example.clientsellingmedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.Notification;

import java.text.SimpleDateFormat;

public class DetailNotificationActivity extends AppCompatActivity {
    private Context mContext;
    private TextView tvDetailContentNotification, tvDetailCreateTimeNotification;
    private ImageView ivNotificationDetailItem, ivBackDetailNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.notification_detail);
        addControl();
        addEvents();
        // Get the notification object from the intent
        Notification notification = (Notification) getIntent().getSerializableExtra("notification");

        if (notification != null) {
            // Set notification details in the views
            tvDetailContentNotification.setText(notification.getContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(notification.getCreateTime());
            ivNotificationDetailItem.setImageResource(R.drawable.error_image);
            tvDetailCreateTimeNotification.setText(formattedDate); // Assuming you want to display the date as a string
        } else {
            // Handle the case where notification is null (e.g., show error message)
            Toast.makeText(this, "Failed to retrieve notification details", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvents() {
        ivBackDetailNotification.setOnClickListener(v -> {
            finish();
        });
    }

    private void addControl() {
        tvDetailContentNotification = findViewById(R.id.tvDetailContentNotification);
        tvDetailCreateTimeNotification = findViewById(R.id.tvDetailCreateTimeNotification);
        ivNotificationDetailItem = findViewById(R.id.ivNotificationDetailItem);
        ivBackDetailNotification = findViewById(R.id.ivBackDetailNotification);
    }

}
