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

    public static void clearData(Context context, String prefsName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void removeData(Context context, String prefsName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean containsData(Context context, String prefsName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public static void saveToken(Context context, String prefsName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadToken(Context context, String prefsName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

}
