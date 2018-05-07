package com.example.android.firebaseupload.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.firebaseupload.R;
import com.example.android.firebaseupload.fragments.HistoryFragment;
import com.example.android.firebaseupload.fragments.InvoiceFragment;
import com.example.android.firebaseupload.fragments.RequestFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setElevation(0);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = {getString(R.string.CallRegister), getString(R.string.title_history), getString(R.string.Requests)};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);


        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position) {
                case 0:  fragment = InvoiceFragment.newInstance(position);

                    break;
                case 1:
                    fragment = HistoryFragment.newInstance(position);
                    break;
                case 2:
                    fragment= RequestFragment.newInstance(position);
                    break;
            }return fragment;

        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
