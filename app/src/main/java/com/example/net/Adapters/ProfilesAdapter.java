package com.example.net.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Models.Users;
import com.example.net.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfilesHolder> {
    Context context;
    ArrayList<Users> profiles;
    MyClickInterface myClickInterface;

    public ProfilesAdapter(Context context, ArrayList<Users> profiles, MyClickInterface myClickInterface) {
        this.context = context;
        this.profiles = profiles;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ProfilesAdapter.ProfilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_holder , parent , false);
        return new ProfilesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilesAdapter.ProfilesHolder holder, int position) {

        holder.userName.setText(profiles.get(position).getName());
        if (!TextUtils.isEmpty(profiles.get(position).getProfileImg())){
            Picasso.get().load(profiles.get(position).getProfileImg()).error(R.color.white).placeholder(R.color.white).into(holder.profileImage);
        }

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ProfilesHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userName;

        public ProfilesHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_picture);
            userName = itemView.findViewById(R.id.profile_user_name);

        }
    }

    public interface MyClickInterface{

        void onItemClicked(View view , int position);
    }
}
