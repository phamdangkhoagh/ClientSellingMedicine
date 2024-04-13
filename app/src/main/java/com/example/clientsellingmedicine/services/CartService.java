package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CartService {
    @GET("/api/cart_detail")
    Call<List<CartItem>> getCart();


}
