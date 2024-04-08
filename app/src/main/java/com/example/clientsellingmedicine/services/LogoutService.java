package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.ResponseDto;
import com.example.clientsellingmedicine.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LogoutService {
    @GET("/api/auth/logout")
    Call<ResponseDto> logout();
}
