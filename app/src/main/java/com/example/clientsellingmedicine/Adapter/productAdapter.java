package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnItemClickListenerRecyclerView;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.utils.Convert;

import java.util.List;

public class productAdapter extends RecyclerView.Adapter <productAdapter.ViewHolder> {

    private List<Product> mProducts;
    private Context mContext;

    private IOnItemClickListenerRecyclerView mListener;

    public productAdapter(List<Product> list, IOnItemClickListenerRecyclerView listener) {
        this.mProducts = list;
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameProductItem,tvProductPrice;
        public ImageView ivProductItem;

        public LinearLayout layout_Discount,layoutProductItem;



        public Button btnAddtoCartProduct;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameProductItem = itemView.findViewById(R.id.tvNameProductItem);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductItem = itemView.findViewById(R.id.ivProductItem);
            btnAddtoCartProduct = itemView.findViewById(R.id.btnAddtoCartProduct);
            ivProductItem = itemView.findViewById(R.id.ivProductItem);
            layout_Discount = itemView.findViewById(R.id.layout_Discount);
            layoutProductItem = itemView.findViewById(R.id.layoutProductItem);
            //xử lý sự kiện khi click nút view
            btnAddtoCartProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.product_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(newsView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layout_Discount.setVisibility(View.GONE);
        Product product = (Product) mProducts.get(position);
        if (product == null) {
            return;
        }

        holder.tvNameProductItem.setText(product.getName());
        String unit = product.getUnit();
        String price = Convert.convertPrice(product.getPrice());
        holder.tvProductPrice.setText(price+"/"+unit);
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivProductItem);

        holder.layoutProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(product);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }



}
