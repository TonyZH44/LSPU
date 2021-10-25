package com.anton.lspu.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LoginAsyncTask extends AsyncTask<String, Integer, String> {

    private String login, password;
    private String cookies;
    private Context context;
    private boolean remember;

    LoginAsyncTask(String login, String password, boolean remember, Context context) {
        this.login = login;
        this.password = password;
        this.context = context;
        this.remember = remember;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logIntoAccount(String login, String password) throws IOException {

        String urlParameters;

        if (remember) {
            urlParameters = "avt_login=" + login + "&avt_pass=" + password + "&opciya=avt" + "&avt_mem=on";
        } else {
            urlParameters = "avt_login=" + login + "&avt_pass=" + password + "&opciya=avt";
        }
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);


        Log.i("postData:",urlParameters);


        URL url = new URL("https://lspu-lipetsk.ru/modules.php?name=kabinet");
        Log.i("Url:", url.toString());
        HttpURLConnection connection;

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        connection = (HttpURLConnection) url.openConnection();     //create new connection with cookies


        connection.setRequestMethod("POST");
        connection.setRequestProperty("Cookie", StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));
        connection.setRequestProperty("Host", "lspu-lipetsk.ru");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(urlParameters.length()));
        connection.setRequestProperty("Cache-Control", "max-age=0");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("Origin", "https://lspu-lipetsk.ru");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connection.setRequestProperty("Referer", "https://lspu-lipetsk.ru/modules.php?name=kabinet");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");

        Log.i("Method:", connection.getRequestMethod());
        Log.i("Properties:", String.valueOf(connection.getRequestProperties()));



        try {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        connection.connect();

        String cookiesHeader = connection.getHeaderField("Set-Cookie");
        List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);

        for (HttpCookie cookie : cookies) {
            cookieManager.getCookieStore().add(null, cookie);
        }

        Log.i("postRequest:", String.valueOf(connection.getResponseCode()));

        connection.disconnect();
        //handle cookies


        connection = (HttpURLConnection) url.openConnection();     //create new connection with cookies
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));
        Log.i("Method:", connection.getRequestMethod());
        Log.i("Properties:", String.valueOf(connection.getRequestProperties()));
        connection.connect();

        Log.i("getRequest:", String.valueOf(connection.getResponseCode()));



        setCookies(StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));

        connection.disconnect();

        updateDB(this.login, this.password, this.cookies);
    }

    private void updateDB(String login, String password, String cookies){
        SessionDB sessionDB = new SessionDB(context);
        sessionDB.clearAll();
        sessionDB.add(login, password, cookies);
        sessionDB.close();
    }

    public boolean checkCookies(String cookies){
        if (cookies == null || !cookies.contains("account_type=") || !cookies.contains("account_login=")) return false;
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        try {
            logIntoAccount(login, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (checkCookies(this.cookies)) return getCookies();
        return null;
    }

    public void setCookies(String cookies){
        this.cookies = cookies;
    }

    public String getCookies(){
        return this.cookies;
    }
}
