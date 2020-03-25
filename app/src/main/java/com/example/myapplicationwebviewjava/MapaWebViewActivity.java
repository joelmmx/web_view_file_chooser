package com.example.myapplicationwebviewjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;

import okhttp3.HttpUrl;

public class MapaWebViewActivity extends AppCompatActivity {

    public static final String EXTRA_UBICACION = "com.example.myapplicationwebviewjava.ubicacionwebview";
    private static final String TAG = "MapaWebViewActivity";
    private WebView mapWebView;
    private String base_url="http://192.168.0.14:8088/Ubicar/dce/GenerarMapa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_web_view);
        initFields();
        String jsonUbiacion = getIntent().getStringExtra(EXTRA_UBICACION);
        Log.d(TAG, "onCreate() called with: jsonUbiacion = [" + jsonUbiacion + "]");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(base_url).newBuilder();
        urlBuilder.addQueryParameter("ubicacion", jsonUbiacion);
        String FINAL_URL = urlBuilder.build().toString();
        Log.d(TAG, "onCreate() called with: final_url = [" + FINAL_URL + "]");
        mapWebView.loadUrl(FINAL_URL);
        changeActionBarColor();
    }

    public static Intent newIntent(Context context, Ubicacion ubicacion){
        Intent intent = new Intent(context, MapaWebViewActivity.class);
        intent.putExtra(EXTRA_UBICACION,new Gson().toJson(ubicacion));
        return intent;
    }

    public void initFields() {
        // TODO Auto-generated method stub

        mapWebView = findViewById(R.id.mapWebView);
        mapWebView.getSettings().setJavaScriptEnabled(true);
        mapWebView.getSettings().setBuiltInZoomControls(true);
        mapWebView.getSettings().setAllowFileAccess(true);
    }

    public void changeActionBarColor(){
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF69B4"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }
}
