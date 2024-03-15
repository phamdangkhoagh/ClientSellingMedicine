package com.example.clientsellingmedicine.services;


import com.example.clientsellingmedicine.models.Product;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IdeaService {
    @GET("products")
    Call<List<Product>> getIdeas();


    @GET("ideas/{id}")
    Call<Product> getIdea(@Path("id")int id);

    @POST("ideas")
    Call<Product> createIdea(@Body Product newIdea);

    @FormUrlEncoded
    @PUT("ideas/{id}")
    Call<Product> updateIdea(
            @Path("id")int id,
            @Field("name")String name,
            @Field("description")String desc,
            @Field("status")String status,
            @Field("owner")String owner
    );

    @DELETE("ideas/{id}")
    Call<Void> deleteIdea(@Path("id")int id);
}
