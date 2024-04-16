package com.example.clientsellingmedicine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailProductActivity extends AppCompatActivity {
    private Context mContext;

    private ImageView imv_Back,imv_ProductImage;
    private TextView tv_ProductName,tv_ProductPrice,tv_ProductCode;

    private Button btn_AddToCart,btn_BuyNow;

    private WebView wv_ProductDescription;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.detail_product_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        imv_Back = findViewById(R.id.imv_Back);
        imv_ProductImage = findViewById(R.id.imv_ProductImage);
        tv_ProductName = findViewById(R.id.tv_ProductName);
        tv_ProductPrice = findViewById(R.id.tv_ProductPrice);
        tv_ProductCode = findViewById(R.id.tv_ProductCode);
        btn_AddToCart = findViewById(R.id.btn_AddToCart);
        btn_BuyNow = findViewById(R.id.btn_BuyNow);
        wv_ProductDescription = findViewById(R.id.wv_ProductDescription);
    }

    private void addEvents() {
        //back to previous screen
        imv_Back.setOnClickListener(v -> {
            finish();
        });

        // on lick buy now
        btn_BuyNow.setOnClickListener(v -> {
            Toast.makeText(mContext, "Buy Now", Toast.LENGTH_SHORT).show();
        });

        // on click add to cart
        btn_AddToCart.setOnClickListener(v -> {
            showAddToCartDialog(product);
        });

        //get detail product from previous screen
        getDetailProduct();
    }

    private void getDetailProduct(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            product = (Product) bundle.getSerializable("product");

            tv_ProductName.setText(product.getName());
            String price = Convert.convertPrice(product.getPrice());
            tv_ProductPrice.setText(price+"/"+product.getUnit().getName());
            wv_ProductDescription.loadDataWithBaseURL(null, product.getDescription(), "text/html", "UTF-8", null);
//            tv_ProductCode.setText(productCode);
            Glide.with(mContext)
                    .load(product.getImage())
                    .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                    .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                    .into(imv_ProductImage);
        }
    }

    private CompletableFuture<Integer> addToCart(CartItem cartItem) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<CartItem> request = cartService.addCartItem(cartItem);

        request.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    int result = response.code();
                    future.complete(result);
                }
                else if(response.code() == 401){
                    int result = response.code();
                    future.complete(result);
                }else {
                    future.completeExceptionally(new Exception("Failed to add item to cart"));
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                if (t instanceof IOException) {
                    future.completeExceptionally(new Exception("A connection error occurred"));
                } else {
                    future.completeExceptionally(new Exception("Failed to add item to cart"));
                }
            }
        });

        return future;
    }
    private void showAddToCartDialog(Product product) {

        final AtomicInteger quantity = new AtomicInteger(1);
        final Dialog dialog = new Dialog(this);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_add_to_cart);

        // add control
        TextView tv_product_name = dialog.findViewById(R.id.tv_product_name);
        TextView tv_product_price = dialog.findViewById(R.id.tv_product_price);
        TextView tv_quantity = dialog.findViewById(R.id.tv_quantity);
        ImageView iv_product = dialog.findViewById(R.id.iv_product);
        LinearLayout ll_minus = dialog.findViewById(R.id.ll_minus);
        LinearLayout ll_plus = dialog.findViewById(R.id.ll_plus);
        ImageView iv_back = dialog.findViewById(R.id.iv_back);
        Button btn_AddToCart = dialog.findViewById(R.id.btn_AddToCart);
        Button btn_BuyNow = dialog.findViewById(R.id.btn_BuyNow);

        // add event
        iv_back.setOnClickListener(v -> dialog.dismiss());
        tv_product_name.setText(product.getName());
        tv_product_price.setText(Convert.convertPrice(product.getPrice()));
        Glide.with(dialog.getContext())
                .load(product.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(iv_product);

        // set quantity minus
        ll_minus.setOnClickListener(v -> {
            if (quantity.get() > 1) {
                quantity.decrementAndGet();
                tv_quantity.setText(String.valueOf(quantity.get()));
            }
        });

        // set quantity plus
        ll_plus.setOnClickListener(v -> {
            quantity.incrementAndGet();
            tv_quantity.setText(String.valueOf(quantity.get()));
        });

        btn_AddToCart.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(product, quantity.get(), 1);
            addToCart(cartItem)
                    .thenAccept(result -> {
                        if (result == 200) {
                            // reset total cart
                            //getTotalCartItem();

                            //get CartItems Checked from SharedPreferences
                            Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
                            List<CartItem> listCartItemsChecked = SharedPref.loadData(this, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
                            if(listCartItemsChecked == null){
                                listCartItemsChecked = new ArrayList<>();
                            }
                            listCartItemsChecked.add(cartItem);
                            // update CartItems Checked to SharedPreferences
                            SharedPref.saveData(this, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
                            Toast.makeText(mContext, "Add item to cart successfully", Toast.LENGTH_LONG).show();
                        }
                        else if(result == 401){
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Toast.makeText(mContext, "Failed to add item to cart", Toast.LENGTH_LONG).show();
                        }
                    })
                    .exceptionally(ex -> {
                        Log.e("Error", "Failed to add item to cart: " + ex.getMessage());
                        return null;
                    });
            dialog.dismiss();
        });


        // show dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
