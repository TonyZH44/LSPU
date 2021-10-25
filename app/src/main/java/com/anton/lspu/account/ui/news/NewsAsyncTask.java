package com.anton.lspu.account.ui.news;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAsyncTask extends AsyncTask<String, String, List<Article>> {

    private List<Article> articles = new ArrayList<>();

    private URL archiveUrl = new URL("https://lspu-lipetsk.ru/modules.php?name=News_archive&start=0");

    NewsAsyncTask() throws MalformedURLException {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected List<Article> doInBackground(String... strings) {
        //getResponse();
        //getArticles(getResponse());
        return generateArticles(getResponse());
    }



    public List<Article> generateArticles(Document response){


        Elements aElements = response.getElementsByAttributeValue("class", "news_archive");

        while (aElements.size() > 5){
            aElements.remove(5);
        }

        for (Element aElement : aElements) {

            Element photoElement = aElement.child(0);
            Element infoElement = aElement.child(1);
            String url = "https://lspu-lipetsk.ru/" + aElement.attr("href");
            String imageUrl = "https://lspu-lipetsk.ru/" + photoElement.child(0).attr("src");
            String date = infoElement.child(1).text();
            String title = infoElement.child(2).text();
            String description = infoElement.child(3).text();
            byte[] imageBinaryData = new byte[0];



            try {

                imageBinaryData = Jsoup.connect(imageUrl).ignoreContentType(true).execute().bodyAsBytes();


            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("arcticleParams:", url+" "+title+" "+description+" "+imageBinaryData+" "+date);

            articles.add(new Article(url, title, description, imageBinaryData, date));
        }

        return articles;
    }


    public Document getResponse(){
        try {
            Document doc = Jsoup.connect(String.valueOf(archiveUrl)).get();
            doc = Jsoup.parse(String.valueOf(doc.getElementsByClass("news_archive")));
            Log.i("News_responseJsoup:", String.valueOf(doc));
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}