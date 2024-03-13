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
    HomeFragment firstFragment = new HomeFragment();
    CategoryFragment secondFragment = new CategoryFragment();


    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.navigation_home){
            getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, firstFragment)
                        .commit();
                return true;
        }
        else if(id == R.id.navigation_pills) {
            getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, secondFragment)
                        .commit();
                return true;

                    }
        return false;
    }
}

