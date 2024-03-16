package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientsellingmedicine.Adapter.cartAdapter;
import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.models.Cart;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.IdeaService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {
    private Context mContext;

    RecyclerView rcvCart;
    cartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = CartActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        rcvCart = findViewById(R.id.rcvCart);
    }

    private void addEvents() {

//        getCartItem();
    }

//    public void getCartItem(){
//        IdeaService ideaService = ServiceBuilder.buildService(IdeaService.class);
//        Call<List<Cart>> request = ideaService.getIdeas();
//
//        request.enqueue(new Callback<List<Cart>>() {
//
//            @Override
//            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
//                if(response.isSuccessful()){
//                    cartAdapter = new cartAdapter(response.body());
//                    rcvCart.setAdapter(cartAdapter);
//                    rcvCart.setLayoutManager(new GridLayoutManager(mContext, 2));
//                } else if(response.code() == 401) {
//                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Cart>> call, Throwable t) {
//                if (t instanceof IOException){
//                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
//                } else
//                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
