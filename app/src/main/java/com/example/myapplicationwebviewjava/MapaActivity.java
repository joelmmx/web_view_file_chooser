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

        TextView textView = findViewById(R.id.mapa_geoloc_text_view);
        textView.setText(ubicacion.getGeoloc());

        textView = findViewById(R.id.mapa_colonia_text_view);
        textView.setText(ubicacion.getColonia());

        textView = findViewById(R.id.mapa_calle_text_view);
        textView.setText(ubicacion.getCalle());

        textView = findViewById(R.id.mapa_numero_text_view);
        textView.setText(ubicacion.getNumero());

        textView = findViewById(R.id.mapa_cp_text_view);
        textView.setText(ubicacion.getCp());

        textView = findViewById(R.id.mapa_edificio_text_view);
        textView.setText(ubicacion.getEdificio());

        textView = findViewById(R.id.mapa_entrada_text_view);
        textView.setText(ubicacion.getEntrada());

        textView = findViewById(R.id.mapa_localidad_text_view);
        textView.setText(ubicacion.getLocalidad());

        textView = findViewById(R.id.mapa_edmslm_text_view);
        textView.setText(ubicacion.getEdmslm());

        textView = findViewById(R.id.mapa_longitud_text_view);
        textView.setText(ubicacion.getLocalidad());

        textView = findViewById(R.id.mapa_latitud_text_view);
        textView.setText(ubicacion.getLatitud());

        textView = findViewById(R.id.mapa_entidad_text_view);
        textView.setText(ubicacion.getEntidad());

    }

    public static Intent newIntent(Context context, Ubicacion ubicacion){
        Intent intent = new Intent(context, MapaActivity.class);
        intent.putExtra(EXTRA_UBICACION,new Gson().toJson(ubicacion));
        return intent;
    }
}
