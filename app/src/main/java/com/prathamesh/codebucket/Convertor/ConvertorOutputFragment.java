package com.prathamesh.codebucket.Convertor;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.prathamesh.codebucket.R;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public class ConvertorOutputFragment extends Fragment {

    CodeEditor codeEditor;
    String code = "";

    public ConvertorOutputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            code = getArguments().getString("code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_convertor_output, container, false);

        codeEditor = root.findViewById(R.id.CodeEditor_Convertor_Output);
        codeEditor.setEditorLanguage(new JavaLanguage());

        if (!code.equals(""))
            codeEditor.setText(code);
        else
            Log.d("PRATHAMESHADATE", "empty");

        return root;
    }
}