package com.example.clientsellingmedicine.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;


public class SharedPref {

    public static void saveData(Context context, List<?> objectsList, String prefsName, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(objectsList);
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static <T> List<T> loadData(Context context, String prefsName, String key, Type type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, null);
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
