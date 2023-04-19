package com.prathamesh.codebucket.Convertor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prathamesh.codebucket.R;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public class ConvertorCodeFragment extends Fragment {

    ExtendedFloatingActionButton FAB;

    public ConvertorCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_convertor_code, container, false);

        CodeEditor editor = root.findViewById(R.id.CodeEditor_Convertor);
        editor.setEditorLanguage(new JavaLanguage());

        ConvertorActivity convertorActivity = (ConvertorActivity) getActivity();

        FAB = getActivity().findViewById(R.id.FAB_Convertor);

        FAB.setOnClickListener(view -> {
            String code = editor.getText().toString();
            convertorActivity.convertCode(code);
        });

        return root;
    }
}