package com.example.net.Fragements;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Adapters.PostsAdapter;
import com.example.net.Models.Posts;
import com.example.net.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BeginnerProfilePostFragment extends Fragment {

    private FirebaseAuth mAuth ;
    private DatabaseReference postRef , likesRef , savedRef;
    private String currentUserId;
    private ArrayList<Posts> posts = new ArrayList<>();
    private PostsAdapter postsAdapter;
    private RecyclerView postRecycler;
    private Boolean likeClicked , saveClicked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beginner_profile_post_fragment_layout, container ,false);

        mAuth = FirebaseAuth.getInstance();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        savedRef = FirebaseDatabase.getInstance().getReference().child("Saved");
        postRecycler = view.findViewById(R.id.beginner_my_posts_recycler);

        handlPosts();

        return view;
    }

    private void handlPosts() {

        currentUserId = mAuth.getCurrentUser().getUid();
        savedRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                if (snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            posts.add(0,dataSnapshot.getValue(Posts.class));
                    }

                    postsAdapter = new PostsAdapter(getActivity(), posts, new PostsAdapter.MyClickInterface() {
                        @Override
                        public void onItemClicked(View view, int position) {

                        }

                        @Override
                        public void onLikeClicked(View view, int position) {
                            likeClicked = true;

                            Log.d("clicked", "onLikeClicked: clicked");
                            String key = posts.get(position).getKey();
                            likesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (likeClicked.equals(true)) {
                                        if (snapshot.child(key).hasChild(currentUserId)) {
                                            likesRef.child(key).child(currentUserId).removeValue();
                                            posts.get(position).setLikesNumber(Integer.toString((int) snapshot.child(key).getChildrenCount()));
                                            likeClicked = false;
                                        } else {
                                            likesRef.child(key).child(currentUserId).setValue(true);
                                            posts.get(position).setLikesNumber(Integer.toString((int) snapshot.child(key).getChildrenCount()));
                                            likeClicked = false;
                                        }
                                    }

//
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                        }

                        @Override
                        public void onCommentsClicked(View view, int position) {
                            String key = posts.get(position).getKey();
                            CommentsBottomSheetFragment commentsBottomSheetFragment = new CommentsBottomSheetFragment(getActivity() , key);
                            commentsBottomSheetFragment.show(getActivity().getSupportFragmentManager() , commentsBottomSheetFragment.getTag());
                        }

                        @Override
                        public void onSaveClicked(View view, int position) {
                            saveClicked = true;

                            String key = posts.get(position).getKey();

                            savedRef.addValueEventListener(new ValueEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (saveClicked.equals(true)){
                                        if (snapshot.child(currentUserId).hasChild(key)){
                                            posts.remove(position);
                                            savedRef.child(currentUserId).child(key).removeValue();
                                            saveClicked=false;
                                        }else {
                                            savedRef.child(currentUserId).child(key).setValue(posts.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(), "saved!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            saveClicked = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });


                    postRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    postRecycler.setAdapter(postsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
