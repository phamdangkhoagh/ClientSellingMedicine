package com.example.clientsellingmedicine;

;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;




import android.content.Context;
import android.content.Intent;


import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.Adapter.productDiscountAdapter;
import com.example.clientsellingmedicine.interfaces.IOnItemClickListenerRecyclerView;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.IdeaService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements IOnItemClickListenerRecyclerView {
    private Context mContext;

    IOnItemClickListenerRecyclerView listener;
    productAdapter productAdapter;

    productDiscountAdapter productDiscountAdapter;
    TextView tvTopSale,tvTopDiscount,tvNumberCart;
    RecyclerView rcvTopProductSelling, rcvTopProductsDiscount;
    ImageView ivCart,ivNotification;

    TextInputEditText searchText;

    FrameLayout redCircle;

    ImageSlider imageSlider;
    private SearchView searchView;
    private String lastQuery;

    public HomeFragment() {
        // Required empty public constructor

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        mContext = view.getContext();
        // toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false); // Tắt title mặc định của Toolbar
        }
        setHasOptionsMenu(true);
        addControl(view);
        addEvents();

        return view;


    }


    private void addControl(View view) {
        tvTopSale = view.findViewById(R.id.tvTopSale);
        rcvTopProductSelling = view.findViewById(R.id.rcvTopProductSelling);
        tvTopDiscount = view.findViewById(R.id.tvTopDiscount);
        rcvTopProductsDiscount =view.findViewById(R.id.rcvTopProductDiscount);
        searchText = view.findViewById(R.id.search_text);
        ivCart = view.findViewById(R.id.ivCart);
        ivNotification = view.findViewById(R.id.ivNotification);
        tvNumberCart = view.findViewById(R.id.tvNumberCart);
        redCircle = view.findViewById(R.id.redCircle);
        imageSlider = view.findViewById(R.id.image_slider);
    }
    private void addEvents(){
        tvTopSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                startActivity(intent);
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL){

                    performSearch(searchText.getText().toString());

                    // Clear text and hide keyboard
                    searchText.setText("");
                    hideKeyboard(mContext,v);
                    return true;
                }
                return false;
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on Notification", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
//                Toast.makeText(mContext, "Click on Cart", Toast.LENGTH_LONG).show();
            }
        });

//        getTopProductsSelling();
//        getTopProductsDiscount();
        showSlider();
    }

    private void getTopProductsSelling(){
        IdeaService ideaService = ServiceBuilder.buildService(IdeaService.class);
        Call<List<Product>> request = ideaService.getIdeas();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productAdapter = new productAdapter(response.body(), HomeFragment.this);

                    rcvTopProductSelling.setAdapter(productAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    rcvTopProductSelling.setLayoutManager(layoutManager);

                    Log.d("Number of cart", "onResponse: "+productAdapter.getItemCount());
                    displayCartItemCount(productAdapter.getItemCount());

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


    private void getTopProductsDiscount(){
        IdeaService ideaService = ServiceBuilder.buildService(IdeaService.class);
        Call<List<Product>> request = ideaService.getIdeas();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productDiscountAdapter = new productDiscountAdapter(response.body());
                    rcvTopProductsDiscount.setAdapter(productDiscountAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    rcvTopProductsDiscount.setLayoutManager(layoutManager);
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

    private void performSearch(String searchText) {
        Toast.makeText(mContext, searchText, Toast.LENGTH_LONG).show();

    }

    private void displayCartItemCount(int num) {
        if(num <= 0){
            redCircle.setVisibility(GONE);
        }
        else if(num > 99){
            tvNumberCart.setText("99");
            redCircle.setVisibility(VISIBLE);
        }
        else {
            tvNumberCart.setText(String.valueOf(num));
            redCircle.setVisibility(VISIBLE);
        }
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showSlider(){
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://data-service.pharmacity.io/pmc-ecm-webapp-config-api/production/banner/795%20x%20302%20(x2)%20(1)-1710388934651.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240308081457-0-795%20x%20302%20%28x2%29.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240305075612-0-PMCE_795x302(x2).png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240311085012-0-795x302%28x2%29%20%281%29.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://data-service.pharmacity.io/pmc-ecm-webapp-config-api/production/banner/795%20x%20302%20(x2)%20(1)-1710388934651.png", ScaleTypes.FIT));
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
         imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240308084056-0-dealhot_dealhot_1590x604.jpg", ScaleTypes.FIT));

        imageSlider.setImageList(imageList,ScaleTypes.FIT);

    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
