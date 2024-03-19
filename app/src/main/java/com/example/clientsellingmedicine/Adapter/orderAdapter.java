package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.Cart;
import com.example.clientsellingmedicine.models.Order;

import java.util.List;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    List<Order> listOrder;

    private Context mContext;

    public orderAdapter(List<Order> listOrder) {
        this.listOrder = listOrder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvOrderCode, tvTotalPrice, tvAddress,orderStatus;
        public LinearLayout statusBackground;


        public ViewHolder (View itemView, Context context){
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            statusBackground = itemView.findViewById(R.id.statusBackground);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
    @NonNull
    @Override
    public orderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsview = inflater.inflate(R.layout.order_item,parent,false);

        orderAdapter.ViewHolder viewHolder = new orderAdapter.ViewHolder(newsview,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull orderAdapter.ViewHolder holder, int position) {
        Order order = (Order) listOrder.get(position);

        holder.tvOrderCode.setText(order.getCode());
        String price = convertPrice(order.getTotal());
        holder.tvTotalPrice.setText(price+" đ");
        holder.tvAddress.setText("Nhà thuốc Pharmacity");
        if(order.getStatus() == 1){
            holder.orderStatus.setText("Thành công");
            holder.statusBackground.setBackgroundResource(R.drawable.success_background);
        }
        else if(order.getStatus() == 0){
            holder.orderStatus.setText("Thất bại");
            holder.statusBackground.setBackgroundResource(R.drawable.dicount_background);
        }

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public String convertPrice(double number) {
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart;
    }
}
