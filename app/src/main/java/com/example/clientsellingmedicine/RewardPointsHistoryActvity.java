package com.example.clientsellingmedicine;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.clientsellingmedicine.Adapter.rewardPointsHistoryAdapter;
import com.google.android.material.tabs.TabLayout;

public class RewardPointsHistoryActvity extends AppCompatActivity {
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.accumulate_points_history_screen);

        addControl();
        addEvents();

    }

    private void addControl() {
        mTabLayout = findViewById(R.id.tabRewardPointsHistory);
        mViewPaper = findViewById(R.id.viewPayPer_RewardPointsHistory);

        rewardPointsHistoryAdapter historyAdapter = new rewardPointsHistoryAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPaper.setAdapter(historyAdapter);
        mTabLayout.setupWithViewPager(mViewPaper);
    }
    private void addEvents() {}
}
