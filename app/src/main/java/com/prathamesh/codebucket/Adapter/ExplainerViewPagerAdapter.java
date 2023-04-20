package com.prathamesh.codebucket.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prathamesh.codebucket.Explainer.ExplainerCodeFragment;
import com.prathamesh.codebucket.Explainer.ExplainerOutputFragment;

public class ExplainerViewPagerAdapter extends FragmentPagerAdapter {
    public ExplainerViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new ExplainerOutputFragment();
        return new ExplainerCodeFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Code";

        return "Explanation";
    }
}
