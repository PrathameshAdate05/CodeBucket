package com.prathamesh.codebucket.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prathamesh.codebucket.Compiler.CompilerCodeFragment;
import com.prathamesh.codebucket.Compiler.CompilerOutputFragment;

public class CompilerViewPagerAdapter extends FragmentPagerAdapter {
    public CompilerViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new CompilerOutputFragment();
        return new CompilerCodeFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "Output";
        return "Code";
    }
}
