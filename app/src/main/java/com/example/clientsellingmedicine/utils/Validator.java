package com.example.clientsellingmedicine.utils;

import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;

public class Validator {
    public static boolean isTokenValid(Token token) {
        return checkTokenValidity(token);
    }



    public static boolean checkTokenValidity(Token token) {
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<Boolean> call = loginService.checkToken(token);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Response<Boolean> response = call.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    return false;
                }
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        } finally {
            executorService.shutdown();
        }
    }
}
