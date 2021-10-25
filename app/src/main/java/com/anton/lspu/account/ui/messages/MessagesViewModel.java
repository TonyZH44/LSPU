package com.anton.lspu.account.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anton.lspu.account.ui.schedule.ScheduleAsyncTask;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class MessagesViewModel extends ViewModel {

    private String messagesHTML;

    private String cookies;

    public MessagesViewModel(){}

    public String getMessagesHTML(){
        return messagesHTML;
    }

    public void executeConnection(){

        if (cookies == null) return;

        try {
            try {
                messagesHTML = new MessagesAsyncTask(cookies).execute().get();
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