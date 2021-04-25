package com.example.vtradeversion3;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class SendString extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public String message;
    public boolean isSet;
    public List<TableRows> data = new ArrayList<>();
    //public boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_strings);

        Intent intent = getIntent();
        message = intent.getStringExtra("data");
        //  isFav = intent.getBooleanExtra("favorite", false);
        //       Intent intent = getIntent(); 
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); 
//        Log.d("pooja",message); 


     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(mViewPager);

    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1Charts tab1 = new Tab1Charts();
                    return tab1;
                //    case 1:
                //       Tab2History tab2 = new Tab2History();
                //       return tab2;
                //    case 2:
                //        Tab3News tab3 = new Tab3News();
                //       return tab3;
                default:
                   return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

    }
}

