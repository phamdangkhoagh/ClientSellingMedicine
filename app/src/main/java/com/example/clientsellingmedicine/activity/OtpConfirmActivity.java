package com.example.clientsellingmedicine.activity;
import android.view.KeyEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.R;

public class OtpConfirmActivity extends AppCompatActivity {
    private Context mContext;
    private TextView tvCountDown, tvOtpPhone;
    private EditText edt1, edt2, edt3, edt4, edt5, edt6;
    private EditText[] editTexts = {edt1, edt2, edt3, edt4, edt5, edt6};
    private Integer countDownTime = 90;
    private String smsVerify,phoneNumber, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirm_screen);
        mContext = this;

        addControls();
        addEvents();
    }
    private void addControls() {
        tvCountDown = findViewById(R.id.tvCountDown);
        tvOtpPhone = findViewById(R.id.tvOtpPhone);
        edt1 = findViewById(R.id.edtConfimNumber1);
        edt2 = findViewById(R.id.edtConfimNumber2);
        edt3 = findViewById(R.id.edtConfimNumber3);
        edt4 = findViewById(R.id.edtConfimNumber4);
        edt5 = findViewById(R.id.edtConfimNumber5);
        edt6 = findViewById(R.id.edtConfimNumber6);
        editTexts[0] = edt1;
        editTexts[1] = edt2;
        editTexts[2] = edt3;
        editTexts[3] = edt4;
        editTexts[4] = edt5;
        editTexts[5] = edt6;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phoneNumber");
            password = extras.getString("password"); // Not recommended to display password
            smsVerify = extras.getString("smsCode");

            // Use the retrieved data (e.g., display on TextViews or logic for OTP verification)
            if (phoneNumber != null && phoneNumber.length() >= 4) {
                String lastFourDigits = phoneNumber.substring(phoneNumber.length() - 4);
                // Replace the remaining digits with "xxxx"
                String maskedPhoneNumber = lastFourDigits + "xxxx";
                // Set the masked phone number on the TextView
                tvOtpPhone.setText("0"+maskedPhoneNumber);
            }
        }
    }
    private void addEvents() {
        startCountDownTimer(90);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredOtp =
                          edt1.getText().toString() + edt2.getText().toString()
                        + edt3.getText().toString() + edt4.getText().toString()
                        + edt5.getText().toString() + edt6.getText().toString();

                // Check if entered OTP matches received smsCode
                if (enteredOtp.length() == 6 && enteredOtp.equals(smsVerify)) {
                    // OTP verified! (Perform necessary actions based on successful verification)
                    Toast.makeText(mContext, "OTP verified!", Toast.LENGTH_SHORT).show();
                    Bundle extras = new Bundle();
                    extras.putString("phoneNumber", phoneNumber.toString());
                    extras.putString("password", password.toString());
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.putExtras(extras);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        };

        for (int i = 0; i < editTexts.length; i++) {
            final EditText editText = editTexts[i];
            editText.addTextChangedListener(textWatcher);
            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        // Handle backspace
                        int index = getEditTextIndex(editText);
                        if (index > 0 && editText.getText().length() == 0) {
                            // If on an EditText other than the first one and backspace is pressed with empty text
                            // Move focus to the previous EditText and delete its content
                            EditText previousEditText = editTexts[index - 1];
                            previousEditText.setText("");
                            previousEditText.requestFocus();
                            return true;
                        }
                    } else if (keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        // Handle digit input
                        int index = getEditTextIndex(editText);
                        if (editText.getText().length() >= 1) {
                            // If current EditText has a digit, move focus to the next one
                            if (index < editTexts.length - 1) {
                                editTexts[index + 1].requestFocus();
                            } else {
                                // If on the last EditText, hide the keyboard
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            }
                        }
                    }
                    return false;
                }
            });
        }

    }
    private int getEditTextIndex(EditText editText) {
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i] == editText) {
                return i;
            }
        }
        return -1; // Not found
    }



    private void startCountDownTimer(int initialTime) {
        new CountDownTimer(initialTime * 1000L, 1000L) { // Convert seconds to milliseconds

            @Override
            public void onTick(long millisUntilFinished) {
                long remainingSeconds = millisUntilFinished / 1000;
                long minutes = remainingSeconds / 60;
                long seconds = remainingSeconds % 60;

                // Format the time as "mm:ss"
                String formattedTime = String.format("%02d:%02d", minutes, seconds);
                tvCountDown.setText(formattedTime);
            }

            @Override
            public void onFinish() {
                tvCountDown.setText("00:00");
                finish(); // Finish activity when countdown reaches 0
            }
        }.start();
    }
}
