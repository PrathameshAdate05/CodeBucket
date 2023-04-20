package com.prathamesh.codebucket.Explainer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prathamesh.codebucket.R;

public class ExplainerOutputFragment extends Fragment {


    String explanation = "";
    TextView TV_Output;

    public ExplainerOutputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            explanation = getArguments().getString("explanation");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_explainer_output, container, false);

        TV_Output = root.findViewById(R.id.TV_Explainer_Output);
        TV_Output.setText(explanation);
        return root;
    }
}
