package com.example.myapplicationwebviewjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class MapaActivity extends AppCompatActivity {

    public static final String EXTRA_UBICACION = "com.example.myapplicationwebviewjava.ubicacion";
    private static final String TAG = "MapaActivity";

    private Ubicacion mUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        mUbicacion = new Gson().fromJson(getIntent().getStringExtra(EXTRA_UBICACION),Ubicacion.class);

        TextView textView = findViewById(R.id.mapa_geoloc_text_view);
        textView.setText(mUbicacion.getGeoloc());

        textView = findViewById(R.id.mapa_colonia_text_view);
        textView.setText(mUbicacion.getColonia());

        textView = findViewById(R.id.mapa_calle_text_view);
        textView.setText(mUbicacion.getCalle());

        textView = findViewById(R.id.mapa_numero_text_view);
        textView.setText(mUbicacion.getNumero());

        textView = findViewById(R.id.mapa_cp_text_view);
        textView.setText(mUbicacion.getCp());

        textView = findViewById(R.id.mapa_edificio_text_view);
        textView.setText(mUbicacion.getEdificio());

        textView = findViewById(R.id.mapa_entrada_text_view);
        textView.setText(mUbicacion.getEntrada());

        textView = findViewById(R.id.mapa_localidad_text_view);
        textView.setText(mUbicacion.getLocalidad());

        textView = findViewById(R.id.mapa_edmslm_text_view);
        textView.setText(mUbicacion.getEdmslm());

        textView = findViewById(R.id.mapa_longitud_text_view);
        textView.setText(mUbicacion.getLocalidad());

        textView = findViewById(R.id.mapa_latitud_text_view);
        textView.setText(mUbicacion.getLatitud());

        textView = findViewById(R.id.mapa_entidad_text_view);
        textView.setText(mUbicacion.getEntidad());

        Button btnMostrarMapa = findViewById(R.id.btn_mostrar_mapa);
        btnMostrarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick() called with: v = [" + v + "]");
                startActivity(MapaWebViewActivity.newIntent(getApplicationContext() , mUbicacion));
            }
        });

    }

    public static Intent newIntent(Context context, Ubicacion ubicacion){
        Intent intent = new Intent(context, MapaActivity.class);
        intent.putExtra(EXTRA_UBICACION,new Gson().toJson(ubicacion));
        return intent;
    }
}
