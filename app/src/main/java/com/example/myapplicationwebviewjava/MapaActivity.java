package com.example.myapplicationwebviewjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class MapaActivity extends AppCompatActivity {

    public static final String EXTRA_UBICACION = "com.example.myapplicationwebviewjava.ubicacion";

    private Ubicacion ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ubicacion = new Gson().fromJson(getIntent().getStringExtra(EXTRA_UBICACION),Ubicacion.class);
        TextView textView = findViewById(R.id.text_view_3);
        textView.setText(ubicacion.getCalle());
    }

    public static Intent newIntent(Context context, Ubicacion ubicacion){
        Intent intent = new Intent(context, MapaActivity.class);
        intent.putExtra(EXTRA_UBICACION,new Gson().toJson(ubicacion));
        return intent;
    }
}
