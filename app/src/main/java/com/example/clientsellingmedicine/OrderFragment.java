package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.orderAdapter;
import com.example.clientsellingmedicine.models.Order;

import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.services.OrderService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.example.clientsellingmedicine.utils.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private Context mContext;

    LinearLayout layout_allOrder, layout_processingStatus, layout_inTransitStatus, layout_deliveredStatus, layout_cancelledStatus, layout_empty_order, layout_statusOrder;

    TextView processingStatus, inTransitStatus, deliveredStatus, cancelledStatus, allOrder, tv_empty_order;

    Button btn_shopping_now;

    ImageView img_filter_order;

    ScrollView scrollview_content;

    RecyclerView rcvOrder;

    orderAdapter orderAdapter;

    HorizontalScrollView horizontal_statusOrder;

    private static List<Order> listOrder; //list order for search status

    public OrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_screen, container, false);
        mContext = view.getContext();
        // Thực hiện các thao tác cần thiết trên giao diện view của fragment
        addControl(view);
        addEvents();
        return view;


    }


    private void addControl(View view) {
        layout_statusOrder = view.findViewById(R.id.layout_statusOrder);
        layout_processingStatus = view.findViewById(R.id.layout_processingStatus);
        layout_inTransitStatus = view.findViewById(R.id.layout_inTransitStatus);
        layout_deliveredStatus = view.findViewById(R.id.layout_deliveredStatus);
        layout_cancelledStatus = view.findViewById(R.id.layout_cancelledStatus);
        layout_allOrder = view.findViewById(R.id.layout_allOrder);
        layout_empty_order = view.findViewById(R.id.layout_empty_order);

        processingStatus = view.findViewById(R.id.processingStatus);
        inTransitStatus = view.findViewById(R.id.inTransitStatus);
        deliveredStatus = view.findViewById(R.id.deliveredStatus);
        cancelledStatus = view.findViewById(R.id.cancelledStatus);
        allOrder = view.findViewById(R.id.allOrder);
        tv_empty_order = view.findViewById(R.id.tv_empty_order);

        horizontal_statusOrder = view.findViewById(R.id.horizontal_statusOrder);
        scrollview_content = view.findViewById(R.id.scrollview_content);

        rcvOrder = view.findViewById(R.id.rcvOrder);

        btn_shopping_now = view.findViewById(R.id.btn_shopping_now);

        img_filter_order = view.findViewById(R.id.img_filter_order);


    }

    private void addEvents() {
        // Set the horizontal scroll position to the first position
        horizontal_statusOrder.post(new Runnable() {
            @Override
            public void run() {
                horizontal_statusOrder.scrollTo(0, 0);
            }
        });


//        // click on the shopping now button or login now button
//        View.OnClickListener shoppingNowClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view == btn_shopping_now) {
//                    //go to home fragment
//                    ((MainActivity) getActivity()).goToHomeFragment();
//                }
//            }
//        };
//        btn_shopping_now.setOnClickListener(shoppingNowClickListener);


        //get order status
        View.OnClickListener statusClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_allOrder.setBackgroundResource(R.color.white);
                layout_processingStatus.setBackgroundResource(R.color.white);
                layout_inTransitStatus.setBackgroundResource(R.color.white);
                layout_deliveredStatus.setBackgroundResource(R.color.white);
                layout_cancelledStatus.setBackgroundResource(R.color.white);

                if (view == processingStatus) {
                    layout_processingStatus.setBackgroundResource(R.drawable.order_selection_background);

                } else if (view == inTransitStatus) {
                    layout_inTransitStatus.setBackgroundResource(R.drawable.order_selection_background);

                } else if (view == deliveredStatus) {
                    //set background color delivered status
                    layout_deliveredStatus.setBackgroundResource(R.drawable.order_selection_background);
                    if (listOrder != null) {
                        //get orders delivered
                        List<Order> ordersDelivered = listOrder.stream()
                                .filter(order -> order.getStatus() == 1)
                                .collect(Collectors.toList());

                        orderAdapter = new orderAdapter(ordersDelivered);
                        rcvOrder.setAdapter(orderAdapter);

                    }

                } else if (view == cancelledStatus) {
                    //set background color cancelled status
                    layout_cancelledStatus.setBackgroundResource(R.drawable.order_selection_background);
                    if (listOrder != null) {
                        //get orders cancelled
                        List<Order> ordersCancelled = listOrder.stream()
                                .filter(order -> order.getStatus() == 0)
                                .collect(Collectors.toList());

                        orderAdapter = new orderAdapter(ordersCancelled);
                        rcvOrder.setAdapter(orderAdapter);
                    }
                } else if (view == allOrder) {
                    layout_allOrder.setBackgroundResource(R.drawable.order_selection_background);
                    if (listOrder != null) {
                        orderAdapter = new orderAdapter(listOrder);
                        rcvOrder.setAdapter(orderAdapter);
                    }

                }
            }
        };

        //set color background for title order filter
        processingStatus.setOnClickListener(statusClickListener);
        inTransitStatus.setOnClickListener(statusClickListener);
        deliveredStatus.setOnClickListener(statusClickListener);
        cancelledStatus.setOnClickListener(statusClickListener);
        allOrder.setOnClickListener(statusClickListener);

        // validate token
        Token token = SharedPref.loadToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
        if (token != null && Validator.isTokenValid(token)) {
            // get all orders
            getOrders();
        } else {
            showOrderHistoryWithoutLogin();
        }

    }

    public void getOrders() {
        OrderService orderService = ServiceBuilder.buildService(OrderService.class);
        Call<List<Order>> request = orderService.getOrders();

        request.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    listOrder = new ArrayList<>();
                    listOrder = response.body(); //get list order
                    if(listOrder != null && listOrder.size() > 0) {
                        // add list order to recycle view
                        orderAdapter = new orderAdapter(listOrder);
                        rcvOrder.setAdapter(orderAdapter);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        rcvOrder.setLayoutManager(layoutManager);
                    }
                    else {
                        showOrderHistoryWithEmptyOrder();
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public void showOrderHistoryWithoutLogin() {
        tv_empty_order.setText("Bạn cần đăng nhập để xem lịch sử đơn hàng!");
        btn_shopping_now.setText("Đăng nhập ngay");
        layout_empty_order.setVisibility(View.VISIBLE);
        scrollview_content.setVisibility(View.GONE);
        horizontal_statusOrder.setVisibility(View.GONE);
        img_filter_order.setVisibility(View.GONE);
        btn_shopping_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showOrderHistoryWithEmptyOrder() {
        tv_empty_order.setText("Bạn chưa có đơn hàng nào!");
        btn_shopping_now.setText("Mua sắm ngay");
        layout_empty_order.setVisibility(View.VISIBLE);
        scrollview_content.setVisibility(View.GONE);
        horizontal_statusOrder.setVisibility(View.GONE);
        img_filter_order.setVisibility(View.GONE);

        btn_shopping_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToHomeFragment();
            }
        });
    }
}
