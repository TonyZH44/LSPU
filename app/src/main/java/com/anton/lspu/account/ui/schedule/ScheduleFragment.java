package com.anton.lspu.account.ui.schedule;

import android.os.Build;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.anton.lspu.account.CookieViewModel;
import com.anton.lspu.account.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private WebView scheduleWebView;

    private ScheduleViewModel scheduleViewModel;
    private CookieViewModel cookieViewModel;
    private String cookies;
    private String htmlTable;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cookieViewModel = new ViewModelProvider(requireActivity()).get(CookieViewModel.class);
        cookies = String.valueOf(cookieViewModel.getCookies());

        scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);
        scheduleViewModel.setCookies(cookies);
        scheduleViewModel.executeConnection();

        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        scheduleWebView = root.findViewById(R.id.schedulewebview);
        scheduleWebView.setWebViewClient(new MyWebViewClient());

        htmlTable = scheduleViewModel.getScheduleHTMLTable();

        WebSettings webSettings = scheduleWebView.getSettings();
        webSettings.setTextZoom(50);
        scheduleWebView.getSettings().setBuiltInZoomControls(true);
        scheduleWebView.getSettings().setDisplayZoomControls(false);

        scheduleWebView.loadDataWithBaseURL(null,
                                             htmlTable,
                                    "text/html",
                                     "utf-8",
                                     null);

        return root;
    }

    private class MyWebViewClient extends WebViewClient{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            scheduleViewModel.setUrl(String.valueOf(request.getUrl()));
            scheduleViewModel.executeConnection();
            htmlTable = scheduleViewModel.getScheduleHTMLTable();
            scheduleWebView.loadDataWithBaseURL(null,
                    htmlTable,
                    "text/html",
                    "utf-8",
                    null);


            return true;
        }


    }
}