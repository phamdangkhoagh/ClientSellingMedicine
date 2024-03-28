package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.Cart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    public static List<Cart> listProductsCart;

    public static List<Cart> listProductsToBuy = new ArrayList<>();

    private Context mContext;


    public cartAdapter(List<Cart> list){
        this.listProductsCart = list;
    }

    public List<Cart> getListProductsCart() {
        return listProductsCart;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNameCartItem, tvPriceCartItem,tvTotalAmountCart,tvQuantityCartItem;
        public ImageView ivCartItem;

        public CheckBox checkboxCartItem,masterCheckboxCart;

        public ViewHolder (View itemView, Context context) {
            super(itemView);
            tvNameCartItem = itemView.findViewById(R.id.tvNameCartItem);
            tvPriceCartItem = itemView.findViewById(R.id.tvPriceCartItem);
            ivCartItem = itemView.findViewById(R.id.ivCartItem);
            masterCheckboxCart = itemView.findViewById(R.id.masterCheckboxCart);
            checkboxCartItem = itemView.findViewById(R.id.checkboxCartItem);
            tvTotalAmountCart = itemView.findViewById(R.id.tvTotalAmountCart);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsview = inflater.inflate(R.layout.cart_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(newsview,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, int position) {
        Cart cart = listProductsCart.get(position);
        holder.tvNameCartItem.setText(cart.getName());
        int quantity = cart.getQuantity();
        holder.tvQuantityCartItem.setText(String.valueOf(quantity));
        String price = convertPrice(cart.getPrice());
        holder.tvPriceCartItem.setText(price+" đ");

        holder.checkboxCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = holder.getAdapterPosition();
                if (isChecked){
                    Cart cart = listProductsCart.get(position);
                    listProductsToBuy.add(cart);

                    if (listProductsToBuy.contains(cart)) {
                        Log.d("TAG", "listProductsToBuy contains cart ID: " + cart.getId() + " Price" + cart.getPrice());
                    } else {
                        Log.d("TAG", "listProductsToBuy does not contain cart");
                    }
                }else{
                    for (int i = 0;i<listProductsToBuy.size();i++){
                        if (listProductsToBuy.get(i).getId() == cart.getId()){
                            listProductsToBuy.remove(i);
                        }
                    }
                }
            }
        });


        Glide.with(holder.itemView.getContext())
                .load(cart.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivCartItem);
    }

    @Override
    public int getItemCount() {
        return listProductsCart.size();
    }
    public String convertPrice(double number) {
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart;
    }

}
