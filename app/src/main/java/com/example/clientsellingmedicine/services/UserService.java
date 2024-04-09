package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("/api/user")
    Call<User> getUser();
}
