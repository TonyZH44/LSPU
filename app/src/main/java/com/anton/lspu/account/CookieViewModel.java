package com.anton.lspu.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CookieViewModel extends ViewModel {

    private MutableLiveData<String> cookies = new MutableLiveData<>();

    public void setCookies(MutableLiveData<String> cookies){
        this.cookies = cookies;
    }

    public MutableLiveData<String> getCookies(){
        return this.cookies;
    }

}
