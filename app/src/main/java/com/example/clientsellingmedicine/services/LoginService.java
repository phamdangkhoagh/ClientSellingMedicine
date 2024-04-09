package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.models.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/api/auth/login")
    Call<Token> login(@Body UserLogin userLogin);

    @GET("/api/user")
    Call<User> login();

    @POST("/api/token/check")
    Call<Boolean> checkToken(@Body Token token);

    @POST("/api/auth/refresh")
    Call<Token> refreshToken(@Body Token token);

}
