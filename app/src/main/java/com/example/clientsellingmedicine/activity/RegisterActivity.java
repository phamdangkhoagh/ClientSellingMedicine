package com.example.clientsellingmedicine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private Context mContext;
    private FirebaseAuth mAuth;
    private TextInputEditText tvConfirmPassword, tvPassword, tvPhone;
    private CheckBox cbPolicy;
    private Button btnRegister, btn_google_signin_register;

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private String verificationId, smsCode;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.register_screen);
        addControl();
        addEvents();
        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
    }

    private void addControl() {
        cbPolicy = findViewById(R.id.cbPolicy);
        tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvPassword =findViewById(R.id.tvPassword);
        tvPhone = findViewById(R.id.tvPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btn_google_signin_register = findViewById(R.id.btn_google_signin_register);
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
                boolean phoneValid = !tvPhone.getText().toString().trim().isEmpty();
                boolean passwordValid = !tvPassword.getText().toString().trim().isEmpty();
                boolean confirmPasswordValid = !tvConfirmPassword.getText().toString().trim().isEmpty();
                boolean policyAgreed = cbPolicy.isChecked();
                boolean passwordsMatch = passwordValid && tvPassword.getText().toString().trim().equals(tvConfirmPassword.getText().toString().trim());

                if (passwordsMatch) {
                    int borderWidth = 2; // Adjust thickness as needed
                    int color = Color.parseColor("#000000");// Red color (replace with your color)

                    // Increase bottom padding for border, maintain other paddings
                    tvConfirmPassword.setPadding(tvConfirmPassword.getPaddingLeft(),
                            tvConfirmPassword.getPaddingTop(),
                            tvConfirmPassword.getPaddingRight(),
                            tvConfirmPassword.getPaddingBottom() + borderWidth);
                    tvConfirmPassword.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                } else {
                    int borderWidth = 2; // Adjust thickness as needed
                    int color = Color.parseColor("#FF0000"); // Red color (replace with your color)

                    // Increase bottom padding for border, maintain other paddings
                    tvConfirmPassword.setPadding(tvConfirmPassword.getPaddingLeft(),
                            tvConfirmPassword.getPaddingTop(),
                            tvConfirmPassword.getPaddingRight(),
                            tvConfirmPassword.getPaddingBottom() + borderWidth);
                    tvConfirmPassword.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
                btnRegister.setEnabled(phoneValid && passwordValid && confirmPasswordValid && policyAgreed && passwordsMatch);
            }
        };
        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable the button if both EditTexts have values
                boolean phoneValid = !tvPhone.getText().toString().trim().isEmpty();
                boolean passwordValid = !tvPassword.getText().toString().trim().isEmpty();
                boolean confirmPasswordValid = !tvConfirmPassword.getText().toString().trim().isEmpty();
                boolean policyAgreed = cbPolicy.isChecked();
                boolean passwordsMatch = passwordValid && tvPassword.getText().toString().trim().equals(tvConfirmPassword.getText().toString().trim());
            }
        };
        cbPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean phoneValid = !tvPhone.getText().toString().trim().isEmpty();
                boolean passwordValid = !tvPassword.getText().toString().trim().isEmpty();
                boolean confirmPasswordValid = !tvConfirmPassword.getText().toString().trim().isEmpty();
                boolean policyAgreed = cbPolicy.isChecked();
                boolean passwordsMatch = passwordValid && tvPassword.getText().toString().trim().equals(tvConfirmPassword.getText().toString().trim());
                btnRegister.setEnabled(phoneValid && passwordValid && confirmPasswordValid && policyAgreed && passwordsMatch);
            }
        });
        tvConfirmPassword.addTextChangedListener(textWatcher);
        tvPassword.addTextChangedListener(textWatcher);
        tvPhone.addTextChangedListener(textWatcher1);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking whether the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(tvPhone.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(RegisterActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone = "+84" + tvPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        btn_google_signin_register.setOnClickListener(v -> oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(RegisterActivity.this, result -> {

                    IntentSenderRequest intentSenderRequest =
                            new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                    activityResultLauncher.launch(intentSenderRequest);
                })
                .addOnFailureListener(RegisterActivity.this, e -> {
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    Log.d("TAG", e.getLocalizedMessage());
                }));
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            System.out.println("Vào hàm này rồi!");
                            System.out.println("Otp code is " + smsCode);
                            Bundle extras = new Bundle();
                            extras.putString("phoneNumber", tvPhone.getText().toString());
                            extras.putString("password", tvPassword.getText().toString());
                            extras.putString("smsCode", smsCode);
                            extras.putInt("countDown", 60);
                            Intent intent = new Intent(mContext, OtpConfirmActivity.class);
                            intent.putExtras(extras);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setCallbacks(mCallBack)
                        .setActivity(this)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                smsCode = code;

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
}
