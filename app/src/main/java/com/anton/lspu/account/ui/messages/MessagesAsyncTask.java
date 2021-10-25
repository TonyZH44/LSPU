package com.anton.lspu.account.ui.messages;

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

public class MessagesAsyncTask extends AsyncTask<String, String, String> {

    private final String cookies;

    private final URL messagesURL = new URL("https://lspu-lipetsk.ru/modules.php?name=kabinet&active_item=30");

    private String messagesHTML;

    MessagesAsyncTask(String cookies) throws MalformedURLException {
        this.cookies = cookies;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {

        Document doc = getResponse(cookies);

        setMessagesHTML(doc);


        return messagesHTML;
    }

    public Document getResponse(String cookies){
        try {
            Document doc = Jsoup.connect(String.valueOf(messagesURL))
                    .cookies(splitToMap(cookies, ";","="))
                    .get();

            Log.i("Messages_response:", String.valueOf(doc));

            doc = Jsoup.parse(String.valueOf(doc.getElementsByAttributeValue("id", "p30")));
            Log.i("Messages_parsed:", String.valueOf(doc));
            return doc;
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

    public void setMessagesHTML(Document doc){

        String messagesHTMLEdited = String.valueOf(doc)
                .replace("hidden", "visible")
                .replace("none", "block");

        if (messagesHTMLEdited.contains("<a href=\"modules.php?")) {
            messagesHTMLEdited = messagesHTMLEdited.replace("<a href=\"modules.php?",
                                                        "<a href=\"https://lspu-lipetsk.ru/modules.php?");
        }

        Log.i("Messages_edited",messagesHTMLEdited);

        this.messagesHTML = messagesHTMLEdited;
    }

}
