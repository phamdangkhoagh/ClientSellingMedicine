package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientsellingmedicine.Adapter.couponAdapter;
import com.example.clientsellingmedicine.models.Coupon;
import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.services.CouponService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.services.UserService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeFragment extends Fragment {
    private Context mContext;

    private couponAdapter couponAdapter;
    private TextView tvPoints;
    private RecyclerView rcvAccoumlatePointsItem;
    private LinearLayout ll_Empty;
    private Button btnHistory;
    private User user;
    public ExchangeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accumulate_points_screen, container, false);
        mContext = view.getContext();
        addControl(view);
        addEvents();
        return view;
    }



    private void addControl(View view) {
        rcvAccoumlatePointsItem = view.findViewById(R.id.rcvAccoumlatePointsItem);
        tvPoints = view.findViewById(R.id.tvPoints);
        ll_Empty = view.findViewById(R.id.ll_Empty);
        btnHistory = view.findViewById(R.id.btnHistory);
    }
    private void addEvents() {
        loadData();
        // Go to history screen
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, RewardPointsHistoryActvity.class);
            intent.putExtra("points", user.getPoint());
            startActivity(intent);
        });
    }

    private void loadData(){
        getPoints();
        getCoupons();
    }

    private void getPoints(){
        UserService userService = ServiceBuilder.buildService(UserService.class);
        Call<User> request = userService.getUser();

        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    user = response.body();
                    // Set point
                    tvPoints.setText(String.valueOf(user.getPoint()));
                } else if(response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve points", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve points", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getCoupons(){
        CouponService couponService = ServiceBuilder.buildService(CouponService.class);
        Call<List<Coupon>> request = couponService.getCoupons();

        request.enqueue(new Callback<List<Coupon>>() {
            @Override
            public void onResponse(Call<List<Coupon>> call, Response<List<Coupon>> response) {
                if(response.isSuccessful()){
                    if(response.body().size()>0){
                        couponAdapter = new couponAdapter(response.body());
                        rcvAccoumlatePointsItem.setAdapter(couponAdapter);
                        rcvAccoumlatePointsItem.setLayoutManager(new LinearLayoutManager(mContext));
                    }else {
                        ll_Empty.setVisibility(View.VISIBLE);
                    }

                } else if(response.code() == 401) {

                } else {
                    Toast.makeText(mContext, "Something was wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Coupon>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}