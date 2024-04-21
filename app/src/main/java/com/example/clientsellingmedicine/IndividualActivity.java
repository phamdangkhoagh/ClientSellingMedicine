package com.example.clientsellingmedicine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.services.UserService;
import com.example.clientsellingmedicine.utils.Convert;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualActivity extends AppCompatActivity {
    private Context mContext;
    private TextView tvName,tvPhone,tvGender,tvBirthday,tvUserID;
    private Button btnEditProfile;
    private ImageView ivAvatar;
    private static User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.individual_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvGender = findViewById(R.id.tvGender);
        tvBirthday = findViewById(R.id.tvBirthday);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvUserID = findViewById(R.id.tvUserID);
        btnEditProfile = findViewById(R.id.btnEditProfile);
    }

    private void addEvents() {
        // Chuyển sang màn hình chỉnh sửa thông tin cá nhân
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
        // Lấy thông tin cá nhân
        getUserLogin();
    }
    public void getUserLogin() {
        UserService userService = ServiceBuilder.buildService(UserService.class);
        Call<User> request = userService.getUser();
        request.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if(user != null){
                        tvUserID.setText("user#"+user.getId());
                        tvName.setText(user.getFirstName() != null ? user.getFirstName()+" "+user.getLastName() : "Unknown");
                        tvPhone.setText(user.getPhone());
                        tvGender.setText(user.getGender() == 1 ? "Nam" : "Nữ");
                        String birthday = Convert.convertToDate(user.getBirthday().toString());
                        tvBirthday.setText(birthday != null ? birthday : "Unknown");

                        Glide.with(mContext)
                                .load(user.getImage())
                                .placeholder(R.drawable.ic_avartar) // Hình ảnh thay thế khi đang tải
                                .error(R.drawable.ic_avartar) // Hình ảnh thay thế khi có lỗi
                                .circleCrop()
                                .into(ivAvatar);
                    }
                } else if (response.code() == 401) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }
}
