package com.prathamesh.codebucket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView TV_Quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String s = getIntent().getStringExtra("quote");

        TV_Quote = findViewById(R.id.TV_Quote);
        TV_Quote.setText(s);
    }
}