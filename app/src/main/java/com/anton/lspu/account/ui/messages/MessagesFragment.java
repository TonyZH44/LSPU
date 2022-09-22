package com.anton.lspu.account.ui.messages;

import static androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF;
import static androidx.webkit.WebSettingsCompat.FORCE_DARK_ON;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.anton.lspu.account.CookieViewModel;
import com.anton.lspu.account.R;

public class MessagesFragment extends Fragment {

    private WebView messagesWebView;
    private MessagesViewModel messagesViewModel;
    private CookieViewModel cookieViewModel;
    private String cookies;
    private String messagesHTML;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cookieViewModel = new ViewModelProvider(requireActivity()).get(CookieViewModel.class);
        cookies = String.valueOf(cookieViewModel.getCookies());

        messagesViewModel =
                new ViewModelProvider(this).get(MessagesViewModel.class);
        messagesViewModel.setCookies(cookies);
        messagesViewModel.executeConnection();



        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        messagesWebView = root.findViewById(R.id.messageswebview);

        messagesHTML = messagesViewModel.getMessagesHTML();

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    WebSettingsCompat.setForceDark(messagesWebView.getSettings(), FORCE_DARK_ON);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebSettingsCompat.setForceDark(messagesWebView.getSettings(), FORCE_DARK_OFF);
                    break;
            }
        }


        messagesWebView.loadDataWithBaseURL(null,
                                                    messagesHTML,
                                            "text/html",
                                            "utf-8",
                                            null);

        return root;
    }
}