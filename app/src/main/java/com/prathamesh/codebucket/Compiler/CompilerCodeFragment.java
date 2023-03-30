package com.prathamesh.codebucket.Compiler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.prathamesh.codebucket.HomeActivity;
import com.prathamesh.codebucket.R;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public class CompilerCodeFragment extends Fragment {

    CodeEditor CodeEditor;
    View FAB;

    public CompilerCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compiler_code, container, false);

        CodeEditor = root.findViewById(R.id.CodeEditor_Compiler);
        CodeEditor.setEditorLanguage(new JavaLanguage());

        CompilerActivity compilerActivity = (CompilerActivity) getActivity();
        FAB = getActivity().findViewById(R.id.FAB_Compiler);

        FAB.setOnClickListener(view -> {
            String code = CodeEditor.getText().toString();

            compilerActivity.showInputDialog(code);
        });

        return root;
    }
}