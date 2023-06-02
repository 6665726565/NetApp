package com.example.net.Fragements;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.net.ChoosingFieldsActivity;
import com.example.net.EditProfileActivity;
import com.example.net.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileProfessFragment extends Fragment implements View.OnClickListener{

    private static final int PAGE_NUM = 2;
    private static final int Gallery_Pick =1;
    private Uri ImageUri;
    private StorageReference postImageRef;
    private FirebaseAuth mAuth;
    private String currentUserId , downloadUrl;
    private DatabaseReference userRef ;
    private TextView userNameTxt , fieldsTxt , bioTxt , edtProfileTxt;
    private CircleImageView profileImage;

    private ColorStateList def;
    private TextView item1Txt, item2Txt , select ;
    private ImageView item1Img, item2Img;
    private ImageButton sheetBtn;

    private FrameLayout item1FrameLayout , item2FrameLayout , selectFrame ;

    private ViewPager2 professProfileViewPager;
    private FragmentStateAdapter pagerAdapter;

    private ProgressDialog progressDialog;

    private TextView switchTxt;
    private Switch aSwitch;
    private Button switchBtn  ;
    private CheckBox mobileDev , webDev , designer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_profess_fragment, container ,false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        postImageRef = FirebaseStorage.getInstance().getReference().child("Profile_Picture").child(currentUserId);

        progressDialog = new ProgressDialog(getActivity());

        userNameTxt = view.findViewById(R.id.user_name_profile);
        fieldsTxt = view.findViewById(R.id.field_txt);
        bioTxt = view.findViewById(R.id.bio_txt);
        edtProfileTxt = view.findViewById(R.id.edt_profile_txt_profess);

        sheetBtn = view.findViewById(R.id.sheet_btn);

        profileImage = view.findViewById(R.id.profile_image_profess);

        item1Txt = view.findViewById(R.id.item1_txt);
        item2Txt = view.findViewById(R.id.item2_txt);
        select = view.findViewById(R.id.select);

        item1Img = view.findViewById(R.id.item1_img);
        item2Img = view.findViewById(R.id.item2_img);

        item1FrameLayout = view.findViewById(R.id.item1_frame_layout);
        item2FrameLayout = view.findViewById(R.id.item2_frame_layout);
        selectFrame = view.findViewById(R.id.select_frame);

        professProfileViewPager = view.findViewById(R.id.profess_profile_viewpager);

        item1Txt.setOnClickListener(this);
        item2Txt.setOnClickListener(this);

        def = item2Txt.getTextColors();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OppenGallery();
            }
        });

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


        sheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userName = snapshot.child("name").getValue().toString();
                    String fieldTxt = snapshot.child("fields").getValue().toString();

                    if (snapshot.hasChild("profileImg")){
                        Picasso.get().load(snapshot.child("profileImg").getValue().toString()).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(profileImage);
                    }


                    if(snapshot.child("bio").exists()){
                        String bio = snapshot.child("bio").getValue().toString();

                        if (!TextUtils.isEmpty(bio)) {
                            bioTxt.setText(bio);
                        }else{
                            bioTxt.setText("BIO");
                        }
                    }


                    userNameTxt.setText(userName);
                    fieldsTxt.setText(fieldTxt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edtProfileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToEditProfileActivity();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.item1_txt:
                postItemSelected();
                professProfileViewPager.setCurrentItem(0);
                break;

            case R.id.item2_txt:
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

    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profess_profile_container , fragment);
        transaction.commit();

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

    private void sendUserToEditProfileActivity() {
        Intent intent = new Intent(getActivity() , EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity() , R.style.DialogSheetBottomTheme);
        dialog.setContentView(R.layout.switch_sheet_dialog);

         switchBtn= dialog.findViewById(R.id.switch_submit);
         aSwitch = dialog.findViewById(R.id.switch_icon);
         switchTxt = dialog.findViewById(R.id.switch_txt);
         mobileDev = dialog.findViewById(R.id.mobile_dev);
         webDev = dialog.findViewById(R.id.web_dev);
         designer = dialog.findViewById(R.id.designer);

         switchTxt.setText("Switch to Beginner mode");

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();

                String fields = "" ;

                if (mobileDev.isChecked()){
                    if (fields == ""){
                        fields = fields + "Mobile developer ";
                    }else{
                        fields = fields + "| Mobile developer ";
                    }

                }
                if (webDev.isChecked()){
                    if (fields == ""){
                        fields = fields + "Web developer ";
                    }else{
                        fields = fields + "| Web developer ";
                    }
                }
                if (designer.isChecked()){
                    if (fields == ""){
                        fields = fields + "Designer ";
                    }else{
                        fields = fields + "| Designer ";
                    }
                }
                hashMap.put("fields", fields);
                if (!TextUtils.isEmpty(fields)){
                    userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                        }
                    });
                }

                if (aSwitch.isChecked()) {

                    if (!(mobileDev.isChecked() || webDev.isChecked() || designer.isChecked())) {
                        Toast.makeText(getActivity(), "Please select your field", Toast.LENGTH_SHORT).show();
                    }else {

                        hashMap.put("isProfess", false);

                        userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });
                        dialog.dismiss();
                    }

                }

            }
        });



        dialog.show();
    }


    private void OppenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , Gallery_Pick);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            profileImage.setImageURI(ImageUri);
            ValidateImageInfo();
        }
    }


    private void ValidateImageInfo() {
        if (profileImage == null){
            Toast.makeText(getActivity(), "you have to choose a pic", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("wait...");
            progressDialog.setMessage("uploading image ..");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);


            StoringImageToFireBaseStorage();

        }
    }

    private void StoringImageToFireBaseStorage() {

        StorageReference filPath = postImageRef.child(ImageUri.getLastPathSegment() + ".jpg");
        filPath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){


                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUrl = task.getResult().toString();
                                Toast.makeText(getActivity(), "successful!", Toast.LENGTH_SHORT).show();
                                SavingImageInformationToDataBase();


                            }else{
                                Toast.makeText(getActivity(), "Problem Occured : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "eurror occured  : " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SavingImageInformationToDataBase() {
        HashMap hashMap = new HashMap();

        hashMap.put("profileImg" , downloadUrl);

        userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(getActivity(), "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                }
            }

        });
    }
}
