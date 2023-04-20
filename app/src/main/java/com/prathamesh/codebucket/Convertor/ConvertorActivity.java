package com.prathamesh.codebucket.Convertor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ConvertorActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ExtendedFloatingActionButton FAB_Convert;
    FloatingActionButton FAB_Copy;
    Spinner spinner;
    CustomLoader customLoader;
    String languageKey = "";
    String code = "";
    RelativeLayout relativeLayout;
    String API_KEY;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertor);

        relativeLayout = findViewById(R.id.RL_Convertor);
        tabLayout = findViewById(R.id.TBL_Convertor);
        viewPager = findViewById(R.id.ViewPager_Convertor);
        FAB_Convert = findViewById(R.id.FAB_Convertor);
        FAB_Copy = findViewById(R.id.FAB_Convertor_Copy);
        spinner = findViewById(R.id.Convertor_Spinner);
        customLoader = new CustomLoader();

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Object temp = applicationInfo.metaData.get("API_KEY");
            API_KEY = temp.toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        FAB_Copy.setVisibility(View.GONE);

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
                    FAB_Convert.setVisibility(View.VISIBLE);
                    FAB_Copy.setVisibility(View.GONE);
                    fabAnimation();
                }
                if (position == 1) {
                    tabLayout.getTabAt(1).removeBadge();
                    FAB_Convert.setVisibility(View.GONE);
                    FAB_Copy.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // listener for copy button
        FAB_Copy.setOnClickListener(view -> {
            if (!code.equals("")){
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("CodeBucket", code);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, "Code Copied", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "Nothing to copy...", Toast.LENGTH_SHORT).show();
           
        });

    }

    private void fabAnimation() {
        FAB_Convert.extend();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FAB_Convert.shrink();
            }
        }, 3000);
    }

    // this method is used to set the response from the convertor to the desired screen
    private void setCodeToOutputScreen(String code) {
        ConvertorOutputFragment convertorOutputFragment = new ConvertorOutputFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Convertor_Output, convertorOutputFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        convertorOutputFragment.setArguments(bundle);
        Log.d("PRATHAMESHADATE", "code setted");
    }

    // main method which send a request to server for converting code
    public void convertCode(String inputCode) {
        if (inputCode.isEmpty()) {
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

            String prompt = "##### Translate the below code into " + lang + "###\n" + inputCode + "\n###";

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

                    try {
                        JSONArray jsonArrayResponse = response.getJSONArray("choices");
                        JSONObject res = jsonArrayResponse.getJSONObject(0);
                        code = res.getString("text");
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
                    headers.put("Authorization", API_KEY);
                    return headers;
                }
            };


            SingletonAPI.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    // used to show ot snackbar
    private void showSnackBar(String message) {
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG).show();
    }


}