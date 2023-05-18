package com.prathamesh.codebucket.Loader;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.prathamesh.codebucket.R;

public class CustomSuccessAnimation {
    private AlertDialog customAnimation;

    public void showAnimation(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_success_animation,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        customAnimation = alertDialogBuilder.create();
        customAnimation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customAnimation.show();
    }

    public void dismissAnimation(){
        customAnimation.dismiss();
    }
}
