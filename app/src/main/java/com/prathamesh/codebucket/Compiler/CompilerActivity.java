package com.prathamesh.codebucket.Compiler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.usage.ConfigurationStats;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.prathamesh.codebucket.Adapter.CompilerViewPagerAdapter;
import com.prathamesh.codebucket.Adapter.CustomSpinnerAdapter;
import com.prathamesh.codebucket.Constants;
import com.prathamesh.codebucket.Loader.CustomLoader;
import com.prathamesh.codebucket.R;
import com.prathamesh.codebucket.SingletonAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class CompilerActivity extends AppCompatActivity {

    TabLayout tabLayout;
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    ExtendedFloatingActionButton FAB;
    Spinner spinner;
    String languageKey = "";
    CustomLoader customLoader;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiler);

        FAB = findViewById(R.id.FAB_Compiler);
        tabLayout = findViewById(R.id.TBL_Compiler);
        viewPager = findViewById(R.id.ViewPager_Compiler);
        relativeLayout = findViewById(R.id.RL_Compiler);
        spinner = findViewById(R.id.Compiler_Spinner);
        customLoader = new CustomLoader();

        CompilerViewPagerAdapter compilerViewPagerAdapter = new CompilerViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(compilerViewPagerAdapter);
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

        fabAnimation();

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

    // fab animation method
    private void fabAnimation() {
        FAB.extend();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FAB.shrink();
            }
        }, 3000);
    }
    private void setTextToOutputScreen(String text) {
        CompilerOutputFragment compilerOutputFragment = new CompilerOutputFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Compiler_Output, compilerOutputFragment).commit();
        TextView textView = findViewById(R.id.TV_Compiler_Output);
        textView.setText(text);
    }

    // API request method
    public void compile(String code, String input) {

        customLoader.showCustomLoader(this, "Compiling...");
        JSONObject payload = new JSONObject();

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

        try {
            payload.put("code", code);
            payload.put("language", lang);
            payload.put("input", input);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.Compiler_API, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("error").isEmpty()) {
                        setTextToOutputScreen(response.getString("output"));
                        Log.d("PRATHAMESHADATE", response.getString("output"));
                    } else {
                        setTextToOutputScreen(response.getString("error"));
                        Log.d("PRATHAMESHADATE", response.getString("error"));
                    }
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
        });

        int timeout = 32000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        SingletonAPI.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void showSnackBar(String message) {
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void showInputDialog(String code) {

        if (code.isEmpty())
            showSnackBar("Please write some code...!");
        else {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setView(view);

            final EditText editText = view.findViewById(R.id.ET_InputDialog);
            alertDialog.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String input = editText.getText().toString();
                    compile(code, input);
                }
            });

            AlertDialog showDialog = alertDialog.create();
            showDialog.show();
        }
    }
}