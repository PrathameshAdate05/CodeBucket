package com.prathamesh.codebucket.Compiler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prathamesh.codebucket.R;

public class CompilerOutputFragment extends Fragment {

    TextView TV_Output;

    public CompilerOutputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compiler_output, container, false);

        TV_Output = root.findViewById(R.id.TV_Compiler_Output);
        TV_Output.setVerticalScrollBarEnabled(true);
        TV_Output.setMovementMethod(new ScrollingMovementMethod());

        return root;
    }


}