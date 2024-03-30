package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.SQLite.CartDAO;
import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Product;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private Context mContext;
    public static List<CartItem> listCartItems;

//    public List<Cart> listProductsToBuy;
    CartDAO cartDAO;

    public cartAdapter(List<CartItem> listCartItems) {
        this.listCartItems = listCartItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCartItem, tvPriceCartItem, tvTotalAmountCart, tvQuantityCartItem;
        public ImageView ivCartItem;

        public CheckBox checkboxCartItem, masterCheckboxCart;

        public ViewHolder(View itemView, Context context) {
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
        String price = convertPrice(cart.getPrice());
        holder.tvPriceCartItem.setText(price + " đ");

        // Add cartItem into database
        holder.checkboxCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = holder.getAdapterPosition();
                CartItem cartItem = listCartItems.get(position);
                CartDAO cartDAO = new CartDAO(holder.itemView.getContext());
                if (isChecked){
                    Log.d("check", "checked: " + isChecked);
                    cartDAO.insertItem(cartItem);
                    Log.d("check", "cartItem.getID: " + cartItem.getId());
                }else{
                    cartDAO.deleteItem(cartItem.getId() + "");
                    Log.d("uncheck", "uncheck: " + isChecked);
                    Log.d("uncheck", "cartItem.getID: " + cartItem.getId());
                }
            }
        });

        // setChecked cartItems
        List<CartItem> cartItems = getAllCartItems(holder.itemView.getContext());
        Log.d("TAG", "Size cartItems: " + cartItems.size());


        for (CartItem item :cartItems){
            if(cart.getId() == item.getId()){
                holder.checkboxCartItem.setChecked(true);
            }
            Log.d("checkbox", "item.getId(): " + item.getId());
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

    public String convertPrice(double number) {
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart;
    }

    public List<CartItem> getAllCartItems(Context context){
        CartDAO cartDAO = new CartDAO(context);
        return cartDAO.getProductsAll();
    }







}
