package com.example.clientsellingmedicine;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

//        } else if (id == R.id.navigation_user) {
//            if(true){
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, profileFragment)
//                        .commit();
//            }
//            else {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, unLoginProfileFragment)
//                        .commit();
//            }
//            return true;
        }
        return false;
    }
}

