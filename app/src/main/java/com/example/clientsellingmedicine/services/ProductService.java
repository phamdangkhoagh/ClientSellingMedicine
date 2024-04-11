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
    Call<List<Product>> getProducts(@Path("page") int page);

    @POST("/api/product/filter/{page}")
    Call<List<Product>> getProductsFilter (@Path("page") int page, @Body ProductFilter filter);
}
