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
                            // access token is expired
                            if(response.code() == 401) {
                                synchronized (okHttp) {
                                    //perform all 401 in sync blocks, to avoid multiply token updates
                                    Token currentToken = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);

                                    Log.d("Token", "Equals:  " +currentToken.equals(token) );
                                    if(currentToken != null && currentToken.getAccessToken().equals(token.getAccessToken())) {
                                        Log.d("TAG", "intercept: " + "401 error found");
                                        int code = refreshToken(token) / 100;
                                        if (code != 2) { //if refresh token failed for some reason
                                            if (code == 4) //only if response is 400, 500 might mean that token was not updated
                                                Logout(MyApplication.getContext()); //go to login screen
                                            return response; //if token refresh failed - show error to user
                                        }
                                    }
                                    if(currentToken != null) {
                                        Log.d("TAG", "intercept: " + "401 error found 2");
                                        Token newToken = SharedPref.loadToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
                                        setAuthHeader(builder, newToken);
                                        Request newrequest = builder.build();
                                        return chain.proceed(newrequest); //repeat request with new token
                                    }
                                }
                            }

                        return response;
                        }
                    })
                    .addInterceptor(logger);


    private static Gson gson = new GsonBuilder()
            .setDateFormat("MMM d, yyyy, hh:mm:ss a")
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
        // This method now returns an int
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<Token> requestRefreshToken = loginService.refreshToken(token);

        try {
            // Make a synchronous request by calling execute()
            retrofit2.Response<Token> response = requestRefreshToken.execute();

            if (response.isSuccessful()) {
                Token newToken = response.body();
                SharedPref.saveToken(MyApplication.getContext(), Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN, newToken);
                Log.d("tag", "onResponse: "+response.body());
                return response.code();
            } else {
                return response.code();
            }
        } catch (IOException e) {
            return 500; // Return a default error code
        }
    }

    public static void Logout(Context context) {
        LogoutService logoutService = ServiceBuilder.buildService(LogoutService.class);
        Call<ResponseDto> request = logoutService.logout();
        request.enqueue(new Callback<ResponseDto>() {

            @Override
            public void onResponse(Call<ResponseDto> call, retrofit2.Response<ResponseDto> response) {
                if (response.isSuccessful()) {
                    // remove token
                    SharedPref.removeData(context, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
                    // return to login screen and finish all activity
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    if(context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDto> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "A connection error occured", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onFailure: "+ t.getMessage());
                } else
                    Toast.makeText(context, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }
}