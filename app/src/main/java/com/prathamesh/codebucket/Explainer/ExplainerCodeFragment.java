package com.prathamesh.codebucket.Explainer;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prathamesh.codebucket.R;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public class ExplainerCodeFragment extends Fragment {

    CodeEditor codeEditor;
    public ExplainerCodeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_explainer_code, container, false);

        codeEditor = root.findViewById(R.id.CodeEditor_Explainer);
        codeEditor.setEditorLanguage(new JavaLanguage());
        ExplainerActivity explainerActivity = (ExplainerActivity) getActivity();

        ExtendedFloatingActionButton FAB_Explain = getActivity().findViewById(R.id.FAB_Explain);
        FAB_Explain.setOnClickListener(view -> {
            String code = codeEditor.getText().toString();
            explainerActivity.explain(code);
        });

        return root;
    }
}