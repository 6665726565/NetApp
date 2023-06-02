package com.example.net.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.SearchResult;
import com.example.net.Models.Users;
import com.example.net.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    ArrayList<Users> results ;
    Context context  ;
    MyClickInterface myClickInterface;

    public SearchAdapter(ArrayList<Users> results, Context context , MyClickInterface myClickInterface) {
        this.results = results;
        this.context = context;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_holder , parent , false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {

        holder.userName.setText(results.get(position).getName());
        holder.userField.setText(results.get(position).getFields());

        if (!TextUtils.isEmpty(results.get(position).getProfileImg())){
            Picasso.get().load(results.get(position).getProfileImg()).error(R.color.white).placeholder(R.color.white).into(holder.profileImage);
        }

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        private ImageView profileImage ;
        private ImageButton arrowImage;
        private TextView userName , userField;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.search_profile_picture);
            arrowImage = itemView.findViewById(R.id.search_arrow);
            userName = itemView.findViewById(R.id.search_user_name);
            userField = itemView.findViewById(R.id.search_field);

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
        void onLongItemClicked(int notePosition ,View view);
    }
}
