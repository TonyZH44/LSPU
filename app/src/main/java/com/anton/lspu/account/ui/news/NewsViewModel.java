package com.anton.lspu.account.ui.news;


import androidx.lifecycle.ViewModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends ViewModel {

    private List<Article> articles = new ArrayList<>();

    public NewsViewModel() {

        try {

            articles = new NewsAsyncTask().execute().get();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public List<Article> getArticles(){
        return this.articles;
    }

}