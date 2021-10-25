package com.anton.lspu.account.ui.news;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anton.lspu.account.R;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class NewsActivity extends AppCompatActivity {

    private String articleURL;
    private String articleHTML;
    private WebView newsWebView;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        newsWebView = findViewById(R.id.news_webview);
        articleURL = getIntent().getStringExtra("articleURL");

        try {
            articleHTML = new NewsArticleAsyncTask(articleURL).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        WebSettings webSettings = newsWebView.getSettings();
        webSettings.setTextZoom(50);
        newsWebView.getSettings().setBuiltInZoomControls(true);
        newsWebView.getSettings().setDisplayZoomControls(false);

        newsWebView.loadDataWithBaseURL(null,
                articleHTML,
                "text/html",
                "utf-8",
                null);
    }


}
