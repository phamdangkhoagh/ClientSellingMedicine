package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.models.token;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity  extends AppCompatActivity {
    private Context mContext;

    TextInputEditText edt_phone_number, edt_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.login_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
    }
    private void addEvents() {
        edt_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isLogin()) {
                    btn_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isLogin()) {
                    btn_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin userLogin = new UserLogin(edt_phone_number.getText().toString(), edt_password.getText().toString());
                Login(userLogin);
            }
        });

    }

    public void Login(UserLogin userLogin) {
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<token> request = loginService.login(userLogin);
        request.enqueue(new Callback<token>() {

            @Override
            public void onResponse(Call<token> call, Response<token> response) {
                if (response.isSuccessful()) {
                    SharedPref.saveToken(mContext, "token", "token", response.body().getToken());
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);

                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items (response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<token> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isLogin() {
        if(edt_phone_number.getText().equals("")  && edt_password.getText().equals("")) {
            return true;
        }
        return false;
    }
}
