package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UnLoginProfileFragment extends Fragment {
    private Context mContext;

    Button btn_login, btn_register;
    public UnLoginProfileFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_no_login_screen, container, false);
        mContext = view.getContext();
        // Thực hiện các thao tác cần thiết trên giao diện view của fragment
        addControl(view);
        addEvents();



        return view;
    }



    private void addControl(View view){
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
    }
    private void addEvents(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình đăng nhập
                Toast.makeText(mContext, "Chuyển sang màn hình đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình đăng ký
                Toast.makeText(mContext, "Chuyển sang màn hình đăng ký", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
