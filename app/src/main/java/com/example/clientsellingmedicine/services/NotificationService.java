package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.CouponDetail;
import com.example.clientsellingmedicine.models.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("/api/notification")
    Call<List<Notification>> getNotification();
    @PUT("/api/notification/{notificationId}")
    Call<Notification> updateNotification(@Path("notificationId") Integer notificationId);
    @POST("/api/notification/all")
    Call<Notification> updateAllNotification();
}
