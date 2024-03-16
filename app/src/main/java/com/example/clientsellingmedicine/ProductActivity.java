package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.IdeaService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private Context mContext;
    RecyclerView rcvProduct;
    productAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = ProductActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        rcvProduct = findViewById(R.id.rcvProduct);
    }

    private void addEvents() {
        getProducts();
    }

    public void getProducts(){
        IdeaService ideaService = ServiceBuilder.buildService(IdeaService.class);
        Call<List<Product>> request = ideaService.getIdeas();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productAdapter = new productAdapter(response.body());
                    rcvProduct.setAdapter(productAdapter);
                    rcvProduct.setLayoutManager(new GridLayoutManager(mContext, 2));
                } else if(response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}