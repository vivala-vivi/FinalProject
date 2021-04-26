package com.example.vtradeversion3;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MarketActivity extends AppCompatActivity {

   //private TextView mTextViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.market);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MarketFragment()).commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener((item) -> {

             Fragment selectedFragment= null;
                            switch (item.getItemId()){
                                case R.id.market:
                                    selectedFragment = new MarketFragment();
                                    break;
                                case R.id.watch_list:
                                    selectedFragment = new WatchlistFragment();
                                    break;
                                case R.id.news:
                                    selectedFragment = new NewsFragment();
                                    break;
                                case R.id.account:
                                    selectedFragment = new AccountFragment();
                                    break;
                            }

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            return true;
                    });

    }



}
