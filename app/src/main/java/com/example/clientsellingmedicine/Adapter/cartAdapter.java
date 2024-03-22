package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.Cart;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private List<Cart> listProductSelected;
    private Context mContext;

    public CheckBox checkboxCartItem,masterCheckboxCart;

    public cartAdapter(List<Cart> list){
        this.listProductSelected = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNameCartItem, tvPriceCartItem;
        public ImageView ivCartItem;

        private SparseBooleanArray itemStateArray= new SparseBooleanArray();



        public ViewHolder (View itemView, Context context) {
            super(itemView);
            tvNameCartItem = itemView.findViewById(R.id.tvNameCartItem);
            tvPriceCartItem = itemView.findViewById(R.id.tvPriceCartItem);
            ivCartItem = itemView.findViewById(R.id.ivCartItem);

            masterCheckboxCart = itemView.findViewById(R.id.masterCheckboxCart);
            CheckBox checkboxCartItem = itemView.findViewById(R.id.checkboxCartItem);

            itemView.setOnClickListener(this);
            this.setIsRecyclable(false);

            masterCheckboxCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    if (!itemStateArray.get(adapterPosition, false)) {
                        checkboxCartItem.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                    } else {
                        checkboxCartItem.setChecked(false);
                        itemStateArray.put(adapterPosition, false);
                    }
                }
            });
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
        Cart cart = (Cart) listProductSelected.get(position);

        holder.tvNameCartItem.setText(cart.getName());
        String price = convertPrice(cart.getPrice());
        holder.tvPriceCartItem.setText(price+" đ");
        Glide.with(holder.itemView.getContext())
                .load(cart.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivCartItem);

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return listProductSelected.size();
    }
    public String convertPrice(double number) {
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart;
    }

}
