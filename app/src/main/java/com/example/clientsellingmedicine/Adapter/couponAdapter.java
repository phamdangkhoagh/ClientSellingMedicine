package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnButtonExchangeCouponClickListener;
import com.example.clientsellingmedicine.models.Coupon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class couponAdapter extends RecyclerView.Adapter<couponAdapter.ViewHolder> {
    private List<Coupon> mCoupons;
    private Context mContext;

    private IOnButtonExchangeCouponClickListener mListener;
    public couponAdapter(List<Coupon> list,IOnButtonExchangeCouponClickListener listener) {
        this.mCoupons = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.accumulate_points_item, parent, false);

        couponAdapter.ViewHolder viewHolder = new couponAdapter.ViewHolder(newsView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coupon coupon = mCoupons.get(position);
        if (coupon == null) {
            return;
        }


        holder.tvNameDiscountItem.setText(coupon.getDescription());
        // Expire date = current date + expire date
        Integer expire_date = coupon.getExpirationTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, expire_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String expire = dateFormat.format(calendar.getTime());
        holder.tvExpireDiscountItem.setText(expire);
        holder.tv_Point.setText(String.valueOf(coupon.getPoint()));

        holder.btn_ExchangePoints.setOnClickListener(v -> {
            mListener.onButtonExchangeCouponItemClick(coupon);
        });


    }

    @Override
    public int getItemCount() {
        return mCoupons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btn_ExchangePoints;
        public TextView tvNameDiscountItem, tvExpireDiscountItem, tv_Point;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameDiscountItem = itemView.findViewById(R.id.tvNameDiscountItem);
            tvExpireDiscountItem = itemView.findViewById(R.id.tvExpireDiscountItem);
            tv_Point = itemView.findViewById(R.id.tv_Point);
            btn_ExchangePoints = itemView.findViewById(R.id.btn_ExchangePoints);

        }
    }
}

