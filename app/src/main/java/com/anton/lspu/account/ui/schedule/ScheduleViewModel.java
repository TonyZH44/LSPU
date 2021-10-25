package com.anton.lspu.account.ui.schedule;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.anton.lspu.account.ui.news.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ScheduleViewModel extends ViewModel {

    //private List<ScheduleRow> scheduleList = new ArrayList<>();

    private String scheduleHTMLTable;
    private String url = null;
    private String cookies;

    public ScheduleViewModel() {

    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getScheduleHTMLTable(){
        return scheduleHTMLTable;
    }


    public void executeConnection(){

        if (cookies == null) return;

        try {
            try {
                if (url != null)
                    scheduleHTMLTable = new ScheduleAsyncTask(cookies, url).execute().get();
                else
                    scheduleHTMLTable = new ScheduleAsyncTask(cookies).execute().get();

                if (scheduleHTMLTable.contains("hidden")){
                    this.scheduleHTMLTable = scheduleHTMLTable.replace("div id=\"p22\" style=\"visibility: hidden; display: none;\"","div id=\"p22\" style=\"visibility: visible; display: block;\"");

                }

                if (scheduleHTMLTable.contains("modules.php?name=kabinet&amp;active_item=22")){
                    this.scheduleHTMLTable = scheduleHTMLTable.replace("modules.php?name=kabinet&amp;active_item=22", "https://lspu-lipetsk.ru/modules.php?name=kabinet&amp;active_item=22");

                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setCookies(String cookies){
        this.cookies = cookies;
    }




}