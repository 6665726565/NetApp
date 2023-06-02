package com.example.net.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.Posts;
import com.example.net.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kr.co.prnd.readmore.ReadMoreTextView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHolder> {
    Context context ;
    ArrayList<Posts> posts ;
    MyClickInterface myClickInterface;
    boolean isLiked;

    public PostsAdapter(Context context, ArrayList<Posts> posts, MyClickInterface myClickInterface) {
        this.context = context;
        this.posts = posts;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public PostsAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_holder , parent ,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.PostHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.userName.setText(posts.get(position).getUserName());
        holder.userField.setText(posts.get(position).getuField());

        holder.likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(posts.get(position).getKey()).hasChild(holder.currentUserId)){
                    holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_filled);
                    holder.likeCounter = (int) snapshot.child(posts.get(position).getKey()).getChildrenCount();
                    holder.likeNumber.setText(Integer.toString(holder.likeCounter));
                }else {
                    holder.likeBtn.setBackgroundResource(R.drawable.ic_heart);
                    holder.likeCounter = (int) snapshot.child(posts.get(position).getKey()).getChildrenCount();
                    holder.likeNumber.setText(Integer.toString(holder.likeCounter));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.commentCounter = (int) snapshot.child(posts.get(position).getKey()).getChildrenCount();
                    holder.commentsNumber.setText(Integer.toString(holder.commentCounter));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.savedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(holder.currentUserId).hasChild(posts.get(position).getKey())){
                    holder.saveBtn.setBackgroundResource(R.drawable.ic_save_filled);
                }else {
                    holder.saveBtn.setBackgroundResource(R.drawable.ic_save_bookmark);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (TextUtils.isEmpty(posts.get(position).getPostDescription())){
            holder.postDescription.setVisibility(View.GONE);
        }else {
            holder.postDescription.setVisibility(View.VISIBLE);
            holder.postDescription.setText(posts.get(position).getPostDescription());
        }

        if (!TextUtils.isEmpty(posts.get(position).getUserProfileImg())){
            Picasso.get().load(posts.get(position).getUserProfileImg()).error(R.color.white).placeholder(R.color.white).into(holder.userProfileImg);
        }

        if (!TextUtils.isEmpty(posts.get(position).getPostImg())){
            holder.postHolderCardView.setVisibility(View.VISIBLE);
            Picasso.get().load(posts.get(position).getPostImg()).error(R.color.white).placeholder(R.color.white).into(holder.postImg);
        }else {
            holder.postHolderCardView.setVisibility(View.GONE);
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickInterface.onLikeClicked(view , position);

            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickInterface.onCommentsClicked(view , position);
            }
        });

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickInterface.onSaveClicked(view , position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        CardView postHolderCardView;
        ImageView userProfileImg , likeBtn , commentBtn , sendMsgBtn , saveBtn  ;
        TextView userName , userField , likeNumber , commentsNumber;
        ReadMoreTextView postDescription;
        ImageView postImg;
        DatabaseReference likesRef , commentsRef , savedRef;
        String currentUserId;
        int likeCounter , commentCounter;
        boolean isLiked , isSaved ;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
            savedRef = FirebaseDatabase.getInstance().getReference().child("Saved");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            userProfileImg = itemView.findViewById(R.id.profile_picture_post);
            likeBtn = itemView.findViewById(R.id.like_btn);
            commentBtn = itemView.findViewById(R.id.cmnts_btn);
            sendMsgBtn = itemView.findViewById(R.id.share_btn);
            saveBtn= itemView.findViewById(R.id.save_btn);
            userField = itemView.findViewById(R.id.post_user_field);
            userName = itemView.findViewById(R.id.post_user_name);
            postImg = itemView.findViewById(R.id.post_img);
            postDescription = itemView.findViewById(R.id.post_description);
            postHolderCardView = itemView.findViewById(R.id.post_holder_card_view);
            likeNumber = itemView.findViewById(R.id.likes_number);
            commentsNumber = itemView.findViewById(R.id.comments_number);




            isSaved = false;





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClickInterface.onItemClicked(view , getAdapterPosition());
                }
            });



        }


    }

    public interface MyClickInterface{

        void onItemClicked(View view , int position);
        void onLikeClicked(View view , int position);
        void onCommentsClicked(View view , int position);
        void onSaveClicked(View view , int position);
    }
}
