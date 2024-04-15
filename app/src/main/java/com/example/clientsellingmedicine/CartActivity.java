package com.example.clientsellingmedicine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.cartAdapter;
import com.example.clientsellingmedicine.interfaces.IOnCartItemListener;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity implements IOnCartItemListener {
    private Context mContext;
    cartAdapter cartAdapter = new cartAdapter();
    RecyclerView rcvCart;
    LinearLayout bottom_view, linear_layout_dynamic;

    TextView tv_TotalAmountCart, tvTotalItemCart, tvDelete, tv_TotalPrice, tv_TotalProductDiscount, tv_TotalVoucherDiscount;


    ImageView icon_arrow_up, ivBackCart;

    CheckBox checkboxCartItem, masterCheckboxCart;

    List<CartItem> listProductsToBuy;

    private Boolean isShowBottomView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.cart_screen);
        addControl();
        addEvents();


    }

    private void addControl() {
        rcvCart = findViewById(R.id.rcvCart);
        bottom_view = findViewById(R.id.bottom_view);
        linear_layout_dynamic = findViewById(R.id.linear_layout_dynamic);
        icon_arrow_up = findViewById(R.id.icon_arrow_up);
        tv_TotalAmountCart = findViewById(R.id.tv_TotalAmountCart);

        masterCheckboxCart = findViewById(R.id.masterCheckboxCart);
        tvTotalItemCart = findViewById(R.id.tvTotalItemCart);
        ivBackCart = findViewById(R.id.ivBackCart);
        tvDelete = findViewById(R.id.tvDelete);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
        tv_TotalProductDiscount = findViewById(R.id.tv_TotalProductDiscount);
        tv_TotalVoucherDiscount = findViewById(R.id.tv_TotalVoucherDiscount);
    }

    private void addEvents() {
        cartAdapter.setOnCheckboxChangedListener(this);

        getCartItems();

        ivBackCart.setOnClickListener(v -> finish());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int total = Convert.convertCurrencyFormat(tv_TotalPrice.getText().toString().trim());
                int totalProductDiscount = Convert.convertCurrencyFormat(tv_TotalProductDiscount.getText().toString().trim());
                int totalVoucherDiscount = Convert.convertCurrencyFormat(tv_TotalVoucherDiscount.getText().toString().trim());
                int totalAmount = total - totalProductDiscount - totalVoucherDiscount;
                tv_TotalAmountCart.setText(Convert.convertPrice(totalAmount));
            }
        };
        tv_TotalPrice.addTextChangedListener(textWatcher);
        tv_TotalProductDiscount.addTextChangedListener(textWatcher);
        tv_TotalVoucherDiscount.addTextChangedListener(textWatcher);



        icon_arrow_up.setOnClickListener(v -> {
            if (isShowBottomView) {
                // set new height
                int desiredHeightInDp = 260;

                float scale = getResources().getDisplayMetrics().density;
                int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);

                ViewGroup.LayoutParams layoutParams = bottom_view.getLayoutParams();
                layoutParams.height = desiredHeightInPixels;
                bottom_view.setLayoutParams(layoutParams);

                //display view
                linear_layout_dynamic.setVisibility(View.VISIBLE);
                // set icon down
                icon_arrow_up.setImageResource(R.drawable.ic_arrow_down);

                isShowBottomView = false;
            } else {
                // set new height
                int desiredHeightInDp = 160;

                float scale = getResources().getDisplayMetrics().density;
                int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);

                ViewGroup.LayoutParams layoutParams = bottom_view.getLayoutParams();
                layoutParams.height = desiredHeightInPixels;
                bottom_view.setLayoutParams(layoutParams);

                //display view
                linear_layout_dynamic.setVisibility(View.GONE);
                // set icon up
                icon_arrow_up.setImageResource(R.drawable.ic_arrow_up);

                isShowBottomView = true;
            }

        });


        masterCheckboxCart.setOnClickListener(v -> {
            if (masterCheckboxCart.isChecked())
                cartAdapter.setAllSelected(true);
            else
                cartAdapter.setAllSelected(false);

        });

        tvDelete.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
            builder.setIcon(R.drawable.drug) // Đặt icon của Dialog
                    .setTitle("Xác Nhận Xóa Sản Phẩm")
                    .setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?")
                    .setCancelable(false) // Bấm ra ngoài không mất dialog

                    .setPositiveButton("Xóa", (dialog, which) -> {
                        // Xử lý khi nhấn nút OK
                        // get list cart items checked
                        Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
                        List<CartItem> listCartItemsChecked = SharedPref.loadData(CartActivity.this, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
                        if(listCartItemsChecked != null){
                            // delete cart item
                            for (CartItem item: listCartItemsChecked) {
                                deleteCartItem(item);
                                cartAdapter.removeItems(item);
                                tvTotalItemCart.setText("(" + cartAdapter.getItemCount() + ")");
                            }
                        }
                    })

                    .setNegativeButton("Hủy", (dialog, which) -> {
                        // Xử lý khi nhấn nút Cancel
                    })
                    .show();
        });


    }



    public void getCartItems() {
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<List<CartItem>> request = cartService.getCart();
        request.enqueue(new Callback<List<CartItem>>() {

            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    cartAdapter.setListCartItems(response.body());
                    tvTotalItemCart.setText("(" + cartAdapter.getItemCount() + ")"); // set total item in cart
                    rcvCart.setAdapter(cartAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    rcvCart.setLayoutManager(layoutManager);

                } else if (response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void deleteCartItem(CartItem cartItem) {
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<CartItem> request = cartService.deleteCartItem(cartItem.getProduct().getId());
        request.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(CartActivity.this, "Deleted item: " + cartItem.getProduct().getId(), Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to Deleted item", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(CartActivity.this, "A connection error occurred", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to delete item: " + cartItem.getProduct().getName(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void setValueOfMasterCheckbox(boolean isChecked) {
        if (isChecked) {
            masterCheckboxCart.setChecked(true);
        } else {
            masterCheckboxCart.setChecked(false);
        }
    }

    @Override
    public void setStatusOfDeleteText(boolean isShowed) {
        if (isShowed) {
            tvDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.GONE);
        }

    }


    @Override
    public void getTotalAmount(int total) {
        String totalAmount = Convert.convertPrice(total);
        tv_TotalPrice.setText(totalAmount);
        //tv_TotalAmountCart.setText(totalAmount);
    }

    @Override
    public void getTotalProductDiscount(int total) {
        String totalProductDiscount = Convert.convertPrice(total);
        tv_TotalProductDiscount.setText(totalProductDiscount);
    }

    @Override
    public void updateCartItemQuantity(CartItem cartItem) {
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<CartItem> request = cartService.updateCartItem(cartItem);
        request.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(CartActivity.this, "Updated item: " + cartItem.getProduct().getId(), Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Somethings was wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(CartActivity.this, "A connection error occurred", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Somethings was wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
