package com.anton.lspu.account;


import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;

import android.os.Bundle;



import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    EditText loginText, passwordText;
    Button buttonLogin, buttonClear;
    CheckBox checkBoxMemory;
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

        buttonClear.setOnClickListener(v -> {
            loginText.setText("");
            passwordText.setText("");
            dbAsyncTask.clearDB();
            Toast.makeText(getBaseContext(), "DB is cleared",Toast.LENGTH_SHORT).show();
        });


        buttonLogin.setOnClickListener(v -> {

            progressBar.setVisibility(ProgressBar.VISIBLE);

            new LoginATask(LoginActivity.this).execute();

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

    private static class LoginATask extends AsyncTask<Void, Void, Void>{


        private final WeakReference<LoginActivity> weakReference;
        String login, password, cookies;
        Boolean remember;

        LoginATask(LoginActivity context){
            weakReference = new WeakReference<>(context);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            LoginActivity activity = weakReference.get();

            login = activity.loginText.getText().toString();
            password = activity.passwordText.getText().toString();
            remember = activity.checkBoxMemory.isChecked();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            LoginActivity activity = weakReference.get();

            try {
                cookies = new LoginAsyncTask(login, password, remember, activity).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (cookies != null) {

                Intent logIntent = new Intent(activity, DrawerActivity.class);
                logIntent.putExtra("cookies", cookies);
                activity.startActivity(logIntent);

            }
            else {
                Toast.makeText(activity, "Error",Toast.LENGTH_SHORT).show();
                activity.progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }

    }
}


