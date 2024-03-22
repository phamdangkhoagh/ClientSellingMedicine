package com.example.clientsellingmedicine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientsellingmedicine.Adapter.cartAdapter;
import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.models.Cart;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.IdeaService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {
    private Context mContext;
    RecyclerView rcvCart;
    cartAdapter cartAdapter;
    LinearLayout bottom_view,linear_layout_dynamic;

    ImageView icon_arrow_up;

    CheckBox checkboxCartItem,masterCheckboxCart;



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
        checkboxCartItem = findViewById(R.id.checkboxCartItem);
        masterCheckboxCart = findViewById(R.id.masterCheckboxCart);
    }

    private void addEvents() {
        getCartItems();

//        masterCheckboxCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                checkboxCartItem.setChecked(isChecked);
//            }
//        });

        // Listener for the individual checkboxes
//        checkboxCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    masterCheckboxCart.setChecked(false);
//                } else {
//                    // Check if all checkboxes are checked
//                    boolean allChecked = areAllCheckboxesChecked();
//                    // Update masterCheckbox's state
//                    if(allChecked) {
//                        masterCheckboxCart.setChecked(true);
//                    }
//                }
//            }
//        });


//        checkboxCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // Đánh dấu CheckBox đã được tích
//                    checkboxCartItem.setChecked(true);
//                    isChecked = true;
//                } else {
//                    // Đánh dấu CheckBox chưa được tích
//                    checkboxCartItem.setChecked(false);
//                    isChecked = false;
//                }
//            }
//        });
        //  display detail price cart
        icon_arrow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int height = bottom_view.getHeight();
                Log.d("Height", "onClick: "+height);
                if(height == 440){
                    // set new height
                    int desiredHeightInDp = 260;

                    float scale = getResources().getDisplayMetrics().density;
                    int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);

                    ViewGroup.LayoutParams layoutParams = bottom_view.getLayoutParams();
                    layoutParams.height = desiredHeightInPixels;
                    bottom_view.setLayoutParams(layoutParams);

                    //display view
                    linear_layout_dynamic.setVisibility(v.VISIBLE);
                    // set icon down
                   icon_arrow_up.setImageResource(R.drawable.ic_arrow_down);
                }
                else {
                    // set new height
                    int desiredHeightInDp = 160;

                    float scale = getResources().getDisplayMetrics().density;
                    int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);

                    ViewGroup.LayoutParams layoutParams = bottom_view.getLayoutParams();
                    layoutParams.height = desiredHeightInPixels;
                    bottom_view.setLayoutParams(layoutParams);

                    //display view
                    linear_layout_dynamic.setVisibility(v.GONE);
                    // set icon up
                    icon_arrow_up.setImageResource(R.drawable.ic_arrow_up);
                }

            }
        });


        //set state checkbox


    }

    public void getCartItems(){
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<List<Cart>> request = cartService.getCart();
        request.enqueue(new Callback<List<Cart>>() {

            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if(response.isSuccessful()){
                    cartAdapter = new cartAdapter(response.body());
                    rcvCart.setAdapter(cartAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    rcvCart.setLayoutManager(layoutManager);

                } else if(response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Function to check if all checkboxes are checked
    private boolean areAllCheckboxesChecked() {
        // Add other checkboxes to this list as needed
        List<CheckBox> checkboxes = new ArrayList<>();
        checkboxes.add(checkboxCartItem);
        // Add other checkboxes here

        for (CheckBox checkbox : checkboxes) {
            if (!checkbox.isChecked()) {
            }
            return false;
        }
        return true;
    }

}
