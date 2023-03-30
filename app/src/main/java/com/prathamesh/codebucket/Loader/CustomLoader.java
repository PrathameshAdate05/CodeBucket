package com.prathamesh.codebucket.Loader;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.prathamesh.codebucket.R;

public class CustomLoader {
    private AlertDialog showCustomLoader;

    public void showCustomLoader(Context context){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_loader,null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        showCustomLoader = alertDialog.create();
        showCustomLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showCustomLoader.show();
    }

    public void dismissCustomLoader(){
        showCustomLoader.dismiss();
    }
}
