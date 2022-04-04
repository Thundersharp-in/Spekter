package thundersharp.aigs.spectre.core.browser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import java.util.Random;

import thundersharp.aigs.spectre.R;

public class Browser extends AppCompatActivity {

    public static void loadUrl(@NonNull Context context,@NonNull String url){
        context.startActivity(new Intent(context,Browser.class).putExtra("url",url));
    }

    WebView webView;
    ProgressBar progressBar;
    Toolbar toolbar;
    Intent intent;
    ImageView img_not_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        toolbar = findViewById(R.id.tool);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webView = findViewById(R.id.pdfviwer);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---

        webView.setWebViewClient(new Callback());
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...

                progressBar.setProgress(progress); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100){
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                }

            }
        });

        webView.loadUrl(uri.toString());

            toolbar.setTitle(webView.getTitle());

    }

    private class Callback extends WebViewClient {
        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){
                //HIDE LOADING IT HAS FINISHED
                progressBar.setVisibility(View.GONE);
                toolbar.setTitle(webView.getTitle());



            } else{
                redirect = false;
            }

        }
    }

}