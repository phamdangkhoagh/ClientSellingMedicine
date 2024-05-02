package com.example.clientsellingmedicine.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private ActivityResultLauncher<Intent> launcher;
    private Context mContext;

    public static String tokenDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.fragment);

        bottomNavigationView
                = findViewById(R.id.bottom_navigation);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // This is only necessary for API level >= 33 (TIRAMISU)
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        tokenDevice = token;
                        System.out.println("Token is: " + token);

                        // Log and toast
                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("noti")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    HomeFragment homeFragment = new HomeFragment();
//    CategoryFragment categoryFragment = new CategoryFragment();
    ExchangeFragment exchangeFragment = new ExchangeFragment();
    OrderFragment orderFragment = new OrderFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    UnLoginProfileFragment unLoginProfileFragment = new UnLoginProfileFragment();


    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commit();
            return true;
        } else if (id == R.id.navigation_medal) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, exchangeFragment)
                    .commit();
            return true;

        } else if (id == R.id.navigation_prescription) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, orderFragment)
                    .commit();
            return true;

        } else if (id == R.id.navigation_user) {
            Token token = SharedPref.loadToken(mContext, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
            if (token == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, unLoginProfileFragment)
                        .commit();
                return true;
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, profileFragment)
                        .commit();
                return true;
            }
        }

        return false;
    }

    public void goToHomeFragment() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        homeFragment.loadData();
//    }
}





