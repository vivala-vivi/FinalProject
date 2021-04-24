package com.example.vtradeversion3;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

  List<NewsData> newsDataList;
  public NewsAdapter(List<NewsData> newsDataList){
      this.newsDataList = newsDataList;
  }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_rows,parent,false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsData newsData = newsDataList.get(position);
        holder.title.setText(newsData.title);
        holder.description.setText(newsData.description);
        holder.url = newsData.url;
        Picasso.get().load(newsData.urlToImage).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

      TextView title, description;
      ImageView imageView;
      LinearLayout parent;
      String url;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            parent= itemView.findViewById(R.id.parent);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(itemView.getContext(), WebpageActivity.class);
                   intent.putExtra("url",url);
                 itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
