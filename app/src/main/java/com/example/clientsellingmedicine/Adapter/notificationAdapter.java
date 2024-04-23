package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnNotificationItemClickListener;
import com.example.clientsellingmedicine.models.Notification;


import java.text.SimpleDateFormat;
import java.util.List;

public class notificationAdapter extends RecyclerView.Adapter <notificationAdapter.ViewHolder> {
    private List<Notification> mNotifications;
    private Context mContext;

    private IOnNotificationItemClickListener mListener;

    public List<Notification> getmNotifications() {
        return mNotifications;
    }

    public void setmNotifications(List<Notification> mNotifications) {
        this.mNotifications = mNotifications;
    }

    public notificationAdapter(List<Notification> list, IOnNotificationItemClickListener listener) {
        this.mNotifications = list;
        this.mListener = listener;
    }
    public notificationAdapter(List<Notification> list) {
        this.mNotifications = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.notification_item, parent, false);

        notificationAdapter.ViewHolder viewHolder = new notificationAdapter.ViewHolder(newsView,context);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        if (notification == null) {
            return;
        }
        holder.tvTitleNotification.setText(notification.getTitle().toString());
        String content = notification.getContent();
        if (content.length() > 100) {
            content = content.substring(0, 100) + "...";
        }
        holder.tvContentNotification.setText(content);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(notification.getCreateTime());
        holder.tvCreateTimeNotification.setText(formattedDate);
        if (notification.getStatus() == 2) {
            holder.tvStatusNotification.setTextColor(Color.RED);
            holder.tvStatusNotification.setText("Chưa xem");
            holder.layoutNotificationItem.setBackgroundColor(Color.parseColor("#E2DEDE")); // Transparent green
        } else {
            holder.tvStatusNotification.setTextColor(Color.BLACK);
            holder.tvStatusNotification.setText("Đã xem");
        }
        if (notification.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(notification.getImage())
                    .placeholder(R.drawable.loading_icon)
                    .error(R.drawable.error_image)
                    .into(holder.ivNotificationItem);
        } else {
            holder.ivNotificationItem.setImageResource(R.drawable.error_image); // Example
        }
        holder.layoutNotificationItem.setOnClickListener(view -> mListener.onItemClick(notification));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitleNotification,tvContentNotification,tvCreateTimeNotification, tvStatusNotification;
        public ImageView ivNotificationItem;
        private CardView layoutNotificationItem;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvTitleNotification = itemView.findViewById(R.id.tvTitleNotification);
            tvContentNotification = itemView.findViewById(R.id.tvContentNotification);
            tvCreateTimeNotification = itemView.findViewById(R.id.tvCreateTimeNotification);
            tvStatusNotification = itemView.findViewById(R.id.tvStatusNotification);
            ivNotificationItem = itemView.findViewById(R.id.ivNotificationItem);
            layoutNotificationItem = itemView.findViewById(R.id.layoutNotificationItem);

        }
    }
}
