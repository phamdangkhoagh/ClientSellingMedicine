package com.example.clientsellingmedicine.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnVoucherItemClickListener;
import com.example.clientsellingmedicine.models.CouponDetail;
import com.example.clientsellingmedicine.utils.Convert;

import java.util.List;

public class couponCheckboxAdapter extends RecyclerView.Adapter <couponCheckboxAdapter.ViewHolder> {
    private List<CouponDetail> mCoupon;
    private Context mContext;

    private int lastCheckedPosition ;
    private int previousLastCheckedPosition = -1;

    IOnVoucherItemClickListener listener;

    public couponCheckboxAdapter(List<CouponDetail> list, IOnVoucherItemClickListener listener,int lastCheckedPosition) {
        this.mCoupon = list;
        this.listener = listener;
        this.lastCheckedPosition = lastCheckedPosition;
    }

    public Integer getPositionVoucherSelected() {
        return lastCheckedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.coupon_item_checkbox, parent, false);

        couponCheckboxAdapter.ViewHolder viewHolder = new couponCheckboxAdapter.ViewHolder(newsView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CouponDetail coupon = mCoupon.get(position);
        if (coupon == null) {
            return;
        }

        holder.tvNameDiscountItem.setText(coupon.getCoupon().getDescription());
        String date = coupon.getEndTime().toString();
        String date_expiration = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date_expiration = Convert.convertToDate(date);
        }
        holder.tvExpireDiscountItem.setText(date_expiration);

        listener.onVoucherItemClick(lastCheckedPosition); // get position of voucher selected for apply button in first time

        // Set the state of the CheckBox based on the lastCheckedPosition
        holder.cbSelectCoupon.setChecked(position == lastCheckedPosition);

        holder.cbSelectCoupon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Update lastCheckedPosition and previousLastCheckedPosition
                if (lastCheckedPosition != position) {
                    previousLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = position;
                    listener.onVoucherItemClick(position); // get position of voucher selected

                    notifyItemChanged(previousLastCheckedPosition); // Hủy chọn CheckBox trước đó
                    notifyItemChanged(lastCheckedPosition); // Chọn CheckBox mới
                }
            }
            else {
                if (lastCheckedPosition == position) {
                    lastCheckedPosition = -1;
                    previousLastCheckedPosition = -1;
                    listener.onVoucherItemClick(lastCheckedPosition);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoupon.size();
    }

    public CouponDetail getCouponSelected() {
        if (lastCheckedPosition == -1)
            return null;
        return mCoupon.get(lastCheckedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameDiscountItem,tvExpireDiscountItem,tvDetailDiscountItem;
       public CheckBox cbSelectCoupon;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameDiscountItem = itemView.findViewById(R.id.tvNameDiscountItem);
            tvExpireDiscountItem = itemView.findViewById(R.id.tvExpireDiscountItem);
            tvDetailDiscountItem = itemView.findViewById(R.id.tvDetailDiscountItem);
            cbSelectCoupon = itemView.findViewById(R.id.cbSelectCoupon);

        }
    }
}