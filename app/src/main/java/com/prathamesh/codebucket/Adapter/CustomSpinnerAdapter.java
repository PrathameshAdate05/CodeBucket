package com.prathamesh.codebucket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prathamesh.codebucket.R;

public class CustomSpinnerAdapter extends BaseAdapter {

    Context context;
    int[] icons;
    String[] languages;
    LayoutInflater layoutInflater;


    public CustomSpinnerAdapter(Context applicationContext, int[] icons, String[] languages) {
        this.context = applicationContext;
        this.icons = icons;
        this.languages = languages;
        layoutInflater = LayoutInflater.from(applicationContext);
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.custom_spinner_layout,null);
        ImageView imageView = view.findViewById(R.id.IVSpinner);
        TextView textView = view.findViewById(R.id.TVSpinner);
        imageView.setImageResource(icons[i]);
        textView.setText(languages[i]);
        return view;
    }
}
