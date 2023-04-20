package com.prathamesh.codebucket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    String quote = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);

        getQuote();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.putExtra("quote", quote);
                startActivity(intent);
                finish();
            }
        }, Constants.SPLASH_TIME);

    }

    private void getQuote() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.Quotes_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    quote = response.getString("quote").toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        SingletonAPI.getInstance(SplashActivity.this).addToRequestQueue(jsonObjectRequest);
    }
}