package com.finalproject.childmonitor.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.finalproject.childmonitor.fragments.DevicesFragment;
import com.finalproject.childmonitor.fragments.HomeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                DevicesFragment devicesFragment = new DevicesFragment();
                return devicesFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
