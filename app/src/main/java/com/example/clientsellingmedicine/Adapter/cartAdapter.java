package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private Context mContext;
    public static List<CartItem> listCartItems;
    public static List<CartItem> listCartItemsChecked = new ArrayList<>();

    private boolean isAllSelected = false;
    public cartAdapter(List<CartItem> listCartItems) {
        this.listCartItems = listCartItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCartItem, tvPriceCartItem, tvTotalAmountCart, tvQuantityCartItem;
        public ImageView ivCartItem;

        public CheckBox checkboxCartItem;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameCartItem = itemView.findViewById(R.id.tvNameCartItem);
            tvPriceCartItem = itemView.findViewById(R.id.tvPriceCartItem);
            ivCartItem = itemView.findViewById(R.id.ivCartItem);

            checkboxCartItem = itemView.findViewById(R.id.checkboxCartItem);

            this.setIsRecyclable(false);

            tvTotalAmountCart = itemView.findViewById(R.id.tvTotalAmountCart);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsview = inflater.inflate(R.layout.cart_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(newsview, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, int position) {
        CartItem cart = listCartItems.get(position);
        holder.tvNameCartItem.setText(cart.getName());
        int quantity = cart.getQuantity();
        holder.tvQuantityCartItem.setText(String.valueOf(quantity));
        String price = Convert.convertPrice(cart.getPrice());
        holder.tvPriceCartItem.setText(price + " đ");

        holder.checkboxCartItem.setChecked( isAllSelected());

        // Add cartItem into database
        holder.checkboxCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkboxCartItem.isChecked()){
                    Log.d("tag", " Item checked: " + cart.getId());
                    listCartItemsChecked.add(cart);
                    // Save listCartItemsChecked to SharedPreferences
                    SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, "cartSharedPrefs","listCartItemsChecked");

                }else{
                    Log.d("tag", " Item remove: " + cart.getId());
                    // Remove item from listCartItemsChecked
                    for (CartItem cartItem : listCartItemsChecked) {
                        if ( cart.getId() == cartItem.getId()) {
                            listCartItemsChecked.remove(cartItem);
                            break;
                        }
                    }
                    // Save listCartItemsChecked to SharedPreferences
                    SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, "cartSharedPrefs","listCartItemsChecked");
                }
            }
        });



        Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
        //get listCartItemsChecked from SharedPreferences
        listCartItemsChecked = SharedPref.loadData(holder.itemView.getContext(), "cartSharedPrefs", "listCartItemsChecked", cartItemType);

        if(listCartItemsChecked != null){
            Log.d("check", "list Size: " + listCartItemsChecked.size());
            for ( CartItem item : listCartItemsChecked){
                if(cart.getId() == item.getId()){
                    holder.checkboxCartItem.setChecked(true);  // checked item in cart
                }
            }
        }

        Glide.with(holder.itemView.getContext())
                .load(cart.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivCartItem);
    }

    @Override
    public int getItemCount() {
        return listCartItems.size();
    }

    public void selectAllCartItem() {
        listCartItemsChecked.clear();
        for (CartItem data : listCartItems) {
            listCartItemsChecked.add(data);
        }
        notifyDataSetChanged();
    }


    public void setAllSelected(boolean selected) {
        isAllSelected = selected;
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        return isAllSelected;
    }







}
