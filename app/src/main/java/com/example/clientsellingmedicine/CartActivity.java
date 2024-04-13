package com.example.clientsellingmedicine;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.cartAdapter;
import com.example.clientsellingmedicine.interfaces.IOnCheckboxChangedListener;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity implements IOnCheckboxChangedListener {
    private Context mContext;
    cartAdapter cartAdapter = new cartAdapter();
    RecyclerView rcvCart;
    LinearLayout bottom_view, linear_layout_dynamic;

    TextView tvTotalAmountCart, tvTotalItemCart, tvDelete, tvTotalPrice, tvTotalDiscount, tvTotalDiscountVoucher;


    ImageView icon_arrow_up, ivBackCart;

    CheckBox checkboxCartItem, masterCheckboxCart;

    List<CartItem> listProductsToBuy;

    private Boolean isShowBottomView = false;

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
        bottom_view = findViewById(R.id.bottom_view);
        linear_layout_dynamic = findViewById(R.id.linear_layout_dynamic);
        icon_arrow_up = findViewById(R.id.icon_arrow_up);
        tvTotalAmountCart = findViewById(R.id.tvTotalAmountCart);

        masterCheckboxCart = findViewById(R.id.masterCheckboxCart);
        tvTotalItemCart = findViewById(R.id.tvTotalItemCart);
        ivBackCart = findViewById(R.id.ivBackCart);
        tvDelete = findViewById(R.id.tvDelete);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalDiscount = findViewById(R.id.tvTotalDiscount);
        tvTotalDiscountVoucher = findViewById(R.id.tvTotalDiscountVoucher);
    }

    private void addEvents() {
        cartAdapter.setOnCheckboxChangedListener(this);

        getCartItems();

        ivBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icon_arrow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });


        masterCheckboxCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterCheckboxCart.isChecked())
                    cartAdapter.setAllSelected(true);
                else
                    cartAdapter.setAllSelected(false);

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set title
                        .setTitle("Xác nhận xóa sản phẩm")
                        //set message
                        .setMessage("Bạn có chắc chắn muốn xóa những sản phẩm này?")
                        //set positive button
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            //set what would happen when positive button is clicked
                            cartAdapter.removeItems();
                            tvTotalItemCart.setText("(" + cartAdapter.getItemCount() + ")");
                        })
                        //set negative button
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            //set what should happen when negative button is clicked

                        })
                        .show();
            }
        });


    }


    public void getCartItems() {
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<List<CartItem>> request = cartService.getCart();
        request.enqueue(new Callback<List<CartItem>>() {

            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
//                    cartAdapter = new cartAdapter(response.body());
                    cartAdapter.setListCartItems(response.body());
//                    cartAdapter.setOnCheckboxChangedListener(CartActivity.this);
                    tvTotalItemCart.setText("(" + cartAdapter.getItemCount() + ")"); // set total item in cart
                    rcvCart.setAdapter(cartAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    rcvCart.setLayoutManager(layoutManager);

                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
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

    public String convertPrice(double number) {
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart;
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
        tvTotalPrice.setText(totalAmount);
        tvTotalAmountCart.setText(totalAmount);
    }


//    public double calculateTotalAmount() {
//        double totalAmount = 0;
//        if (cartAdapter.listProductsToBuy != null) {
//            Log.d("TAG", "--------cartAdapter-----:  " + cartAdapter.listProductsToBuy.size());
//            for (int i = 0; i < cartAdapter.listProductsToBuy.size(); i++) {
//                Cart cart = cartAdapter.listProductsToBuy.get(i);
//
//                totalAmount += cart.getPrice() * cart.getQuantity();
//            }
//        }
//        Log.d("TAG", "calculateTotalAmount: " + totalAmount);
//        return totalAmount;
//    }

}
