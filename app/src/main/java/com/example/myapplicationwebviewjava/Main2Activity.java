package com.example.myapplicationwebviewjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    public static final String EXTRA = "com.example.myapplicationwebviewjava.address";
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String msg = getIntent().getStringExtra(EXTRA);
        msg = msg.replaceFirst("Texto obtenido de la imagen:","");
        Log.d(TAG, "msg "+msg);
        TextView textView = findViewById(R.id.text_view_2);
        textView.setText(msg);
    }

    public static Intent newIntent(Context context, String addres){
        Intent intent = new Intent(context,Main2Activity.class);
        intent.putExtra(EXTRA,addres);
        return intent;
    }
}
