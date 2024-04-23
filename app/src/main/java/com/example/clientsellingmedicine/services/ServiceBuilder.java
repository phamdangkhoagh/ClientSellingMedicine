package com.example.clientsellingmedicine.services;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.clientsellingmedicine.LoginActivity;
import com.example.clientsellingmedicine.MyApplication;
import com.example.clientsellingmedicine.models.ResponseDto;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    //emulater
//    private static final String URL = "http://10.0.2.2:9000/";
    //device
//    private static final String URL = "http://192.168.1.26:8080/"; //ip Wireless LAN adapter Wi-Fi:
    private static final String URL = "http://192.168.1.15:8080/"; //ip Wireless LAN adapter Wi-Fi:
    // Create logger
    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        private boolean isRefreshing = false; // check fresh token status

                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            //Build new request
                            Request.Builder builder = originalRequest.newBuilder();

                            Token token = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);

                            setAuthHeader(builder, token);

                            // Add headers to the builder before building the request
                            builder.addHeader("x-device-type", Build.DEVICE)
                                    .addHeader("Accept-Language", Locale.getDefault().getLanguage());

                            // Build the request with all the required headers
                            Request request = builder.build();

                            Response response = chain.proceed(request);
                            // Access token is expired
                            if (response.code() == 401) {
                                if (!isRefreshing) { // Kiểm tra trạng thái làm mới token
                                    synchronized (okHttp) {
                                        isRefreshing = true; // Đánh dấu bắt đầu quá trình làm mới token
                                        Token currentToken = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);

                                        if (currentToken != null && currentToken.getAccessToken().equals(token.getAccessToken())) {
                                            int code = refreshToken(token) / 100;
                                            if (code == 2) { // Nếu refresh token thành công
                                                Token newToken = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
                                                setAuthHeader(builder, newToken);
                                                Request newRequest = builder.build();
                                                Response newResponse = chain.proceed(newRequest);
                                                isRefreshing = false; // Đánh dấu kết thúc quá trình làm mới token
                                                return newResponse; // Trả về response mới
                                            }
                                        }
                                    }
                                }
                            }
                            return response;
                        }
                    })
                    .addInterceptor(logger);


    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return retrofit.create(serviceType);
    }


    private static void setAuthHeader(Request.Builder builder, Token token) {
        if (token !=null && token.getAccessToken() != null && token.getAccessToken() != "") //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token.getAccessToken()));
    }

    private static int refreshToken(Token token) {
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<Token> requestRefreshToken = loginService.refreshToken(token);

        try {
            retrofit2.Response<Token> response = requestRefreshToken.execute();

            if (response.isSuccessful()) {
                Token newToken = response.body();
                SharedPref.saveToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN, newToken);
                Log.d("tag", "onResponse: " + response.body());
                return response.code();
            } else {
                return response.code();
            }
        } catch (IOException e) {
            return 500; // Trả về mã lỗi mặc định
        }
    }


}