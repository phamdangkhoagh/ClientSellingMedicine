package com.example.clientsellingmedicine.services;


import android.os.Build;

import com.example.clientsellingmedicine.MyApplication;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    //emulater
//    private static final String URL = "http://10.0.2.2:9000/";
    //device
    private static final String URL = "http://192.168.43.175:8080/"; //ip Wireless LAN adapter Wi-Fi:
    // Create logger
    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();

                            Token token = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
                            if(token == null){
                                token = new Token("","");
                            }

                            request = request.newBuilder()
                                    .addHeader("Authorization", "Bearer " + token.getAccessToken())
                                    .addHeader("x-device-type", Build.DEVICE)
                                    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                                    .build();

                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(logger);


    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return retrofit.create(serviceType);
    }
}