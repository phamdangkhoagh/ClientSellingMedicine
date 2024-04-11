package com.example.clientsellingmedicine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.clientsellingmedicine.models.ResponseDto;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.models.User;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.LogoutService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.services.UserService;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private Context mContext;
    private ImageView iv_Avatar;
    private TextView tv_UserName,tv_Rank,tv_Logout;

    private ProgressBar progress_Point;

    private LinearLayout ll_AddressBook;


    private static User user = new User();
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_login_screen, container, false);
        mContext = view.getContext();
        // Thực hiện các thao tác cần thiết trên giao diện view của fragment
        addControl(view);
        addEvents();
        return view;


    }



    private void addControl(View view){
        iv_Avatar = view.findViewById(R.id.iv_Avatar);
        tv_UserName = view.findViewById(R.id.tv_UserName);
        tv_Rank = view.findViewById(R.id.tv_Rank);
        progress_Point = view.findViewById(R.id.progress_Point);
        tv_Logout = view.findViewById(R.id.tv_Logout);

        ll_AddressBook = view.findViewById(R.id.ll_AddressBook);
    }
    private void addEvents(){
        // this layout is used to show the list of registered addresses
        ll_AddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisteredAddressActivity.class);
                startActivity(intent);
            }
        });

        // logout button
        tv_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setIcon(R.drawable.drug) // Đặt icon của Dialog
                        .setTitle("Xác Nhận Đăng Xuất")
                        .setMessage("Bạn có muốn đăng xuất khỏi ứng dụng không?")
                        .setCancelable(false) // Bấm ra ngoài không mất dialog

                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi nhấn nút OK
                                Logout();
                            }
                        })

                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi nhấn nút Cancel
                            }
                        })
                        .show();
            }
        });

        // login with token
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
//                    user = new User();
//                    user.setId(response.body().getId());
//                    user.setIdRole(response.body().getIdRole());
//                    user.setPhone(response.body().getPhone());
//                    user.setPhone(response.body().getPhone());
//                    user.setPassword(response.body().getPassword());
//                    user.setFirstName(response.body().getFirstName());
//                    user.setLastName(response.body().getLastName());
//                    user.setRank(response.body().getRank());
//                    user.setPoint(response.body().getPoint());
//                    user.setBirthday(Convert.convertToDate(response.body().getBirthday().toString().trim()));
//                    user.setGender(response.body().getGender());
//                    user.setImage(response.body().getImage());
//                    user.setStatus(response.body().getStatus());
                    if(user != null){
                        tv_UserName.setText(user.getFirstName() + " " + user.getLastName());
                        progress_Point.setProgress(user.getPoint());
                        tv_Rank.setText(user.getRank());
                        Glide.with(mContext)
                                .load(user.getImage())
                                .placeholder(R.drawable.ic_profile_user) // Hình ảnh thay thế khi đang tải
                                .error(R.drawable.ic_profile_user) // Hình ảnh thay thế khi có lỗi
                                .circleCrop()
                                .into(iv_Avatar);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
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

    public void RefreshToken(){
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Token token = SharedPref.loadToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
        Call<Token> requestRefreshToken = loginService.refreshToken(token);
        requestRefreshToken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    SharedPref.saveToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN, token);
                    Log.d("tag", "onResponse: "+response.body());
                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Logout() {
        LogoutService logoutService = ServiceBuilder.buildService(LogoutService.class);
        Call<ResponseDto> request = logoutService.logout();
        request.enqueue(new Callback<ResponseDto>() {

            @Override
            public void onResponse(Call<ResponseDto> call, Response<ResponseDto> response) {
                if (response.isSuccessful()) {
                    // remove token
                    SharedPref.removeData(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
                    // return to login screen and finish all activity
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDto> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onFailure: "+ t.getMessage());
                } else
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    public User convertUser(Response<User> response){
        User u = new User();
        u.setId(response.body().getId());
        u.setIdRole(response.body().getIdRole());
        u.setPhone(response.body().getPhone());
        u.setPhone(response.body().getPhone());
        u.setPassword(response.body().getPassword());
        u.setFirstName(response.body().getFirstName());
        u.setLastName(response.body().getLastName());
        u.setRank(response.body().getRank());
        u.setPoint(response.body().getPoint());
        u.setBirthday(Convert.convertToDate(response.body().getBirthday().toString().trim()));
        u.setGender(response.body().getGender());
        u.setImage(response.body().getImage());
        u.setStatus(response.body().getStatus());
        return u;
    }

}
