package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.couponAdapter;
import com.example.clientsellingmedicine.Adapter.redeemAdapter;
import com.example.clientsellingmedicine.models.Coupon;
import com.example.clientsellingmedicine.models.CouponDetail;
import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.services.CouponService;
import com.example.clientsellingmedicine.services.ServiceBuilder;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemFragment extends Fragment {
    private Context mContext;
    private Button btnBuyNow;
    private LinearLayout ll_AccumulateEmpty;
    private RecyclerView rcvAccumulatePointsHistory;
    private redeemAdapter redeemAdapter;
    public RedeemFragment (){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.redeem_fragment,container,false);
        mContext = view.getContext();
        addControl(view);
        addEvents();
        return view;
    }

    private void addControl(View view) {
        rcvAccumulatePointsHistory = view.findViewById(R.id.rcvAccumulatePointsHistory);
        ll_AccumulateEmpty = view.findViewById(R.id.ll_AccumulateEmpty);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);
    }
    private void addEvents() {
        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        });
        // Get all redeem point history
        getAllRedeemPointHistory();
    }


    private void getAllRedeemPointHistory() {
        CouponService couponService = ServiceBuilder.buildService(CouponService.class);
        Call<List<CouponDetail>> request = couponService.getCoupon();

        request.enqueue(new Callback<List<CouponDetail>>() {
            @Override
            public void onResponse(Call<List<CouponDetail>> call, Response<List<CouponDetail>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        redeemAdapter = new redeemAdapter(response.body().stream()
                                .sorted(Comparator.comparing(CouponDetail::getStartTime).reversed())  // sort by date
                                .collect(Collectors.toList()));
                        rcvAccumulatePointsHistory.setAdapter(redeemAdapter);
                        rcvAccumulatePointsHistory.setLayoutManager(new LinearLayoutManager(mContext));
                    }else {
                        ll_AccumulateEmpty.setVisibility(View.VISIBLE);
                    }

                } else if(response.code() == 401) {

                } else {
                    Toast.makeText(mContext, "Something was wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CouponDetail>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
