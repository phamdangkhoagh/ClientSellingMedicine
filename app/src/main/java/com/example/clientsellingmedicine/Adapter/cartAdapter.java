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
import com.example.clientsellingmedicine.interfaces.IOnCheckboxChangedListener;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private IOnCheckboxChangedListener onCheckboxChangedListener;

    public void setOnCheckboxChangedListener(IOnCheckboxChangedListener listener) {
        this.onCheckboxChangedListener = listener;
    }

    private Context mContext;
    public static List<CartItem> listCartItems;
    public static List<CartItem> listCartItemsChecked = new ArrayList<>();

    private boolean isAllSelected = false;

//    public cartAdapter(List<CartItem> listCartItems) {
//        this.listCartItems = listCartItems;
//    }

    public cartAdapter() {

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

            mContext = context;

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
        holder.tvNameCartItem.setText(cart.getProduct().getName());
        int quantity = cart.getQuantity();
        holder.tvQuantityCartItem.setText(String.valueOf(quantity));
        String price = Convert.convertPrice(cart.getProduct().getPrice());
        holder.tvPriceCartItem.setText(price);

//        holder.checkboxCartItem.setChecked(isAllSelected());

        Type cartItemType = new TypeToken<List<CartItem>>() {
        }.getType();
        //get listCartItemsChecked from SharedPreferences
        listCartItemsChecked = SharedPref.loadData(holder.itemView.getContext(), Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
        //if new user, listCartItemsChecked will be null
        if (listCartItemsChecked == null) {
            listCartItemsChecked = new ArrayList<>();
        }
        if (listCartItemsChecked != null) {
            for (CartItem item : listCartItemsChecked) {
                if (cart.getId() == item.getId()) {
                    holder.checkboxCartItem.setChecked(true);  // checked item in cart
                }
            }
        }

        holder.checkboxCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkboxCartItem.isChecked()) {

                    listCartItemsChecked.add(cart);
                    // Save listCartItemsChecked to SharedPreferences
                    SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

                    //set value for master checkbox
                    onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size());

                    //set status for delete text
                    onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);

                    // get Total Amount Item Checked
                    onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());

                } else {
                    // Remove item from listCartItemsChecked
                    for (CartItem cartItem : listCartItemsChecked) {
                        if (cart.getId() == cartItem.getId()) {
                            listCartItemsChecked.remove(cartItem);
                            break;
                        }
                    }
                    // Save listCartItemsChecked to SharedPreferences
                    SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

                    //set value for master checkbox
                    onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size());

                    //set status for delete text
                    onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);

                    // get Total Amount Item Checked
                    onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
                }

            }
        });


        //set value for master checkbox
        onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size() && listCartItems.size() != 0);

        //set status for delete items text
        onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());

        //load image
        Glide.with(holder.itemView.getContext())
                .load(cart.getProduct().getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivCartItem);
    }

    @Override
    public int getItemCount() {
        return listCartItems.size();
    }


    public void setAllSelected(boolean selected) {
        handleCheckBoxSelectedAll(listCartItems, selected);
        isAllSelected = selected;
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        return isAllSelected;
    }


    // update listCartItemsChecked when user click on checkbox selected all
    public void handleCheckBoxSelectedAll(List<CartItem> list, Boolean isAllSelected) {
        if (isAllSelected) {
            // update listCartItemsChecked
            this.listCartItemsChecked = list;
            // update listCartItemsChecked to SharedPreferences
            SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
        } else {
            // clear listCartItemsChecked
            this.listCartItemsChecked.clear();
            // update listCartItemsChecked to SharedPreferences
            SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
        }
        notifyDataSetChanged();
    }

    public void setListCartItems(List<CartItem> list) {
        this.listCartItems = list;
        notifyDataSetChanged();
    }

    public void removeItems() {
        // Remove items from list Cart Items
        Iterator<CartItem> iterator = listCartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            for (CartItem checkedItem : listCartItemsChecked) {
                if (item.equals(checkedItem)) {
                    iterator.remove();
                    break;
                }
            }
        }
        // update list Cart Items for RecyclerView
        setListCartItems(listCartItems);
        // after remove items, clear listCartItemsChecked
        listCartItemsChecked.clear();
        SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

        //set value for master checkbox
        onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size() && listCartItems.size() != 0);
        //set status for delete items text
        onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
    }

    public int calculateTotalAmount() {
        int total = 0;
        if(listCartItemsChecked == null)
            return 0;
        for (CartItem item: listCartItemsChecked) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

}
