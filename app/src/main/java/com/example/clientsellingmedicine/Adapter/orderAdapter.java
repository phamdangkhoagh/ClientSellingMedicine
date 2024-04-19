package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnOrderItemClickListener;
import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.utils.Convert;

import java.util.Date;
import java.util.List;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    List<Order> listOrder;

    private Context mContext;

    private IOnOrderItemClickListener listener;

    public orderAdapter(List<Order> listOrder,IOnOrderItemClickListener listener) {
        this.listOrder = listOrder;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvOrderCode, tvTotalPrice, tvAddress,orderStatus;
        public LinearLayout statusBackground,ll_orderItem;


        public ViewHolder (View itemView, Context context){
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            statusBackground = itemView.findViewById(R.id.statusBackground);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            ll_orderItem = itemView.findViewById(R.id.ll_orderItem);
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
        if(order == null){
            return;
        }
        holder.tvOrderCode.setText(order.getCode());
        String price = Convert.convertPrice(order.getTotal());
        holder.tvTotalPrice.setText(price);
        String date = order.getOrderTime().toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             date = Convert.convertToDate(order.getOrderTime().toString());
        }
        holder.tvAddress.setText(date);
        if(order.getStatus() == 1){
            holder.orderStatus.setText("Thành công");
            holder.statusBackground.setBackgroundResource(R.drawable.success_background);
        }
        else if(order.getStatus() == 0){
            holder.orderStatus.setText("Thất bại");
            holder.statusBackground.setBackgroundResource(R.drawable.dicount_background);
        }
        else if(order.getStatus() == 2){
            holder.orderStatus.setText("Chờ xử lý");
            holder.statusBackground.setBackgroundResource(R.drawable.pending_background);
        }
        else if(order.getStatus() == 3){
            holder.orderStatus.setText("Đang giao");
            holder.statusBackground.setBackgroundResource(R.drawable.shipping_background);
        }

        // get item when click
        holder.ll_orderItem.setOnClickListener(view -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public void setListOrder(List<Order> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }


}
