package com.example.net.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.Articles;
import com.example.net.R;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder> {
    Context context ;
    ArrayList<Articles> articles ;
    MyClickInterface myClickInterface;

    public ArticlesAdapter(Context context, ArrayList<Articles> articles, MyClickInterface myClickInterface) {
        this.context = context;
        this.articles = articles;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_holder , parent , false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        holder.title.setText(articles.get(position).getTitle());
        holder.description.setText(articles.get(position).getDescription());
        holder.readers.setText(articles.get(position).getReaders());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleHolder extends RecyclerView.ViewHolder {

        TextView title , description , readers  ;
        ImageButton saveIc;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            description = itemView.findViewById(R.id.article_description);
            readers = itemView.findViewById(R.id.readers_number);
            saveIc = itemView.findViewById(R.id.save_ic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClickInterface.onItemClicked(itemView , getAdapterPosition());
                }
            });

        }
    }

    public interface MyClickInterface{

        void onItemClicked(View view , int position);
    }
}
