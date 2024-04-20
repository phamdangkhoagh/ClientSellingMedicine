package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.CouponDetail;
import com.example.clientsellingmedicine.utils.Convert;

import java.util.List;


public class redeemAdapter extends RecyclerView.Adapter<redeemAdapter.ViewHolder> {
    private List<CouponDetail> mCouponDetails;
    private Context mContext;


    public redeemAdapter(List<CouponDetail> list) {
        this.mCouponDetails = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.accumulate_points_history_item, parent, false);

        redeemAdapter.ViewHolder viewHolder = new redeemAdapter.ViewHolder(newsView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CouponDetail couponDetail = mCouponDetails.get(position);
        if (couponDetail == null) {
            return;
        }


        holder.tv_label_code.setText("Mã giảm giá:");
        holder.tv_Code.setText(couponDetail.getCoupon().getCode());
        // Expire date = current date + expire date
        String time = couponDetail.getStartTime().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = Convert.convertToDate(time);
        }
        holder.tv_Time.setText(time);
        holder.tv_Point.setText("  -" + couponDetail.getCoupon().getPoint());
        holder.tv_Point.setTextColor(Color.parseColor("#FF0F00"));

    }

    @Override
    public int getItemCount() {
        return mCouponDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_label_code, tv_Code, tv_Time, tv_Point;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tv_label_code = itemView.findViewById(R.id.tv_label_code);
            tv_Code = itemView.findViewById(R.id.tv_Code);
            tv_Time = itemView.findViewById(R.id.tv_Time);
            tv_Point = itemView.findViewById(R.id.tv_Point);

        }
    }
}
