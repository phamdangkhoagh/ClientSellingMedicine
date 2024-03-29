package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.utils.Convert;


public class DetailProductActivity extends AppCompatActivity {
    private Context mContext;

    private ImageView imv_Back,imv_ProductImage;
    private TextView tv_ProductName,tv_ProductPrice,tv_ProductCode;

    private Button btn_AddToCart,btn_BuyNow;

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
            Toast.makeText(mContext, "Add to Cart", Toast.LENGTH_SHORT).show();
        });

        //get detail product from previous screen
        getDetailProduct();
    }

    private void getDetailProduct(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Product product = (Product) bundle.getSerializable("product");

            tv_ProductName.setText(product.getName());
            String price = Convert.convertPrice(product.getPrice());
            tv_ProductPrice.setText(price+" đ/"+product.getUnit());
//            tv_ProductCode.setText(productCode);
            Glide.with(mContext)
                    .load(product.getImage())
                    .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                    .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                    .into(imv_ProductImage);
        }
    }
}
