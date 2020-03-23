package com.example.myapplicationwebviewjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    public static final String EXTRA = "com.example.myapplicationwebviewjava.address";
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main2);
        try {
            callService("http://192.168.0.14:8089/Geolocalizar/dce/BuscarDomicilio");
        }catch (Exception e ){
            e.printStackTrace();
        }
        String msg = getIntent().getStringExtra(EXTRA);
        msg = msg.replaceFirst("Texto obtenido de la imagen:","");
//        Log.d(TAG, "msg "+msg);
        TextView textView = findViewById(R.id.text_view_2);
        textView.setText(msg);
    }

    public static Intent newIntent(Context context, String addres){
        Intent intent = new Intent(context,Main2Activity.class);
        intent.putExtra(EXTRA,addres);
        return intent;
    }

    public Ubicacion callService(String address) throws Exception{
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(address).newBuilder();
            urlBuilder.addQueryParameter("entidad", "9");
            urlBuilder.addQueryParameter("direccion", "9");
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            List<Ubicacion> ubicaciones= stringToArray(jsonData , Ubicacion[].class);
            for (Ubicacion ubicacion:
                 ubicaciones) {
                Log.d(TAG, "callService: ubicacion: "+ubicacion);
            }
        return new Ubicacion();
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
}
