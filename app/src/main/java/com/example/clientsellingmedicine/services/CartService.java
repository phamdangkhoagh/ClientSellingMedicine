package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {
    @GET("/api/cart_detail")
    Call<List<CartItem>> getCart();

    @GET("/api/cart_detail/total_item")
    Call<Integer> getTotalItem();

    @POST("/api/cart_detail")
    Call<CartItem> addCartItem(@Body CartItem cartItem);

    @DELETE("/api/cart_detail/{id}")
    Call<CartItem> deleteCartItem(@Path("id") Integer cartItemId);

    @PUT("/api/cart_detail")
    Call<CartItem> updateCartItem(@Body CartItem cartItem);


}
