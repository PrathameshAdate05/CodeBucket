package com.prathamesh.codebucket.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prathamesh.codebucket.Convertor.ConvertorCodeFragment;
import com.prathamesh.codebucket.Convertor.ConvertorOutputFragment;

public class ConvertorViewPagerAdapter extends FragmentPagerAdapter {
    public ConvertorViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new ConvertorOutputFragment();
        return new ConvertorCodeFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "Converted Code";
        return "Code";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
