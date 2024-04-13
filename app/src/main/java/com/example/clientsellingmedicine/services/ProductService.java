package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.models.ProductFilter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductService {
    @GET("/api/product")
    Call<List<Product>> getProducts();

    @POST("/api/product/filter")
    Call<List<Product>> getProductsFilter ( @Body ProductFilter filter);

    @GET("/api/product/new_product")
    Call<List<Product>> getNewProducts();

    @GET("/api/product/best_seller")
    Call<List<Product>> getBestSellerProducts();

    @GET("/api/product/have_sold")
    Call<List<Product>> getHaveSoldProducts();

    @GET("/api/product/best_promotion")
    Call<List<Product>> getBestPromotionProducts();
}
