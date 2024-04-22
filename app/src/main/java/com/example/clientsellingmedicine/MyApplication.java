package com.example.clientsellingmedicine;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initCongif();
    }

    public static Context getContext() {
        return context;
    }

    private void initCongif() {

        Map config = new HashMap();
        config.put("cloud_name", "dwrd1yxgh");
        config.put("api_key", "716447925773513");
        config.put("api_secret", "JD584oxI3Qb9VTy6ZiQJYqSO6YY");
        //config.put("secure", true);
        MediaManager.init(this, config);
    }
}
