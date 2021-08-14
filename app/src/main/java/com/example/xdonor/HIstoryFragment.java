package com.example.xdonor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class HIstoryFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_istory, container, false);
        viewPager = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tabLayout2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUpViewPager();

    return view;
    }
    private void setUpViewPager() {

        ViewPagerAdapter2 pagerAdapter2 = new ViewPagerAdapter2(getFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter2);
        tabLayout.setupWithViewPager(viewPager);
    }
}