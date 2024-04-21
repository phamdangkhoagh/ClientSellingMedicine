package com.example.clientsellingmedicine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.models.OrderDetail;
import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.Adapter.orderDetailAdapter;
import com.example.clientsellingmedicine.services.OrderService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.services.UserService;
import com.example.clientsellingmedicine.utils.Convert;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private Context mContext;
    private TextView tv_userName, tv_Phone, tv_Address, tv_orderTime,
            tv_totalPrice, tv_orderCode, tv_paymentMethod, tv_totalPoint, tv_totalPayment;
    private ImageView iv_back;
    private RecyclerView rcvOrderDetail;

    private orderDetailAdapter orderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.order_detail);

        addControl();
        addEvents();

    }

    private void addEvents() {
        iv_back.setOnClickListener(v -> {
            finish();
        });

        loadData();
    }

    private void addControl() {
        iv_back = findViewById(R.id.iv_back);
        
        tv_userName = findViewById(R.id.tv_userName);
        tv_Phone = findViewById(R.id.tv_Phone);
        tv_Address = findViewById(R.id.tv_Address);
        tv_orderTime = findViewById(R.id.tv_orderTime);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_orderCode = findViewById(R.id.tv_orderCode);
        tv_paymentMethod = findViewById(R.id.tv_paymentMethod);
        tv_totalPoint = findViewById(R.id.tv_totalPoint);
        tv_totalPayment = findViewById(R.id.tv_totalPayment);

        rcvOrderDetail = findViewById(R.id.rcvOrderDetail);

    }

    private void loadData() {
        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra("order");
        if (order != null) {
            getUserInfo(); // get user name and phone number
            getOrderItems(order.getId()); // get order items and total price
            tv_Address.setText(order.getUserAddress());
            String orderTime = order.getOrderTime().toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                orderTime = Convert.convertToDate(orderTime);
            }
            tv_orderTime.setText(orderTime);
            tv_orderCode.setText(order.getCode());
            tv_paymentMethod.setText(order.getPaymentMethod());
            tv_totalPoint.setText("+"+order.getPoint().toString());
            tv_totalPayment.setText(Convert.convertPrice(order.getTotal()));
        }
    }


    public void getUserInfo() {
        UserService userService = ServiceBuilder.buildService(UserService.class);
        Call<User> request = userService.getUser();
        request.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if( user.getFirstName() == null || user.getLastName() == null) {
                        tv_userName.setText("user#" + user.getId());
                    }else{
                        tv_userName.setText(user.getFirstName() + " " + user.getLastName());
                    }

                    tv_Phone.setText(user.getPhone());
                } else if (response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getOrderItems(Integer orderId) {
        OrderService orderService = ServiceBuilder.buildService(OrderService.class);
        Call<List<OrderDetail>> request = orderService.getOrderItem(orderId);
        request.enqueue(new Callback<List<OrderDetail>>() {

            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                if (response.isSuccessful()) {
                    List<OrderDetail> orderItems = response.body();
                    orderDetailAdapter = new orderDetailAdapter(orderItems);
                    rcvOrderDetail.setAdapter(orderDetailAdapter );
                    rcvOrderDetail.setLayoutManager(new LinearLayoutManager(mContext));

                    // calculate total price
                    Integer totalPrice = 0;
                    for (OrderDetail orderItem : orderItems) {
                        totalPrice+= orderItem.getQuantity() * orderItem.getProduct().getPrice();
                    }
                    tv_totalPrice.setText(Convert.convertPrice(totalPrice)); // set total price

                } else {
                    Toast.makeText(mContext, "Something was wrong", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }
}
