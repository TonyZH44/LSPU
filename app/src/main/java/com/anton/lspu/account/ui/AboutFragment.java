package com.anton.lspu.account.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anton.lspu.account.BuildConfig;
import com.anton.lspu.account.R;


public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        TextView versionText = root.findViewById(R.id.versiontext);

        String text = getResources().getString(R.string.version) + ": " + BuildConfig.VERSION_NAME;
        versionText.setText(text);

        return root;
    }
}