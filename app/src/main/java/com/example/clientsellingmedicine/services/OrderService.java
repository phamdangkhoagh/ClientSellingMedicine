package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.MomoResponse;
import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.models.OrderDetail;
import com.example.clientsellingmedicine.models.PaymentDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @GET("/api/orders")
    Call<List<Order>> getOrders();


    @GET("order/{id}")
    Call<Order> getOrder(@Path("id")int id);

    @GET("/api/order_detail/{id}")
    Call<List<OrderDetail>> getOrderItem(@Path("id")int id);

    @POST("/api/orders")
    Call<MomoResponse> newOrder(@Body PaymentDto order);

}
