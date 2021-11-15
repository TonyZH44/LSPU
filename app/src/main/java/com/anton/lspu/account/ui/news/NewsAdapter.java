package com.anton.lspu.account.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.anton.lspu.account.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    List<Article> articles;

    public NewsAdapter(Context context, List<Article> articles){
        this.context = context;
        this.articles = articles;
    }

    @NotNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.titleText.setText(articles.get(position).getTitle());
        holder.dateText.setText(articles.get(position).getDate());
        holder.descriptionText.setText(articles.get(position).getDescription());

        Bitmap bitmap = BitmapFactory.decodeByteArray(articles.get(position).getImageBinaryData(),
                0,
                articles.get(position).getImageBinaryData().length);


        holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                265,
                177,
                false));

        int finalPosition = position;
        holder.mainConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                //builder.setUrlBarHidingEnabled(true);
                //CustomTabsIntent customTabsIntent = builder.build();
                //customTabsIntent.launchUrl(context, Uri.parse(articles.get(position).getUrl()));

                Intent newsIntent = new Intent(context, NewsActivity.class);
                newsIntent.putExtra("articleURL", articles.get(finalPosition).getUrl());
                context.startActivity(newsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, descriptionText, dateText;
        ImageView imageView;
        ConstraintLayout mainConstraintLayout;

        public NewsViewHolder(@NotNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.article_title);
            descriptionText = itemView.findViewById(R.id.article_description);
            dateText = itemView.findViewById(R.id.article_date);
            imageView = itemView.findViewById(R.id.coat_of_arms);
            mainConstraintLayout = itemView.findViewById(R.id.article_mainConstraintLayout);

        }
    }

}