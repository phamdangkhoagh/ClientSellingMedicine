package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Coupon;
import com.example.clientsellingmedicine.models.CouponDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CouponService {

    @GET("/api/coupon_detail")
    Call<List<CouponDetail>> getCoupon();

    @GET("/api/coupon_detail/all")
    Call<List<CouponDetail>> getAllCoupon();

    @GET("/api/coupon")
    Call<List<Coupon>> getCoupons();

    @POST("/api/coupon_detail")
    Call<CouponDetail> exchangeCoupon(@Body Coupon Coupon);
}
