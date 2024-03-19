package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {
    @GET("orders")
    Call<List<Order>> getOrders();


    @GET("order/{id}")
    Call<Order> getOrder(@Path("id")int id);

}
