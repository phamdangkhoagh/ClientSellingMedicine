package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity  extends AppCompatActivity {
    private Context mContext;

    TextInputEditText edt_phone_number, edt_password;
    ImageView iv_back;
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
        iv_back = findViewById(R.id.iv_back);
    }
    private void addEvents() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable the button if both EditTexts have values
                btn_login.setEnabled(!edt_phone_number.getText().toString().trim().isEmpty() && !edt_password.getText().toString().trim().isEmpty());
            }
        };
        edt_phone_number.addTextChangedListener(textWatcher);
        edt_password.addTextChangedListener(textWatcher);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin userLogin = new UserLogin(edt_phone_number.getText().toString(), edt_password.getText().toString());
                Login(userLogin);
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void Login(UserLogin userLogin) {
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<Token> request = loginService.login(userLogin);
        request.enqueue(new Callback<Token>() {

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    SharedPref.saveToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN, token);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);

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
}
