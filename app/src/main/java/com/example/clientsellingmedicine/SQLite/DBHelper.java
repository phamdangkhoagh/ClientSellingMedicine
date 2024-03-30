package com.example.clientsellingmedicine.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_Name = "DBMedicine";
    public static final int DB_VERSION = 9;

    public DBHelper(Context context){
        super(context,DB_Name,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE CartSelected(id integer ,name text ,price double,unit text,quantity int, image text,status int)";
        db. execSQL (sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql ="DROP TABLE IF EXISTS CartSelected";
        db.execSQL(sql);
        onCreate(db);
    }
}
