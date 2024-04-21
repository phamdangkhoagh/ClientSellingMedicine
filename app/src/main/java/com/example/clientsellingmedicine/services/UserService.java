package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserService {
    @GET("/api/user")
    Call<User> getUser();

    @PUT("/api/user")
    Call<User> updateUser(@Body User user);
}
