package com.anton.lspu.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.AbstractMap;
import java.util.HashMap;

public class SessionDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "sessionDB";
    public static final String TABLE_SESSION = "session";

    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_COOKIES = "cookies";



    public SessionDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableInfo = "CREATE TABLE " + TABLE_SESSION + " (" +
                COLUMN_LOGIN + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_COOKIES + " TEXT);";


        db.execSQL(createTableInfo);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);

        onCreate(db);
    }

    public void add(String login, String password, String cookies){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LOGIN, login);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_COOKIES, cookies);

        database.insert(TABLE_SESSION, null, contentValues);

    }

    public void clearAll(){
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_SESSION, null, null);
    }


    public HashMap<String, String> getSession(){
        String queryString = "SELECT * FROM "+ TABLE_SESSION;

        String login = null;
        String password = null;
        String cookies = null;

        HashMap<String, String> session = new HashMap<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                login = cursor.getString(0);
                password = cursor.getString(1);
                cookies = cursor.getString(2);

                session.put("login", login);
                session.put("password", password);
                session.put("cookies", cookies);

            }while (cursor.moveToNext());

        }else {
            Log.i("DB: ", "empty");
            return null;
        }

        cursor.close();
        database.close();

        return session;
    }

    public String getSessionCookies(){
        String queryString = "SELECT " + COLUMN_COOKIES + " FROM " + TABLE_SESSION;
        String cookies = null;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            cookies = cursor.getString(cursor.getColumnIndex(COLUMN_COOKIES));
        }
        cursor.close();
        database.close();

        return cookies;
    }

}
