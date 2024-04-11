package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.interfaces.IOnItemClickListenerRecyclerView;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.models.ProductFilter;
import com.example.clientsellingmedicine.services.ProductService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements IOnItemClickListenerRecyclerView {
    private Context mContext;
    RecyclerView rcvProduct;
    productAdapter productAdapter;

    TextInputEditText edtSearch;
    LinearLayout loadingLayout;
    IOnItemClickListenerRecyclerView listener;
    private int currentPage = 0;
    private boolean isEndOfPage = false;
    private boolean isLoading = false;

    private final ProductFilter productFilter = new ProductFilter();

    private List<Product> products = new ArrayList<>();

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
        loadingLayout = findViewById(R.id.loadingLayout);
        edtSearch = findViewById(R.id.edtSearch);
    }

    private void addEvents() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL){

                    performSearch(edtSearch.getText().toString());

                    // Clear text and hide keyboard
                    edtSearch.setText("");
                    hideKeyboard(mContext,v);
                    return true;
                }
                return false;
            }
        });


        initRecyclerview();
        initScrollListener();
    }

    public void initRecyclerview() {
        // display all products of category from home screen
        productFilter.setIdCategory(5);
        products = getProductsFiltered(currentPage,productFilter);
        productAdapter = new productAdapter(products, this);
        rcvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        rcvProduct.setAdapter(productAdapter);
    }

    public List<Product> getProducts(Integer page) {
        ProductService addressService = ServiceBuilder.buildService(ProductService.class);
        Call<List<Product>> call = addressService.getProducts(page);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Product>> future = executorService.submit((Callable<List<Product>>) () -> {
            try {
                Response<List<Product>> response = call.execute();
                if (response.isSuccessful()) {
                    return response.body();

                } else {
                    Log.d("Error", "Status Code : " + response.code() + ", Message : " + response.message());
                    return null;
                }
            } catch (IOException e) {
                Log.d("Error", "Get Product Exception : " + e.getMessage());
                return null;
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        } finally {
            executorService.shutdown();
        }

    }


    public List<Product> getProductsFiltered(Integer page,ProductFilter filter) {
        ProductService addressService = ServiceBuilder.buildService(ProductService.class);
        Call<List<Product>> call = addressService.getProductsFilter(page,filter);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Product>> future = executorService.submit((Callable<List<Product>>) () -> {
            try {
                Response<List<Product>> response = call.execute();
                if (response.isSuccessful()) {
                    return response.body();

                } else {
                    Log.d("Error", "Status Code : " + response.code() + ", Message : " + response.message());
                    return null;
                }
            } catch (IOException e) {
                Log.d("Error", "Get Product Exception : " + e.getMessage());
                return null;
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        } finally {
            executorService.shutdown();
        }

    }

    //onClick item product in recyclerview
    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, DetailProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }


    private void initScrollListener() {
        rcvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                final int visibleThreshold = 2;
                if (dy > 0) { // only load more if scrolling up
                    int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = productAdapter.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    Log.d("TAG", "loadMore: " + "End of page");
                    if (lastItem + 1 == currentTotalCount && isLoading == false) {
                        // Đã cuộn tới cuối danh sách
                        loadMore();
                    }
                }
            }
        });
    }


    private void loadMore() {
        isLoading = true;
        currentPage++;
        List<Product> moreProducts = getProductsFiltered(currentPage,productFilter);
        if (moreProducts == null || moreProducts.size() == 0) {

            isEndOfPage = true;
            return;
        }
        loadingLayout.setVisibility(View.VISIBLE);

        products.addAll(moreProducts);
        Handler handler = new Handler();
        handler.postDelayed(() -> {

            productAdapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            isLoading = false;
        }, 1000);

    }

    private void performSearch(String searchText) {
        currentPage = 0;
        isEndOfPage = false;
        isLoading = true;
        ProductFilter filter = new ProductFilter();
        filter.setKeySearch(searchText);
        List<Product> filteredProducts = getProductsFiltered(currentPage,filter);
        if (filteredProducts == null || filteredProducts.size() == 0) {
            Toast.makeText(mContext, "No result found", Toast.LENGTH_SHORT).show();
        }else {
            products.clear();
            products.addAll(filteredProducts);
            //productAdapter = new productAdapter(products, this);
            productAdapter.notifyDataSetChanged();
            isLoading = false;
        }

    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}