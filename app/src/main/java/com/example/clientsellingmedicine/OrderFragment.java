package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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
import com.example.clientsellingmedicine.services.OrderService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private Context mContext;

    LinearLayout layout_allOrder,layout_processingStatus,layout_inTransitStatus,layout_deliveredStatus,layout_cancelledStatus;

    TextView processingStatus,inTransitStatus,deliveredStatus,cancelledStatus,allOrder;

    RecyclerView rcvOrder;

    orderAdapter orderAdapter;

    HorizontalScrollView horizontal_statusOrder;

    private static List<Order> listOrder ; //list order for search status
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



    private void addControl(View view){
        layout_processingStatus = view.findViewById(R.id.layout_processingStatus);
        layout_inTransitStatus = view.findViewById(R.id.layout_inTransitStatus);
        layout_deliveredStatus = view.findViewById(R.id.layout_deliveredStatus);
        layout_cancelledStatus = view.findViewById(R.id.layout_cancelledStatus);
        layout_allOrder = view.findViewById(R.id.layout_allOrder);

        processingStatus = view.findViewById(R.id.processingStatus);
        inTransitStatus = view.findViewById(R.id.inTransitStatus);
        deliveredStatus = view.findViewById(R.id.deliveredStatus);
        cancelledStatus = view.findViewById(R.id.cancelledStatus);
        allOrder = view.findViewById(R.id.allOrder);

        horizontal_statusOrder = view.findViewById(R.id.horizontal_statusOrder);

        rcvOrder = view.findViewById(R.id.rcvOrder);


    }
    private void addEvents() {
        // Set the horizontal scroll position to the first position
        horizontal_statusOrder.post(new Runnable() {
            @Override
            public void run() {
                horizontal_statusOrder.scrollTo(0, 0);
            }
        });

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

                    layout_deliveredStatus.setBackgroundResource(R.drawable.order_selection_background);
                    //get orders delivered
                    List<Order> ordersDelivered = listOrder.stream()
                            .filter(order -> order.getStatus() == 1)
                            .collect(Collectors.toList());

                    orderAdapter = new orderAdapter(ordersDelivered);
                    rcvOrder.setAdapter(orderAdapter);


                } else if (view == cancelledStatus) {
                    layout_cancelledStatus.setBackgroundResource(R.drawable.order_selection_background);
                    //get orders cancelled
                    List<Order> ordersCancelled = listOrder.stream()
                            .filter(order -> order.getStatus() == 0)
                            .collect(Collectors.toList());

                    orderAdapter = new orderAdapter(ordersCancelled);
                    rcvOrder.setAdapter(orderAdapter);

                } else if (view == allOrder){
                    layout_allOrder.setBackgroundResource(R.drawable.order_selection_background);

                    orderAdapter = new orderAdapter(listOrder);
                    rcvOrder.setAdapter(orderAdapter);
                }
            }
        };

        //set color background for title order filter
        processingStatus.setOnClickListener(statusClickListener);
        inTransitStatus.setOnClickListener(statusClickListener);
        deliveredStatus.setOnClickListener(statusClickListener);
        cancelledStatus.setOnClickListener(statusClickListener);
        allOrder.setOnClickListener(statusClickListener);

        getOrder();
    }

    public void getOrder(){
        OrderService orderService = ServiceBuilder.buildService(OrderService.class);
        Call<List<Order>> request = orderService.getOrders();

        request.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                    listOrder = new ArrayList<>();
                    listOrder = response.body(); //get list order for search status

                    // add list order to recycle view
                    orderAdapter = new orderAdapter(listOrder);
                    rcvOrder.setAdapter(orderAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    rcvOrder.setLayoutManager(layoutManager);

                } else if(response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}
