package com.example.net.Fragements;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Adapters.PostsAdapter;
import com.example.net.Adapters.ProfilesAdapter;
import com.example.net.Adding.AddPostActivity;
import com.example.net.Models.Posts;
import com.example.net.Models.Users;
import com.example.net.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePostFragment extends Fragment {

    private FirebaseAuth mAuth ;
    private DatabaseReference userRef ,postRef , likesRef , savedRef;
    private String currentUserId;

    private RecyclerView profileRecycler , postRecycler;
    private ArrayList<Users> profiles = new ArrayList<>();
    private ArrayList<Posts> posts = new ArrayList<>();
    private ProfilesAdapter profilesAdapter ;
    private PostsAdapter postsAdapter ;
    private FloatingActionButton addPostBtn;
    private NestedScrollView postScrollView;
    private int likes =0;
    private HashMap addLike = new HashMap();
    private Boolean likeClicked , saveClicked;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_post_fragment, container ,false);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        savedRef = FirebaseDatabase.getInstance().getReference().child("Saved");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        postScrollView = view.findViewById(R.id.post_scroll_view);
        profileRecycler = view.findViewById(R.id.profile_recycler_view);
        postRecycler = view.findViewById(R.id.posts_recycler_view);
        addPostBtn = view.findViewById(R.id.add_post_btn);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToAddPostActivity();
            }
        });

        handlProfiles();
        handlPosts();

        handlHidingOnScroll();

        return view;
    }

    private void sendUserToAddPostActivity() {
        Intent intent = new Intent(getActivity() , AddPostActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void handlPosts() {

        postRef.addValueEventListener(new ValueEventListener() {
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

                                        if (likeClicked.equals(true)){
                                            if (snapshot.child(key).hasChild(currentUserId)){
                                                likesRef.child(key).child(currentUserId).removeValue();
                                                posts.get(position).setLikesNumber(Integer.toString((int) snapshot.child(key).getChildrenCount()));
                                                likeClicked = false;
                                            }else {
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



//

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
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (saveClicked.equals(true)){
                                        if (snapshot.child(currentUserId).hasChild(key)){
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
                    postRecycler.setNestedScrollingEnabled(false);
                    postRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    postRecycler.setAdapter(postsAdapter);
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handlProfiles() {
        profiles.clear();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren() ) {
                        if (!TextUtils.equals(dataSnapshot.getKey() , currentUserId) ){
                            profiles.add(dataSnapshot.getValue(Users.class));
                        }
                    }
                }

                profilesAdapter = new ProfilesAdapter(getActivity(), profiles, new ProfilesAdapter.MyClickInterface() {
                    @Override
                    public void onItemClicked(View view, int position) {

                    }
                });

                profileRecycler.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL ,false));
                profileRecycler.setAdapter(profilesAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void handlHidingOnScroll(){


        postScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    addPostBtn.hide();
                } else {
                    addPostBtn.show();
                }
            }
        });
    }
}
