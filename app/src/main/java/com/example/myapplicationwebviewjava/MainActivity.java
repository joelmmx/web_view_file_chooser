package com.example.myapplicationwebviewjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import okhttp3.Call;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    private WebView webView;
    private String url = "http://189.240.30.58:810/ine/";

    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        setListeners();
        changeActionBarColor();
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

    public void initFields() {
        // TODO Auto-generated method stub

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
    }
    public void setListeners() {
        // TODO Auto-generated method stub

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                webView.loadUrl("about:blank");

                view.clearHistory();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG,"shouldOverrideUrlLoading: request.getUrl(): "+ request.getUrl());
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                       WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (urlShouldBeHandledByWebView(url)) {
                    return super.shouldInterceptRequest(view, request);
                }

                return handleRequestViaOkHttp(url);
            }

            private boolean urlShouldBeHandledByWebView(String url) {
                Log.d(TAG,"intercepting req....!!!: "+ url);
                if(url.contains("vision.googleapis.com")){
                    return false;
                }
                return true;
            }

            @NonNull
            private WebResourceResponse handleRequestViaOkHttp(@NonNull final String url)  {
                try {
                    Log.d(TAG, "handleRequestViaOkHttp: url:"+url);
                    // On Android API >= 21 you can get request method and headers
                    // As I said, we need to only display "simple" page with resources
                    // So it's GET without special headers
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    final Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            // failure case
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // success case
                            Log.d(TAG, "onResponse "+url+": "+response.body().string());
                            if(url.contains("vision.googleapis.com")){
                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: ");
//                                    webView.loadUrl(
//                                            "javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
//                        webView.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
//                                    webView.loadUrl("javascript:console.log(document.getElementById('base64').value);");
//                                    try {
//                                        Thread.sleep(3000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                        webView.loadUrl("javascript:console.log(document.getElementById('demo').textContent);");

                                    }
                                });
                            }
                        }
                    });

                    return null;
                } catch (Exception e) {
                    Log.e(TAG,"ERROR: "+e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void onPageFinished(WebView view, String url)
            {
                /* This call inject JavaScript into the page which just finished loading. */
                Log.d(TAG, "onPageFinished: url: "+url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("source://")) {
                    try {
                        String html = URLDecoder.decode(url, "UTF-8").substring(9);
                        Log.d(TAG, "shouldOverrideUrlLoading: "+html);

                    } catch (UnsupportedEncodingException e) {
                        Log.e("example", "failed to decode source", e);
                    }
                }
                // For all other links, let the WebView do it's normal thing
                return false;
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {

            }
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                Log.d(TAG, "onShowFileChooser: filePathCallback: "+filePathCallback.toString());
                mUploadMessage = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                return true;
            }
            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg)
            {
                Log.d(TAG, "onConsoleMessage: ");
                if(cmsg.message().contains("Cargando")) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
//                            Log.d(TAG, "run: ");
                            webView.loadUrl("javascript:console.log(document.getElementById('demo').textContent);");

                        }
                    });
                }else{
                    Log.d(TAG, "onConsoleMessage: msg: "+cmsg.message());
                    if(cmsg.message().contains("Texto obtenido de la imagen")){
//                        startSecondActivity(cmsg);
                        startActivity(UbicacionesActivity.newIntent(getApplicationContext(), cmsg.message(), "9"));
                    }

                }
                return false;
            }

        });

        webView.loadUrl(url);

    }

//    boolean wasStarted;
//    public synchronized void startSecondActivity(ConsoleMessage cmsg){
//        Log.d(TAG, "startSecondActivity() called with: cmsg = [" + cmsg + "]");
//        if(!wasStarted) {
//            startActivity(UbicacionesActivity.newIntent(getApplicationContext(), cmsg.message(), "9"));
//            wasStarted = true;
//        }
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        wasStarted = false;
//    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Log.d(TAG, "onBackPressed() called: "+isTaskRoot());
        if (webView.canGoBack() == true) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        Log.d(TAG, "onResume() called");
//        if(fromChooserActivity){
//            fromChooserActivity = false;
//        }else{
//            setListeners();
//        }
//    }

   /* public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            // webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";");
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(MainActivity.this);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }
    }*/

//   boolean fromChooserActivity;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], intent = [" + intent + "]");
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            Uri[] uris = new Uri[1];
            uris[0] = result;
            mUploadMessage.onReceiveValue(uris);
            mUploadMessage = null;
//            fromChooserActivity = true;
        }
    }
}
