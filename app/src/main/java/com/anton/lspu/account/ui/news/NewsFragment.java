package com.anton.lspu.account.ui.news;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anton.lspu.account.CookieViewModel;
import com.anton.lspu.account.DrawerActivity;
import com.anton.lspu.account.R;

import java.net.HttpURLConnection;

public class NewsFragment extends Fragment {

    public RecyclerView newslist;

    private NewsViewModel newsViewModel;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        newslist = root.findViewById(R.id.newslist);
        NewsAdapter newsAdapter = new NewsAdapter(getContext(), newsViewModel.getArticles());
        newslist.setAdapter(newsAdapter);
        newslist.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }




}