package com.example.clientsellingmedicine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
import com.example.clientsellingmedicine.models.GoogleToken;
=======
import com.example.clientsellingmedicine.models.Device;
import com.example.clientsellingmedicine.models.Notification;
>>>>>>> Stashed changes
import com.example.clientsellingmedicine.models.UserLogin;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.services.DeviceService;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.NotificationService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity  extends AppCompatActivity {
    private Context mContext;

    TextInputEditText edt_phone_number, edt_password;
    ImageView iv_back;
<<<<<<< Updated upstream
    Button btn_login,btn_google_signin;
=======
    Button btn_login;
    TextView tvRegister;

>>>>>>> Stashed changes

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.login_screen);

        addControl();
        addEvents();
<<<<<<< Updated upstream

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

         activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
             try {
                 SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                 String idToken = credential.getGoogleIdToken();
                 if (idToken !=  null) {
                     LoginWithGoogle(new GoogleToken(idToken));
                 }else {
                     MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
                     builder.setIcon(R.drawable.ic_warning) // Đặt icon của Dialog
                             .setTitle("Đăng nhập thất bại")
                             .setMessage("Vui lòng kiểm tra lại tài khoản Google đã đăng nhập !")
//                            .setCancelable(false) // Bấm ra ngoài không mất dialog
                             .setPositiveButton("OK", (dialog, which) -> {
                                 // Xử lý khi nhấn nút OK
                             })
                             .show();
                 }
             } catch (ApiException e) {
                 e.printStackTrace();
             }
         });

=======
>>>>>>> Stashed changes
    }


    private void addControl() {
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
<<<<<<< Updated upstream
        btn_google_signin = findViewById(R.id.btn_google_signin);
=======
        tvRegister = findViewById(R.id.tvRegister);
>>>>>>> Stashed changes
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
        btn_login.setOnClickListener(view -> {
            UserLogin userLogin = new UserLogin(edt_phone_number.getText().toString(), edt_password.getText().toString());
            //loginServiceImpl.login(userLogin);
            Login(userLogin);
        });
        tvRegister.setOnClickListener(view -> {
            Intent i = new Intent(mContext, OtpActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            System.out.println("Hello");
        });
        iv_back.setOnClickListener(view -> finish());

        btn_google_signin.setOnClickListener(v -> oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(LoginActivity.this, result -> {

                    IntentSenderRequest intentSenderRequest =
                            new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                    activityResultLauncher.launch(intentSenderRequest);

                })
                .addOnFailureListener(LoginActivity.this, e -> {
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    Log.d("TAG", e.getLocalizedMessage());
                }));

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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
                    builder.setIcon(R.drawable.ic_warning) // Đặt icon của Dialog
                            .setTitle("Sai thông tin đăng nhập")
                            .setMessage("Vui lòng kiểm tra lại thông tin và đăng nhập lại !")
//                            .setCancelable(false) // Bấm ra ngoài không mất dialog
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Xử lý khi nhấn nút OK

                                }
                            })
                            .show();
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

    private void LoginWithGoogle(GoogleToken googleToken){
        LoginService loginService = ServiceBuilder.buildService(LoginService.class);
        Call<Token> request = loginService.loginWithGoogle(googleToken);
        request.enqueue(new Callback<Token>() {

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    SharedPref.saveToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN, token);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
               else {
                    Toast.makeText(mContext, "Đăng nhập thất bại, đã có lỗi xảy ra !", Toast.LENGTH_LONG).show();
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
