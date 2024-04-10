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
    @GET("/api/product/{page}")
    Call<List<Product>> getProducts(@Path("page") int id);

    @POST("/api/product/filter")
    Call<List<Product>> getProductsFilter (@Body ProductFilter filter);
}
