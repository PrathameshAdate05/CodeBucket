package com.prathamesh.codebucket.Convertor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prathamesh.codebucket.R;

import io.github.rosemoe.sora.widget.CodeEditor;

public class ConvertorOutputFragment extends Fragment {

    FloatingActionButton FAB_copy;
    CodeEditor codeEditor;
    public ConvertorOutputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_convertor_output, container, false);

        FAB_copy = root.findViewById(R.id.FAB_Convertor_Copy);
        FAB_copy.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Copy Clicked", Toast.LENGTH_SHORT).show();
        });

        codeEditor = root.findViewById(R.id.CodeEditor_Convertor_Output);
        Bundle bundle = getArguments();

        if (bundle != null){
            codeEditor.setText(bundle.getString("code"));
        }else
            Log.d("PRAtHAMESHADATE","empty");
        return root;
    }
}