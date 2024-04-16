package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.CouponDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CouponService {
    @GET("/api/coupon_detail")
    Call<List<CouponDetail>> getCoupon();
}
