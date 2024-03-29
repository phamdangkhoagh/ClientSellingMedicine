package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.interfaces.IOnItemClickListenerRecyclerView;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.IdeaService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements IOnItemClickListenerRecyclerView  {
    private Context mContext;
    RecyclerView rcvProduct;
    productAdapter productAdapter;

    IOnItemClickListenerRecyclerView listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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
                    productAdapter = new productAdapter(response.body(), ProductActivity.this);
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

    //onClick item product in recyclerview
    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}