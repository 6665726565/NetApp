package com.example.net.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.Fields;
import com.example.net.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ArticleFieldAdapter extends RecyclerView.Adapter<ArticleFieldAdapter.ArticleFieldHolder> {
    Context context ;
    ArrayList<Fields> fields ;
    MyClickInterface myClickInterface;
    int selectedItemPos = 0;

    public ArticleFieldAdapter(Context context, ArrayList<Fields> fields, MyClickInterface myClickInterface) {
        this.context = context;
        this.fields = fields;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ArticleFieldAdapter.ArticleFieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_field_holder , parent , false);
        return new ArticleFieldHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ArticleFieldAdapter.ArticleFieldHolder holder, int position) {

        holder.fieldTxt.setText(fields.get(position).getName());

        if (selectedItemPos == position){
            holder.fieldCard.setCardBackgroundColor(context.getColor(R.color.secondary_color));
            holder.fieldTxt.setTextColor(context.getColor(R.color.white));
        }else {
            holder.fieldCard.setCardBackgroundColor(context.getColor(R.color.white));
            holder.fieldTxt.setTextColor(context.getColor(R.color.text_color));
        }

    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public class ArticleFieldHolder extends RecyclerView.ViewHolder {

        TextView fieldTxt;
        CardView fieldCard;

        public ArticleFieldHolder(@NonNull View itemView) {
            super(itemView);

            fieldTxt = itemView.findViewById(R.id.field_txt);
            fieldCard = itemView.findViewById(R.id.field_article_holder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItemPos = getAdapterPosition();
                    myClickInterface.onItemClicked(view , getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }


    }

    public interface MyClickInterface{

        void onItemClicked(View view , int position);
    }
}
