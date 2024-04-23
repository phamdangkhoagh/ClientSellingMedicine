package com.example.clientsellingmedicine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.models.ResponseDto;
import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.services.UserService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private String phoneNumber, password;
    private Context mContext;
    private TextView tvStatusRegister;
    private Button btnReturnLogin, btnReturnRegiter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mContext = this;
        addControl();
        addEvent();

    }

    private void addEvent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phoneNumber");
            password = extras.getString("password"); // Not recommended to display password
        }
        UserLogin userLogin = new UserLogin(phoneNumber, password);
        sendRegister(userLogin);
    }

    private void addControl() {
        tvStatusRegister = findViewById(R.id.tvStatusRegister);
        btnReturnLogin = findViewById(R.id.btnReturnLogin);
        btnReturnRegiter = findViewById(R.id.btnReturnRegiter);
        btnReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // function to handle register button click event
                Intent i = new Intent(mContext, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        btnReturnRegiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // function to handle register button click event
                Intent i = new Intent(mContext, RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void sendRegister(UserLogin userLogin) {
        UserService user = ServiceBuilder.buildService(UserService.class);
        Call<ResponseDto> request = user.registerUser(userLogin);
        request.enqueue(new Callback<ResponseDto>() {
            @Override
            public void onResponse(Call<ResponseDto> call, Response<ResponseDto> response) {
                if (response.isSuccessful()) {
                    tvStatusRegister.setText("Bạn đã đăng kí thành công, vui lòng đăng nhập!");
                } else if (response.code() == 401) {
                    tvStatusRegister.setText("Bạn đã đăng kí thành công, vui lòng đăng nhập!");
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    tvStatusRegister.setText("Tài khoản đã tồn tại! Vui lòng chọn số điện thoại khác!");
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFailure(Call<ResponseDto> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }
}
