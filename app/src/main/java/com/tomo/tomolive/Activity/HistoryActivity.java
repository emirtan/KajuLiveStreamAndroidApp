package com.tomo.tomolive.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.tomo.tomolive.Adaptor.ViewPagerAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        MainActivity.IS_HOST_LIVE = false;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tablayout1.setupWithViewPager(binding.viewPager);

        settabLayout();

    }


    private void settabLayout() {
        binding.tablayout1.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tablayout1.removeAllTabs();
        binding.tablayout1.addTab(binding.tablayout1.newTab().setCustomView(createCustomView("Recharge")));
        binding.tablayout1.addTab(binding.tablayout1.newTab().setCustomView(createCustomView("Credit")));
        binding.tablayout1.addTab(binding.tablayout1.newTab().setCustomView(createCustomView("Debit")));

    }

    private View createCustomView(String name) {


        View v = LayoutInflater.from(this).inflate(R.layout.custom_tabhorizontol, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(name);


        if (name.equals("Recharge")) {

        }
        return v;

    }

    public void onClickBack(View view) {
        finish();
    }
}