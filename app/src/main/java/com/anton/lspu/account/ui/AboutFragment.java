package com.anton.lspu.account.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anton.lspu.account.BuildConfig;
import com.anton.lspu.account.R;


public class AboutFragment extends Fragment {

    ImageView CoatOfArms;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_about, container, false);

        CoatOfArms = root.findViewById(R.id.coat_of_arms_about);

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            CoatOfArms.setImageResource(R.drawable.coatofarms_lspu_blue);
        }
        else CoatOfArms.setImageResource(R.drawable.coatofarms_lspu_white);

        TextView versionText = root.findViewById(R.id.versiontext);

        String text = getResources().getString(R.string.version) + ": " + BuildConfig.VERSION_NAME;
        versionText.setText(text);

        return root;
    }



}