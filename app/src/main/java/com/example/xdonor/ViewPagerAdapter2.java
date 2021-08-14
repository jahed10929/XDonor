package com.example.xdonor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter2 extends FragmentPagerAdapter {

    int tabCount;
    String[] tabTitles = {"Donations", "Service Taken"};

    public ViewPagerAdapter2(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return new DonationsFragment();
        else
            return new ServiceTakenFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
