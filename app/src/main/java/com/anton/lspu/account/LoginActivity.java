package com.anton.lspu.account;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    EditText loginText, passwordText;
    Button buttonLogin, buttonClear;
    CheckBox checkBoxMemory;
    String cookies;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);



        DBAsyncTask dbAsyncTask = new DBAsyncTask(getBaseContext());

        progressBar = findViewById(R.id.progressBar);
        loginText = findViewById(R.id.login);
        passwordText = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.button_login);
        buttonClear = findViewById(R.id.button_clear);
        checkBoxMemory = findViewById(R.id.checkBox);



        if (dbAsyncTask.checkDB()){
            loginText.setText(dbAsyncTask.getLoginAndPassword().get("login"));
            passwordText.setText(dbAsyncTask.getLoginAndPassword().get("password"));
            //get cookies from DB and log in website with them
            //Intent logIntent = new Intent(getBaseContext(), DrawerActivity.class);
            //logIntent.putExtra("cookies", dbAsyncTask.getCookies());
            //Log.i("DBcookies:", dbAsyncTask.getCookies());
            //startActivity(logIntent);
        }

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginText.setText("");
                passwordText.setText("");
                dbAsyncTask.clearDB();
                Toast.makeText(getBaseContext(), "DB is cleared",Toast.LENGTH_SHORT).show();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(ProgressBar.VISIBLE);

                String login = loginText.getText().toString();
                String password = passwordText.getText().toString();
                boolean remember = false;
                cookies = null;
                if (checkBoxMemory.isChecked()) remember = true;



                try {
                    cookies = new LoginAsyncTask(login, password, remember, getBaseContext()).execute().get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (cookies != null) {

                    Intent logIntent = new Intent(getBaseContext(), DrawerActivity.class);
                    logIntent.putExtra("cookies", cookies);
                    startActivity(logIntent);

                }
                else {
                    Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });

    }



    public void loadLocale(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = preferences.getString("lang", "");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }



}


