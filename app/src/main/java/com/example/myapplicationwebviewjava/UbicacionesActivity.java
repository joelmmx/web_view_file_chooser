package com.example.myapplicationwebviewjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UbicacionesActivity extends AppCompatActivity {

    public static final String EXTRA_ADDRESS = "com.example.myapplicationwebviewjava.address";
    public static final String EXTRA_ENTIDAD = "com.example.myapplicationwebviewjava.entidad";
    private static final String TAG = "UbicacionesActivity";
    private RecyclerView recyclerUbicaciones;
    private List<Ubicacion> mUbicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.ubicacion_list);
        String direccion = getIntent().getStringExtra(EXTRA_ADDRESS);
        Log.d(TAG, "onCreate() direccion sin procesar: "+direccion);
        direccion = processAddress(direccion);
        Log.d(TAG, "onCreate() direccion procesada: "+direccion);
        recyclerUbicaciones = findViewById(R.id.list_ubicaciones);
        recyclerUbicaciones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new ConnectionTask().execute(direccion);


    }

    public void setupAdapter(){
        recyclerUbicaciones.setAdapter(new UbicacionesAdapter(mUbicaciones));
    }

//    private String processAddress(String direccion) {
//        StringTokenizer stk = new StringTokenizer(direccion,"\n");
//        List<String> lineas = new ArrayList<>();
//        while(stk.hasMoreTokens()){
//            String lineaOvbiar = stk.nextToken();
//            if(lineaOvbiar.contains("(")&&lineaOvbiar.contains(")"))
//                break;
//        }
//        while(stk.hasMoreTokens()){
//            String linea = stk.nextToken();
//            if(linea.contains("NO. DE SERVICIO"))
//                break;
//            else
//                lineas.add(linea);
//        }
//        direccion = "";
//        for (String linea : lineas) {
//            direccion += linea +" ";
//        }
//        return direccion.trim();
//    }

    private String processAddress(String direccion) {
        StringTokenizer stk = new StringTokenizer(direccion,"\n");
        List<String> lineas = new ArrayList<>();
        for(int i=0;i<5;i++){
            if(stk.hasMoreTokens()){
                stk.nextToken();
            }
        }
        while(stk.hasMoreTokens()){
            String linea = stk.nextToken();
            if(linea.contains("NO. DE SERVICIO"))
                break;
            else {
                if((linea.contains("$")&&linea.contains("."))
                 ||(linea.contains("(")&&linea.contains(")")))
                    continue;
                else {
                    lineas.add(linea);
                }
            }
        }
        direccion = "";
        for (String linea : lineas) {
            direccion += linea +" ";
        }
        return direccion.trim();
    }


    public static Intent newIntent(Context context, String addres, String entidad){
        Intent intent = new Intent(context, UbicacionesActivity.class);
        intent.putExtra(EXTRA_ADDRESS,addres);
        intent.putExtra(EXTRA_ENTIDAD,entidad);
        return intent;
    }

    public List<Ubicacion> callService(String address, String direccion,String entidad) throws Exception{
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(address).newBuilder();
            urlBuilder.addQueryParameter("entidad", entidad);
            urlBuilder.addQueryParameter("direccion", direccion);
            String url = urlBuilder.build().toString();
            Log.d(TAG, "callService: url: "+url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            List<Ubicacion> ubicaciones= stringToArray(jsonData , Ubicacion[].class);
        return ubicaciones;
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        super.onBackPressed();
//        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
//
//        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//        Log.d(TAG, "onBackPressed() called: "+taskList.get(0).topActivity.getClassName());
//        Log.d(TAG, "onBackPressed() called: "+taskList.get(1).topActivity.getClassName());
//
//        if(taskList.get(0).numActivities == 1 &&
//                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
//            Log.i(TAG, "This is last activity in the stack");
//        }
//
//
//    }

    private class ConnectionTask extends AsyncTask<String,Void,List<Ubicacion>>{

        @Override
        protected List<Ubicacion> doInBackground(String... args) {
            try {
                List<Ubicacion> ubicaciones = callService("http://192.168.0.14:8089/Geolocalizar/dce/BuscarDomicilio",
                        args[0],
                        getIntent().getStringExtra(EXTRA_ENTIDAD));
                        return ubicaciones;
            }catch (Exception e ){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Ubicacion> ubicaciones){
            if(ubicaciones==null){
                ubicaciones = new ArrayList<>();
            }
            ubicaciones.forEach(ubicacion -> Log.d(TAG, "onCreate() ubicacion: "+ubicacion));
            mUbicaciones = ubicaciones;
            setupAdapter();
        }
    }

    private class UbicacionesAdapter extends RecyclerView.Adapter<UbicacionHolder> {

        private List<Ubicacion> mUbicaciones;

        public UbicacionesAdapter(List<Ubicacion> ubicaciones) {
            mUbicaciones = ubicaciones;
        }

        @NonNull
        @Override
        public UbicacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UbicacionHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.ubicacion_holder,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull UbicacionHolder holder, int position) {
            holder.bindUbicacion(mUbicaciones.get(position));
        }

        @Override
        public int getItemCount() {
            return mUbicaciones.size();
        }


    }

    private class UbicacionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mCalleTextView;
        private TextView mColoniaTextView;
        private Ubicacion mUbicacion;

        public UbicacionHolder(@NonNull View ubicacionView) {
            super(ubicacionView);
            mCalleTextView   = ubicacionView.findViewById(R.id.text_view_calle);
            mColoniaTextView = ubicacionView.findViewById(R.id.text_view_colonia);
            ubicacionView.setOnClickListener(this);
        }

        public void bindUbicacion(Ubicacion ubicacion){
            Log.d(TAG, "bindUbicacion() called with: ubicacion = [" + ubicacion + "]");
            mCalleTextView.setText(ubicacion.getCalle());
            mColoniaTextView.setText(ubicacion.getColonia());
            mUbicacion = ubicacion;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick() called with: v = [" + mUbicacion.getCalle() + " clicked!" + "]");
            Toast.makeText(getApplicationContext() , mUbicacion.getCalle() + " clicked!" , Toast.LENGTH_SHORT).show();
            startActivity(MapaActivity.newIntent(getApplicationContext() , mUbicacion));
        }
    }
}
