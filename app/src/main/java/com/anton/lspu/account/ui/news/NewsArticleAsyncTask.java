package com.anton.lspu.account.ui.news;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsArticleAsyncTask extends AsyncTask<String, String, String> {


    private String newsURL;

    private String newsHTML;


    NewsArticleAsyncTask(String url) throws MalformedURLException {

        this.newsURL = url;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {

        Document doc = getResponse(newsURL);

        setArticleHTML(doc);


        return newsHTML;
    }

    public Document getResponse(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            Log.i("newsArticle_response:", String.valueOf(doc));

            Document docParsed = Jsoup.parse(String.valueOf(doc.getElementsByClass("content_secondpage")));
            Log.i("newsArticle_parsed:", String.valueOf(docParsed));
            return docParsed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setArticleHTML(Document doc){
        this.newsHTML = getNewsHTMLwithImages(String.valueOf(doc));

    }

    private String getNewsHTMLwithImages(String html){
        if (html.contains("img src=\"images/")) return html.replace("img src=\"images/", "img src=\"https://lspu-lipetsk.ru/images/");
        return html;
    }

}