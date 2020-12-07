package com.example.myapplication.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.fragment.ContactFragment;
import com.example.myapplication.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public int idcurrentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //changeStatusBarColor("#1651E4");
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        settupViewpager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        idcurrentuser=this.getIntent().getExtras().getInt("idCurrentUser");
    }
    public void settupViewpager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }
    /*private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }*/
    /*class ViewPagerAdapter extends FragmentPagerAdapter {
        public List<Fragment> fragments = new ArrayList<>();
        public List<String> titles= new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            if(fragments.isEmpty()||fragments==null){
                return 0;
            }
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }*/
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public List<Fragment> fragments = new ArrayList<>();
        public List<String> titles= new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            titles.add("Tin nhắn");
            titles.add("Cá nhân");
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0: return new ContactFragment();
                case 1: return new PersonalFragment();
            }
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


    }
}