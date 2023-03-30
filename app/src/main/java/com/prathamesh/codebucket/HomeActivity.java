package com.prathamesh.codebucket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    TextView TV_Quote;
    MaterialCardView Card_Compiler, Card_Code_Convertor, Card_Explain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Card_Compiler = findViewById(R.id.CARD_Compiler);
        Card_Code_Convertor = findViewById(R.id.CARD_Transpile);
        Card_Explain = findViewById(R.id.CARD_Explain);

        String s = getIntent().getStringExtra("quote");

        TV_Quote = findViewById(R.id.TV_Quote);
        if (s.isEmpty())
            TV_Quote.setText("If it works don't touch it.");
        else
            TV_Quote.setText(s);
    }
}