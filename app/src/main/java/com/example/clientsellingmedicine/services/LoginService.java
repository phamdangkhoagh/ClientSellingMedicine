package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.models.token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/api/auth/login")
    Call<token> login(@Body UserLogin userLogin);

}
