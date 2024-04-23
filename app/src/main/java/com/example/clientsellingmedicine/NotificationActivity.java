package com.example.clientsellingmedicine;

import android.app.DatePickerDialog;
import android.content.Context;
// Imports for UI elements and RecyclerView
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
// Imports for UI elements
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.clientsellingmedicine.Adapter.notificationAdapter;
// Import for your reward points history adapter (if applicable)
import com.example.clientsellingmedicine.Adapter.orderAdapter;
import com.example.clientsellingmedicine.Adapter.orderDetailAdapter;
import com.example.clientsellingmedicine.Adapter.rewardPointsHistoryAdapter;
import com.example.clientsellingmedicine.interfaces.IOnNotificationItemClickListener;
import com.example.clientsellingmedicine.models.Notification;
import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.services.NotificationService;
import com.example.clientsellingmedicine.services.OrderService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements IOnNotificationItemClickListener {
    private LinearLayout layoutEmptyNotification;

    private Context mContext;
    private RecyclerView rcvNotification;
    private List<Notification> notificationList;
    private ImageView ivBackNotification;
    private TextView tvReadAllNotification;
    private notificationAdapter mAdapter;

    IOnNotificationItemClickListener iOnNotificationItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // Set the correct layout for notifications
        setContentView(R.layout.notification_screen);
        addControl();
        addEvents();
    }

    private void addControl() {
        layoutEmptyNotification = findViewById(R.id.layoutEmptyNotification);
        rcvNotification = findViewById(R.id.rcvNotification);
        ivBackNotification = findViewById(R.id.ivBackNotification);
        tvReadAllNotification = findViewById(R.id.tvReadAllNotification);
        iOnNotificationItemClickListener = this;
    }

    private void addEvents() {
        ivBackNotification.setOnClickListener(v -> {
            finish();
        });
        tvReadAllNotification.setOnClickListener(v -> {
            updateAllNotification();
        });
        getNotification();
    }

    private void updateAllNotification() {
        NotificationService notificationService = ServiceBuilder.buildService(NotificationService.class);
        Call<Notification> request = notificationService.updateAllNotification();
        request.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    if (mAdapter != null) {
                        getNotification();
                        mAdapter.setmNotifications(notificationList); // Assuming your adapter has an updateData method
                    }
                    Toast.makeText(mContext, "Đọc hết thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Thất bại!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getNotification() {
        NotificationService notificationService = ServiceBuilder.buildService(NotificationService.class);
        Call<List<Notification>> request = notificationService.getNotification();
        request.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    notificationList = new ArrayList<>();
                    notificationList = response.body().stream() //get list order
                            .collect(Collectors.toList());
                    if(notificationList != null && notificationList.size() > 0) {
                        // Check for notifications and set visibility accordingly
                        rcvNotification.setVisibility(View.VISIBLE);
                        layoutEmptyNotification.setVisibility(View.GONE);
                        // Create and set adapter for the RecyclerView
                        mAdapter = new notificationAdapter(notificationList, iOnNotificationItemClickListener);
                        rcvNotification.setAdapter(mAdapter);
                        rcvNotification.setLayoutManager(new LinearLayoutManager(mContext));
                    }
                    else {
                        rcvNotification.setVisibility(View.GONE);
                        layoutEmptyNotification.setVisibility(View.VISIBLE);
                    }

                } else if (response.code() == 401) {
                    rcvNotification.setVisibility(View.GONE);
                    layoutEmptyNotification.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public void onItemClick(Notification notification) {
        updateNotification(notification);
        open(notification);
    }

    private void updateNotification(Notification notification) {
        NotificationService notificationService = ServiceBuilder.buildService(NotificationService.class);
        Call<Notification> updateRequest = notificationService.updateNotification(notification.getId());

        updateRequest.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    // Update notification list and UI (optional, based on your needs)
                    Toast.makeText(mContext, "Notification updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to update notification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to update notification", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void open(Notification notification) {
        Intent intent = new Intent(mContext, DetailNotificationActivity.class); // Replace with your actual activity class
        intent.putExtra("notification", notification); // Key to pass the notification object
        startActivity(intent);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getNotification();
        // Khôi phục trạng thái của activity
        // Cập nhật giao diện người dùng
        // Chuẩn bị cho việc tương tác của người dùng
    }
}
