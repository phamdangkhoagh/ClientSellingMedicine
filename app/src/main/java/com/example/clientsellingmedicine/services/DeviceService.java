package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Device;
import com.example.clientsellingmedicine.models.Notification;
import com.example.clientsellingmedicine.models.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeviceService {
    @POST("/api/device")
    Call<Device> saveDevice(@Body Device device);
}
