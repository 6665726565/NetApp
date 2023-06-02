package com.example.net.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.Comments;
import com.example.net.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {
    Context context ;
    ArrayList<Comments> comments ;

    public CommentsAdapter(Context context, ArrayList<Comments> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_holder , parent , false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {

        holder.commentContent.setText(comments.get(position).getComment());
        holder.userName.setText(comments.get(position).getUserName());

        if (!TextUtils.isEmpty(comments.get(position).getProfileImg())){
            Picasso.get().load(comments.get(position).getProfileImg()).error(R.color.white).placeholder(R.color.white).into(holder.profileImg);
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImg ;
        TextView userName , commentContent;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.comments_user_img);
            userName = itemView.findViewById(R.id.comments_user_name);
            commentContent = itemView.findViewById(R.id.comments_content);


        }
    }
}
