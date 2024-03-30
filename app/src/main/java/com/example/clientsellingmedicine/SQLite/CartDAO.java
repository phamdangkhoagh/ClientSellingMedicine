package com.example.clientsellingmedicine.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private SQLiteDatabase db;
    private List<CartItem> listCartItems;

    DBHelper dbtHelper;
    public CartDAO(Context context) {
        dbtHelper  = new DBHelper(context);
        db = dbtHelper.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<CartItem> getProductsSelected(String sql, String... selectionArgs) {

        listCartItems = new ArrayList<>();
        if(db !=null){
            Cursor cursor = db.rawQuery(sql, selectionArgs);

            Log.d("TAG", "cursor: " + cursor);
            while (cursor.moveToNext()) {
                CartItem cartItem = new CartItem();
                cartItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cartItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                cartItem.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                cartItem.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
                cartItem.setImage(cursor.getString(cursor.getColumnIndex("image")));
                cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
                cartItem.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                listCartItems.add(cartItem);
            }
            cursor.close();
        }else{
            Log.d("TAG", "db: " + db);
        }

        return listCartItems;
    }

    public List<CartItem> getProductsAll() {
        String sql = "SELECT * FROM CartSelected";
        return getProductsSelected(sql);
    }

    public long insertItem(CartItem cartItem) {
        ContentValues values = new ContentValues();
        values.put("name", cartItem.getName());
        values.put("price", cartItem.getPrice());
        values.put("unit", cartItem.getUnit());
        values.put("image", cartItem.getImage());
        values.put("quantity", cartItem.getQuantity());
        values.put("status", cartItem.getStatus());
        return db.insert("CartSelected", null, values);
    }

    public void deleteItem(String id) {
        db.delete("CartSelected", "id=?", new String[]{id});
        db.close();
    }

}
