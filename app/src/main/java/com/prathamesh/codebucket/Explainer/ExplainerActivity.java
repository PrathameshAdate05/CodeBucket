package com.prathamesh.codebucket.Explainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.prathamesh.codebucket.Adapter.ExplainerViewPagerAdapter;
import com.prathamesh.codebucket.Constants;
import com.prathamesh.codebucket.Loader.CustomLoader;
import com.prathamesh.codebucket.R;
import com.prathamesh.codebucket.SingletonAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExplainerActivity extends AppCompatActivity {

    Animation fabOpen, fabClose;
    FloatingActionButton FAB_Play, FAB_Stop;
    ExtendedFloatingActionButton FAB_Explain, FAB_Listen;

    boolean isFabListenOpen = false;

    ViewPager viewPager;
    TabLayout tabLayout;
    CustomLoader customLoader;
    RelativeLayout relativeLayout;
    TextToSpeech textToSpeech;

    String explanation = "";

    String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explainer);

        viewPager = findViewById(R.id.ViewPager_Explainer);
        tabLayout = findViewById(R.id.TBL_Explainer);
        customLoader = new CustomLoader();
        relativeLayout = findViewById(R.id.RL_Explainer);

        FAB_Explain = findViewById(R.id.FAB_Explain);
        FAB_Listen = findViewById(R.id.FAB_Listen);
        FAB_Play = findViewById(R.id.FAB_Explain_Play);
        FAB_Stop = findViewById(R.id.FAB_Explain_Stop);

        FAB_Listen.setVisibility(View.GONE);
        FAB_Listen.setIconResource(R.drawable.icon_speaker);

        // tablayout setup
        ExplainerViewPagerAdapter explainerViewPagerAdapter = new ExplainerViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(explainerViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fabAnimationExplain();

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Object temp = applicationInfo.metaData.get("API_KEY");
            API_KEY = temp.toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        // viewpager setup
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    FAB_Explain.setVisibility(View.GONE);
                    FAB_Listen.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(1).removeBadge();
                    fabAnimationListen();
                } else {
                    FAB_Listen.setVisibility(View.GONE);
                    FAB_Explain.setVisibility(View.VISIBLE);
                    fabAnimationExplain();
                    if (isFabListenOpen) {
                        FAB_Play.startAnimation(fabClose);
                        FAB_Stop.startAnimation(fabClose);
                        FAB_Play.setClickable(false);
                        FAB_Stop.setClickable(false);
                        FAB_Listen.setIcon(getDrawable(R.drawable.icon_speaker));
                        isFabListenOpen = false;
                        stopReading();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // animation setup
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);


        FAB_Listen.setOnClickListener(view -> {
            animateFab();
        });

        FAB_Play.setOnClickListener(view -> {

            if (explanation.isEmpty())
                Toast.makeText(this, "Nothing to read...", Toast.LENGTH_SHORT).show();
            else {
                textToSpeech = new TextToSpeech(ExplainerActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.ENGLISH);
                            textToSpeech.speak(explanation, TextToSpeech.QUEUE_FLUSH, null, null);

                            Toast.makeText(getApplicationContext(), "Reading...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        FAB_Stop.setOnClickListener(view -> {
            Toast.makeText(this, "Reading Stopped...", Toast.LENGTH_SHORT).show();
            stopReading();
        });
    }

    private void animateFab() {
        if (isFabListenOpen) {
            FAB_Play.startAnimation(fabClose);
            FAB_Stop.startAnimation(fabClose);
            FAB_Play.setClickable(false);
            FAB_Stop.setClickable(false);
            FAB_Listen.setIcon(getDrawable(R.drawable.icon_speaker));
            isFabListenOpen = false;
        } else {

            FAB_Play.startAnimation(fabOpen);
            FAB_Stop.startAnimation(fabOpen);
            FAB_Play.setClickable(true);
            FAB_Stop.setClickable(true);
            FAB_Listen.setIcon(getDrawable(R.drawable.icon_close));
            isFabListenOpen = true;
        }
    }

    public void explain(String code) {
        if (code.isEmpty())
            showSnackBar("Please write some code...!");
        else {
            customLoader.showCustomLoader(this, "Generating...");
            String prompt = "##### explain this code step by step in detail ###\n" + code + "\n###";

            JSONObject payload = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("###");

            try {
                payload.put("model", "text-davinci-003");
                payload.put("temperature", 0);
                payload.put("max_tokens", 1000);
                payload.put("top_p", 1);
                payload.put("frequency_penalty", 0.0);
                payload.put("presence_penalty", 0.0);
                payload.put("prompt", prompt);
                payload.put("stop", jsonArray.toString());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.OpenAI_API, payload, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArrayResponse = null;
                    try {
                        jsonArrayResponse = response.getJSONArray("choices");
                        JSONObject res = jsonArrayResponse.getJSONObject(0);
                        explanation = res.getString("text");
                        setExplanationToOutputScreen(explanation);
                        tabLayout.getTabAt(1).getOrCreateBadge().setNumber(1);
                        customLoader.dismissCustomLoader();
                        Log.d("PRATHAMESHADATE", explanation);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customLoader.dismissCustomLoader();
                    Toast.makeText(ExplainerActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    Log.d("PRATHAMESHADATE", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", API_KEY);
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 10000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 10000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            SingletonAPI.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void fabAnimationExplain() {
        FAB_Explain.extend();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FAB_Explain.shrink();
            }
        }, 3000);
    }

    private void fabAnimationListen() {
        FAB_Listen.extend();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FAB_Listen.shrink();
            }
        }, 3000);
    }

    private void showSnackBar(String message) {
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setExplanationToOutputScreen(String explanation) {
        ExplainerOutputFragment explainerOutputFragment = new ExplainerOutputFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Explainer_Output, explainerOutputFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("explanation", explanation);
        explainerOutputFragment.setArguments(bundle);
        Log.d("PRATHAMESHADATE", "code setted");
    }

    @Override
    protected void onPause() {
        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onPause();
    }

    // it stop the text to speech function
    private void stopReading() {
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}