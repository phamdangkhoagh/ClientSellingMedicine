package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.ResponseDto;
import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.models.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;

public interface UserService {
    @GET("/api/user")
    Call<User> getUser();
    @PUT("/api/user")
    Call<User> updateUser(@Body User user);
    @POST("/api/auth/register_with_otp")
    Call<ResponseDto> registerUser(@Body UserLogin userLogin);
}
