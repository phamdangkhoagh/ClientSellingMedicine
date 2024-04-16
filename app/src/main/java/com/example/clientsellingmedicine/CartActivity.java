package com.example.clientsellingmedicine;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.cartAdapter;
import com.example.clientsellingmedicine.Adapter.couponCheckboxAdapter;
import com.example.clientsellingmedicine.interfaces.IOnCartItemListener;
import com.example.clientsellingmedicine.interfaces.IOnVoucherItemClickListener;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.CouponDetail;
import com.example.clientsellingmedicine.models.Total;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.CouponService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity implements IOnCartItemListener, IOnVoucherItemClickListener {
    private Context mContext;
    cartAdapter cartAdapter = new cartAdapter();
    couponCheckboxAdapter couponCheckboxAdapter ;
    RecyclerView rcvCart;
    LinearLayout bottom_view, linear_layout_dynamic;
    TextView tv_TotalAmountCart, tvTotalItemCart, tvDelete,tv_Discount, tv_TotalPrice, tv_TotalProductDiscount, tv_TotalVoucherDiscount;
    LinearLayout ll_Discount;
    ImageView icon_arrow_up, ivBackCart;
    CheckBox checkboxCartItem, masterCheckboxCart;
    List<CartItem> listProductsToBuy;
    Integer voucherDiscountPercent = 0;

    Integer totalProductDiscount = 0;
    Integer positionVoucherItemSelected = -1;

    Boolean isDialogShowing = false;
    Button btn_Buy,btn_Apply;
    TextInputEditText txt_input_code;
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
        btn_Buy = findViewById(R.id.btn_Buy);

        masterCheckboxCart = findViewById(R.id.masterCheckboxCart);
        tvTotalItemCart = findViewById(R.id.tvTotalItemCart);
        ivBackCart = findViewById(R.id.ivBackCart);
        tvDelete = findViewById(R.id.tvDelete);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
        tv_TotalProductDiscount = findViewById(R.id.tv_TotalProductDiscount);
        tv_TotalVoucherDiscount = findViewById(R.id.tv_TotalVoucherDiscount);
        tv_Discount = findViewById(R.id.tv_Discount);
        ll_Discount = findViewById(R.id.ll_Discount);
    }

    private void addEvents() {
        cartAdapter.setOnCheckboxChangedListener(this);

        // get cart items
        getCartItems();

        // back to previous screen
        ivBackCart.setOnClickListener(v -> finish());

        // show or hide discount view
        ll_Discount.setOnClickListener(v -> {
            showSelectCouponDialog();
        });


        // show or hide bottom view
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

        // checkbox master
        masterCheckboxCart.setOnClickListener(v -> {
            if (masterCheckboxCart.isChecked())
                cartAdapter.setAllSelected(true);
            else
                cartAdapter.setAllSelected(false);
        });

        // delete cart item
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

        // buy products
        btn_Buy.setOnClickListener(v -> {
            buyProducts();
        });
    }


    public void buyProducts() {
        // get list cart items checked
        Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> listCartItemsChecked = SharedPref.loadData(CartActivity.this, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
        if(listCartItemsChecked != null){

        }
        else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setIcon(R.drawable.ic_warning) // Đặt icon của Dialog
                    .setTitle("Không có sản phẩm nào được chọn")
                    .setMessage("Vui lòng chọn ít nhất 1 sản phẩm trước khi thanh toán !")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi nhấn nút OK

                        }
                    })
                    .show();
        }
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
    public void getTotal(Total total) {
        tv_TotalPrice.setText(Convert.convertPrice(total.getTotalPrice())); //display total price
        tv_TotalProductDiscount.setText(Convert.convertPrice(total.getTotalProductDiscount())); // display total product discount

        if(voucherDiscountPercent == 0){
            if(total.getTotalPrice() == 0){
                String totalAmount = Convert.convertPrice(0); // calculate total amount without voucher discount
                tv_TotalVoucherDiscount.setText( "0 đ"); // display voucher discount
                tv_TotalAmountCart.setText(totalAmount); // display total amount
            }
            else {
                String totalAmount = Convert.convertPrice(total.getTotalPrice()  - total.getTotalProductDiscount()); // calculate total amount without voucher discount
                tv_TotalVoucherDiscount.setText( "0 đ"); // display voucher discount
                tv_TotalAmountCart.setText(totalAmount); // display total amount
            }

        }
        else {
            if(total.getTotalPrice() == 0){
                int totalVoucherDiscount =  0;
                int  totalAmount = 0; // calculate total amount
                tv_TotalVoucherDiscount.setText( Convert.convertPrice(totalVoucherDiscount)); // display voucher discount
                tv_TotalAmountCart.setText(Convert.convertPrice(totalAmount)); // display total amount
            }else {
                int totalVoucherDiscount =  (total.getTotalPrice() * voucherDiscountPercent / 100);
                int  totalAmount = total.getTotalPrice() - totalVoucherDiscount - total.getTotalProductDiscount(); // calculate total amount
                tv_TotalVoucherDiscount.setText( Convert.convertPrice(totalVoucherDiscount)); // display voucher discount
                tv_TotalAmountCart.setText(Convert.convertPrice(totalAmount)); // display total amount
            }

        }
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

    public List<CouponDetail> getCoupons() {
        CouponService couponService = ServiceBuilder.buildService(CouponService.class);
        Call<List<CouponDetail>> call = couponService.getCoupon();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<CouponDetail>> future = executorService.submit(new Callable<List<CouponDetail>>() {
            @Override
            public List<CouponDetail> call() throws Exception {
                try {
                    Response<List<CouponDetail>> response = call.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    } else if (response.code() == 401) {
                        // Xử lý khi mã trạng thái là 401 (Unauthorized)
                        // Ví dụ: chuyển đến màn hình đăng nhập
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        finish();
                        mContext.startActivity(intent);
                        return null;
                    } else {
                        Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                        return null;
                    }
                } catch (IOException e) {
                    Toast.makeText(mContext, "A connection error occurred", Toast.LENGTH_LONG).show();
                    return null;
                }
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
    private void showSelectCouponDialog() {

        if(isDialogShowing){
            return;
        }

        final Dialog dialog = new Dialog(this);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_select_coupon);

        // add control
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        TextInputLayout txt_input_code_layout = dialog.findViewById(R.id.txt_input_code_layout);
        txt_input_code = dialog.findViewById(R.id.txt_input_code);
        btn_Apply = dialog.findViewById(R.id.btn_Apply);
        RecyclerView rcv_coupon = dialog.findViewById(R.id.rcv_coupon);

        // add event
        iv_close.setOnClickListener(v -> dialog.dismiss()); // close dialog

        txt_input_code.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    btn_Apply.setEnabled(true);
                } else {
                    btn_Apply.setEnabled(false);
                }
            }
        });

        couponCheckboxAdapter = new couponCheckboxAdapter(getCoupons(), CartActivity.this, positionVoucherItemSelected);
        rcv_coupon.setAdapter(couponCheckboxAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rcv_coupon.setLayoutManager(layoutManager);

        btn_Apply.setOnClickListener( v -> {
            CouponDetail couponDetail = couponCheckboxAdapter.getCouponSelected();
            voucherDiscountPercent = couponDetail.getCoupon().getDiscountPercent();  // get voucher discount percent
            positionVoucherItemSelected = couponCheckboxAdapter.getPositionVoucherSelected(); // get position of voucher selected
            handlerApplyCoupon(couponDetail);

            dialog.dismiss();
        });

        dialog.setOnShowListener( dialog1 -> {
            isDialogShowing = true;
        });
        dialog.setOnDismissListener( dialog1 -> {
            isDialogShowing = false;
        });

        // show dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void handlerApplyCoupon(CouponDetail couponDetail) {
        if(couponDetail != null){
            tv_Discount.setText("Mã khuyến mãi: "+ couponDetail.getCoupon().getCode()); // display coupon code
            int total = Convert.convertCurrencyFormat(tv_TotalPrice.getText().toString().trim()); // get total price
            int totalProductDiscount = Convert.convertCurrencyFormat(tv_TotalProductDiscount.getText().toString().trim()); // get total product discount
            int totalVoucherDiscount = couponDetail.getCoupon().getDiscountPercent() * total / 100; // calculate voucher discount
            int totalAmountCart = total - totalVoucherDiscount - totalProductDiscount; // calculate total amount
            tv_TotalVoucherDiscount.setText(Convert.convertPrice(totalVoucherDiscount)); // display voucher discount
            tv_TotalAmountCart.setText( Convert.convertPrice(totalAmountCart)); // display total amount
        }
        else {
            tv_Discount.setText("Chọn hoặc nhập mã khuyến mãi"); // display coupon code
            int total = Convert.convertCurrencyFormat(tv_TotalPrice.getText().toString().trim()); // get total price
            int totalProductDiscount = Convert.convertCurrencyFormat(tv_TotalProductDiscount.getText().toString().trim()); // get total product discount
            int totalAmountCart = total - totalProductDiscount; // calculate total amount
            tv_TotalVoucherDiscount.setText("0 đ"); // display voucher discount
            tv_TotalAmountCart.setText( Convert.convertPrice(totalAmountCart)); // display total amount
        }
    }

    @Override
    public void onVoucherItemClick(int position) {
        if(position == -1 && txt_input_code.getText().toString().isEmpty()){
            btn_Apply.setEnabled(false);
            CouponDetail couponDetail = couponCheckboxAdapter.getCouponSelected();
            voucherDiscountPercent = 0;
            positionVoucherItemSelected = -1;
            handlerApplyCoupon(couponDetail);
        }
        if(position != -1){
            btn_Apply.setEnabled(true);
        }
    }
}
