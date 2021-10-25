package com.anton.lspu.account.ui.schedule;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAsyncTask extends AsyncTask<String, String, String> {

    private String cookies;

    private String scheduleURL = "https://lspu-lipetsk.ru/modules.php?name=kabinet&active_item=22";

    private String scheduleHTMLTable;

    ScheduleAsyncTask(String cookies) throws MalformedURLException {
        this.cookies = cookies;
    }

    ScheduleAsyncTask(String cookies, String url) throws MalformedURLException {
        this.cookies = cookies;
        this.scheduleURL = url;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {

        Document doc = getResponse(cookies);

        setScheduleHTMLTable(doc);

        //return generateRows(doc);
        return scheduleHTMLTable;
    }

    public Document getResponse(String cookies){
        try {
            Document doc = Jsoup.connect(scheduleURL)
                    .cookies(splitToMap(cookies, ";","="))
                    .get();
            Log.i("Schedule_response:", String.valueOf(doc));

            Document docParsed = Jsoup.parse(String.valueOf(doc.getElementsByTag("head"))
                    + String.valueOf(doc.getElementsByAttributeValue("id","p22")), "https://lspu-lipetsk.ru");

            Elements select = docParsed.select("a[href]");

            select = docParsed.select("link");
            for (Element e : select){
                e.attr("href", e.absUrl("href"));
            }

            select = docParsed.select("script");
            for (Element e : select){
                e.attr("src", e.absUrl("src"));
            }


            Log.i("Schedule_parsed:", String.valueOf(docParsed));
            return docParsed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> splitToMap(String source, String entriesSeparator, String keyValueSeparator) {
        Map<String, String> map = new HashMap<String, String>();
        String[] entries = source.split(entriesSeparator);
        for (String entry : entries) {
            if (!TextUtils.isEmpty(entry) && entry.contains(keyValueSeparator)) {
                String[] keyValue = entry.split(keyValueSeparator);
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    public void setScheduleHTMLTable(Document doc){
        this.scheduleHTMLTable = String.valueOf(doc);
    }


}