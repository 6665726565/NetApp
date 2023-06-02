package com.example.net.Fragements;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.net.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfessProfileResult extends Fragment implements View.OnClickListener {

    private static final int PAGE_NUM = 2;
    private ViewPager2 professProfileViewPager;
    private FragmentStateAdapter pagerAdapter;

    private ColorStateList def;
    private FirebaseAuth mAuth ;
    private String currentUserId;
    private DatabaseReference userRef;
    private CircleImageView profileImage ;
    private TextView userName , userFields , userBio ,
            messageTxt , item1Txt , item2Txt ;

    private ImageView item1Img , item2Img;
    private FrameLayout item1FrameLayout , item2FrameLayout , selectFrame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profess_profile_result, container ,false);

        profileImage = view.findViewById(R.id.profile_result_image_profess);
        userName = view.findViewById(R.id.result_user_name_profile);
        userFields = view.findViewById(R.id.result_field_txt);
        userBio = view.findViewById(R.id.result_bio_txt);
        messageTxt = view.findViewById(R.id.message_txt_profess);


        item1Txt = view.findViewById(R.id.item1_txt_result);
        item2Txt = view.findViewById(R.id.item2_txt_result);


        item1Img = view.findViewById(R.id.item1_img_result);
        item2Img = view.findViewById(R.id.item2_img_result);

        item1FrameLayout = view.findViewById(R.id.item1_frame_layout_result);
        item2FrameLayout = view.findViewById(R.id.item2_frame_layout_result);
        selectFrame = view.findViewById(R.id.select_frame_result);

        item1Txt.setOnClickListener(this);
        item2Txt.setOnClickListener(this);

        def = item2Txt.getTextColors();

        professProfileViewPager = view.findViewById(R.id.profess_result_profile_viewpager);
        pagerAdapter = new ScreenSlidePageAdapter(getActivity());
        professProfileViewPager.setAdapter(pagerAdapter);

        professProfileViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position ==0){
                    postItemSelected();
                }else {
                    articleItemSelected();
                }

            }
        });

        String uid = getArguments().getString("uid");

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String field = snapshot.child("fields").getValue().toString();

                    userName.setText(name);
                    userFields.setText(field);

                    if (snapshot.hasChild("profileImg")){
                        Picasso.get().load(snapshot.child("profileImg").getValue().toString()).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(profileImage);
                    }

                    if(snapshot.child("bio").exists()){
                        String bio = snapshot.child("bio").getValue().toString();
                        if (!TextUtils.isEmpty(bio)) {
                            userBio.setText(bio);
                        }else{
                            userBio.setText("");
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item1_txt_result:

                postItemSelected();
                professProfileViewPager.setCurrentItem(0);
                break;

            case R.id.item2_txt_result:
                articleItemSelected();
                professProfileViewPager.setCurrentItem(1);
                break;
        }
    }


    private void articleItemSelected() {
        float size = (float) (item1FrameLayout.getWidth() + convertDpToPixel(10,getActivity()));
        selectFrame.animate().x(size).setDuration(100);
        item1Txt.setTextColor(def);
        item2Txt.setTextColor(getResources().getColor(R.color.white));
        item1Img.setBackgroundResource(R.drawable.ic_post_filled_icon);
        item2Img.setBackgroundResource(R.drawable.ic_articles_filled_icon);
    }

    private void postItemSelected() {
        selectFrame.animate().x(0).setDuration(100);
        item1Txt.setTextColor(getResources().getColor(R.color.white));
        item2Txt.setTextColor(def);
        item1Img.setBackgroundResource(R.drawable.ic_post_icon);
        item2Img.setBackgroundResource(R.drawable.ic_articles_icon);
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {

        public ScreenSlidePageAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0 :
                    return new ProfessProfilePostFragment();
                case 1:
                    return new ProfessProfileArticleFragment();
            }


            return null;
        }

        @Override
        public int getItemCount() {
            return PAGE_NUM;
        }
    }
}
