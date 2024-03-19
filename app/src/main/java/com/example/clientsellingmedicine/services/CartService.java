package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Cart;
import com.example.clientsellingmedicine.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {
    @GET("carts")
    Call<List<Cart>> getCart();


}
