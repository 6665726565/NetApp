package com.example.net;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.OnBoarding.OnBoardingItem;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingItemViewHolder> {
    List<OnBoardingItem> onBoardingItems;
    Context context;

    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItem , Context context) {
        this.onBoardingItems = onBoardingItem;
        this.context = context;
    }

    @NonNull
    @Override
    public OnBoardingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.onboard_countainer,parent,false);
        return new OnBoardingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingItemViewHolder holder, int position) {
        holder.setOnBoardingData(onBoardingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }


    public class OnBoardingItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle ;
        TextView txtDescription;
        ImageView onBoardingImg;

        public OnBoardingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            onBoardingImg = itemView.findViewById(R.id.onBoardingImg);
        }

        void setOnBoardingData(OnBoardingItem onBoardingItem){
            txtTitle.setText(onBoardingItem.getTitle());
            txtDescription.setText(onBoardingItem.getDescription());
            onBoardingImg.setImageResource(onBoardingItem.getImage());
        }
    }
}
