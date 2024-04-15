package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
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
import com.example.clientsellingmedicine.interfaces.IOnCartItemListener;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private IOnCartItemListener onCheckboxChangedListener;

    public void setOnCheckboxChangedListener(IOnCartItemListener listener) {
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
        public TextView tvNameCartItem, tvPriceCartItem, tvQuantityCartItem,tv_MinusCartItem, tv_PlusCartItem;
        public ImageView ivCartItem;

        public CheckBox checkboxCartItem;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameCartItem = itemView.findViewById(R.id.tvNameCartItem);
            tvPriceCartItem = itemView.findViewById(R.id.tvPriceCartItem);
            ivCartItem = itemView.findViewById(R.id.ivCartItem);
            checkboxCartItem = itemView.findViewById(R.id.checkboxCartItem);
            this.setIsRecyclable(false);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
            tv_MinusCartItem = itemView.findViewById(R.id.tv_MinusCartItem);
            tv_PlusCartItem = itemView.findViewById(R.id.tv_PlusCartItem);

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
        if (cart == null) {
            return;
        }
        holder.tvNameCartItem.setText(cart.getProduct().getName());
        int quantity = cart.getQuantity();
        holder.tvQuantityCartItem.setText(String.valueOf(quantity));
        String price = Convert.convertPrice(cart.getProduct().getPrice());
        holder.tvPriceCartItem.setText(price);

        // minus quantity
        holder.tv_MinusCartItem.setOnClickListener(v -> {
            if (quantity > 1) {
                updateQuantityCartItem(holder.itemView.getContext(), cart, -1);   // update on recycler view and shared preferences

            }
        });
        // plus quantity
        holder.tv_PlusCartItem.setOnClickListener(v -> {
            updateQuantityCartItem(holder.itemView.getContext(), cart, 1);  // update on recycler view and shared preferences
        });


//        holder.checkboxCartItem.setChecked(isAllSelected());

        Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
        //get CartItems Checked from SharedPreferences
        listCartItemsChecked = SharedPref.loadData(holder.itemView.getContext(), Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
        //if new user, CartItems Checked will be null
        if (listCartItemsChecked == null) {
            listCartItemsChecked = new ArrayList<>();
        }
        if (listCartItemsChecked != null) {
            for (CartItem item : listCartItemsChecked) {
                Log.d("tag", "onBindViewHolder: "+ (item.getProduct().equals(cart.getProduct())) );
                if (item.getProduct().equals(cart.getProduct())) {
                    holder.checkboxCartItem.setChecked(true);
                    break;// checked item in cart
                }
            }
        }

        holder.checkboxCartItem.setOnClickListener(v -> {
            if (holder.checkboxCartItem.isChecked()) {

                Log.d("tag", "check item: " + cart.getProduct().getName() + " checked");
                listCartItemsChecked.add(cart);
                // Save CartItems Checked to SharedPreferences
                SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

                //set value for master checkbox
                onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size());

                //set status for delete text
                onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);

                // get Total Amount Item Checked
                onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
                // get Total Product Discount
                onCheckboxChangedListener.getTotalProductDiscount(calculateTotalProductDiscount());

            } else {
                // Remove item from CartItems Checked
                for (CartItem cartItem : listCartItemsChecked) {
                    if (cartItem.getProduct().equals(cart.getProduct())) {
                        listCartItemsChecked.remove(cartItem);
                        break;
                    }
                }
                // Save CartItems Checked to SharedPreferences
                SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

                //set value for master checkbox
                onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size());

                //set status for delete text
                onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);

                // get Total Amount Item Checked
                onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
                // get Total Product Discount
                onCheckboxChangedListener.getTotalProductDiscount(calculateTotalProductDiscount());
            }

        });


        //set value for master checkbox
        onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size() && listCartItems.size() != 0);

        //set status for delete items text
        onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
        // get Total Product Discount
        onCheckboxChangedListener.getTotalProductDiscount(calculateTotalProductDiscount());

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
            // update CartItems Checked to SharedPreferences
            SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
        } else {
            // clear CartItems Checked
            this.listCartItemsChecked.clear();
            // update CartItems Checked to SharedPreferences
            SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
        }
        notifyDataSetChanged();
    }

    public void setListCartItems(List<CartItem> list) {
        this.listCartItems = list;
        notifyDataSetChanged();
    }

    public void removeItems(CartItem cartItem) {
        // Remove items from list Cart Items
        listCartItems.remove(cartItem);
        // update CartItems for RecyclerView
        setListCartItems(listCartItems);
        // Remove item from CartItems Checked
        Log.d("tag", "remove: " + listCartItemsChecked.size());
        listCartItemsChecked.remove(cartItem);
        Log.d("tag", " after remove: " + listCartItemsChecked.size());
        // Save CartItems Checked to SharedPreferences
        SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

        //set value for master checkbox
        onCheckboxChangedListener.setValueOfMasterCheckbox(listCartItemsChecked.size() == listCartItems.size() && listCartItems.size() != 0);
        //set status for delete items text
        onCheckboxChangedListener.setStatusOfDeleteText(listCartItemsChecked.size() != 0);
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
        // get Total Product Discount
        onCheckboxChangedListener.getTotalProductDiscount(calculateTotalProductDiscount());
    }

    public int calculateTotalAmount() {
        int total = 0;
        if(listCartItemsChecked == null)
            return 0;
        Log.d("tag", "listCartItemsChecked: " + listCartItemsChecked.size());
        for (CartItem item: listCartItemsChecked) {
            Log.d("tag", "item: " + item.getProduct().getPrice() + " * " + item.getQuantity() + " = " + item.getProduct().getPrice() * item.getQuantity());
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public int calculateTotalProductDiscount() {
        int total = 0;
        if(listCartItemsChecked == null)
            return 0;
        for (CartItem item: listCartItemsChecked) {
            int discountPercent = item.getProduct().getDiscountPercent();
            int price = item.getProduct().getPrice();
            total += (price * discountPercent) / 100;
        }
        return total;
    }

    public void updateQuantityCartItem(Context context, CartItem item, int quantity) {
        int oldQuantity = item.getQuantity();
        int newQuantity = oldQuantity + quantity;
        item.setQuantity(newQuantity);
        notifyDataSetChanged();
        // Save CartItems Checked to SharedPreferences
        for (CartItem itemChecked : listCartItemsChecked) {
            if (itemChecked.getProduct().equals(item.getProduct())) {
                itemChecked.setQuantity(newQuantity);
                SharedPref.saveData(context, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
                break;
            }
        }
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotalAmount(calculateTotalAmount());
        // get Total Product Discount
        onCheckboxChangedListener.getTotalProductDiscount(calculateTotalProductDiscount());
        //  update on database
        onCheckboxChangedListener.updateCartItemQuantity(item);
    }
}
