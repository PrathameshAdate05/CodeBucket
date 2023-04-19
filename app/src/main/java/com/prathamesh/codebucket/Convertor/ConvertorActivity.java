package com.prathamesh.codebucket.Convertor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.prathamesh.codebucket.Adapter.ConvertorViewPagerAdapter;
import com.prathamesh.codebucket.Adapter.CustomSpinnerAdapter;
import com.prathamesh.codebucket.Constants;
import com.prathamesh.codebucket.Loader.CustomLoader;
import com.prathamesh.codebucket.R;
import com.prathamesh.codebucket.SingletonAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.rosemoe.sora.widget.CodeEditor;

public class ConvertorActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ExtendedFloatingActionButton FAB;
    Spinner spinner;
    CustomLoader customLoader;
    String languageKey = "";

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertor);

        relativeLayout = findViewById(R.id.RL_Convertor);
        tabLayout = findViewById(R.id.TBL_Convertor);
        viewPager = findViewById(R.id.ViewPager_Convertor);
        FAB = findViewById(R.id.FAB_Convertor);
        spinner = findViewById(R.id.Convertor_Spinner);
        customLoader = new CustomLoader();

        // tablayout setup
        ConvertorViewPagerAdapter convertorViewPagerAdapter = new ConvertorViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(convertorViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // spinner setup
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), Constants.ICONS, Constants.LANGUAGES);
        spinner.setAdapter(customSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                languageKey = Constants.LANGUAGES[i];
                Toast.makeText(ConvertorActivity.this, languageKey, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // fab animation
        fabAnimation();

        // viewpager setup
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    FAB.setVisibility(View.VISIBLE);
                    fabAnimation();
                }
                if (position == 1) {
                    tabLayout.getTabAt(1).removeBadge();
                    FAB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void fabAnimation() {
        FAB.extend();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FAB.shrink();
            }
        }, 3000);
    }

    // this method is used to set the response from the convertor to the desired screen
    private void setCodeToOutputScreen(String code) {
        ConvertorOutputFragment convertorOutputFragment = new ConvertorOutputFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Convertor_Output, convertorOutputFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("code",code);
        convertorOutputFragment.setArguments(bundle);
//        CodeEditor codeEditor = findViewById(R.id.CodeEditor_Convertor_Output);
//        codeEditor.setText(code);
        Log.d("PRATHAMESHADATE","code setted");
    }

    public void convertCode(String code) {
        if (code.isEmpty()) {
            showSnackBar("Please write some code...!");
        } else {
            customLoader.showCustomLoader(this, "Converting...");
            JSONObject payload = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("###");

            String lang = "";


            switch (languageKey) {
                case "C":
                    lang = "c";
                    break;
                case "C++":
                    lang = "cpp";
                    break;
                case "Java":
                    lang = "java";
                    break;
                default:
                    lang = "python";
                    break;
            }

            String prompt = "##### Translate the below code into " + lang + "###\n" + code + "\n###";

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

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.Convertor_API, payload, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArrayResponse = response.getJSONArray("choices");
                        JSONObject res = jsonArrayResponse.getJSONObject(0);
                        String code = res.getString("text");
                        Log.d("PRATHAMESHADATE", code);
                        setCodeToOutputScreen(code);
                        tabLayout.getTabAt(1).getOrCreateBadge().setNumber(1);
                        customLoader.dismissCustomLoader();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customLoader.dismissCustomLoader();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    String key = "Bearer " + Constants.API_KEY;
                    headers.put("Authorization", key);
                    return headers;
                }
            };

//            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 50000;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 50000;
//                }
//
//                @Override
//                public void retry(VolleyError error) throws VolleyError {
//
//                }
//            });

            SingletonAPI.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    // used to show ot snackbar
    private void showSnackBar(String message) {
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG).show();
    }
}