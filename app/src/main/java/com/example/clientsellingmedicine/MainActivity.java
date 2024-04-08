package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientsellingmedicine.models.Token;
import com.example.clientsellingmedicine.services.LoginService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.example.clientsellingmedicine.utils.Validator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.fragment);

        bottomNavigationView
                = findViewById(R.id.bottom_navigation);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);


    }
    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
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
        } else if (id == R.id.navigation_pills) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, categoryFragment)
                    .commit();
            return true;

        } else if (id == R.id.navigation_prescription) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, orderFragment)
                    .commit();
            return true;

        } else if (id == R.id.navigation_user) {
            Token token = SharedPref.loadToken(this, Constants.TOKEN_PREFS_NAME, Constants.KEY_TOKEN);
            if(token != null && Validator.isTokenValid(token)){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, profileFragment)
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, unLoginProfileFragment)
                        .commit();
            }
            return true;
        }
        return false;
    }

    public void goToHomeFragment() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }





}





