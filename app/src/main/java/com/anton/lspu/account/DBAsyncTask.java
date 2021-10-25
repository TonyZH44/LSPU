package com.anton.lspu.account;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

public class DBAsyncTask extends AsyncTask<String, String, Boolean> {

    private Context context;


    public DBAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        return null;
    }

    public HashMap<String, String> getLoginAndPassword(){
        SessionDB sessionDB = new SessionDB(context);
        return sessionDB.getSession();
    }

    public String getCookies(){
        SessionDB sessionDB = new SessionDB(context);
        return sessionDB.getSessionCookies();
    }

    public boolean checkDB(){
        SessionDB sessionDB = new SessionDB(context);
        HashMap<String, String> sessionInfo = sessionDB.getSession();
        if (sessionInfo != null){
            Log.i("checkDB: ", "true");
            return true;
        }
        else {
            Log.i("checkDB: ", "false");
            return false;
        }
    }

    public void clearDB(){
        SessionDB sessionDB = new SessionDB(context);
        sessionDB.clearAll();
    }

}
