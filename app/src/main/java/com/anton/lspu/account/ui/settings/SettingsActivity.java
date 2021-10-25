package com.anton.lspu.account.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anton.lspu.account.DBAsyncTask;
import com.anton.lspu.account.LoginActivity;
import com.anton.lspu.account.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private TextView text_lang;
    private Spinner spinner_lang;
    private Button button_exit;
    private String[] languages = {"English", "Русский"};
    private int spinner_pos;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);


        text_lang = findViewById(R.id.text_language);
        spinner_lang = findViewById(R.id.spinner_language);
        button_exit = findViewById(R.id.button_exit);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                languages);

        spinner_lang.setAdapter(arrayAdapter);
        spinner_lang.setSelection(spinner_pos);

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_pos = position;
                switch (position){

                    case 0:
                        setLocale("en", position);
                        break;

                    case 1:
                        setLocale("ru", position);
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBAsyncTask dbAsyncTask = new DBAsyncTask(getBaseContext());
                //new ExitAsynkTask(dbAsyncTask.getCookies()).exitAccount();
                dbAsyncTask.clearDB();
                Intent exitIntent = new Intent(getBaseContext(), LoginActivity.class);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(exitIntent);
                finish();
            }
        });
    }

    private void setLocale(String lang, int pos) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Language", lang);
        editor.putInt("Lang_pos", pos);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("Language", "ru");
        spinner_pos = prefs.getInt("Lang_pos",1);
        setLocale(lang, spinner_pos);
    }

    /*private static class ExitAsynkTask extends AsyncTask{

        private String url = "https://lspu-lipetsk.ru/modules.php?name=kabinet&opciya=exit";
        private String cookies;

        ExitAsynkTask(String cookies){
            this.cookies = cookies;
        }

        public void exitAccount(){
            try {
                Document doc = Jsoup.connect(url).cookies(splitToMap(cookies, ";","=")).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

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

    }*/

}
