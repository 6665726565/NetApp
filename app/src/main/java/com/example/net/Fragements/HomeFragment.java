package com.example.net.Fragements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.net.HomeActivity;
import com.example.net.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container ,false);


        replaceFragment(new HomePostFragment());

        if (HomeActivity.value == "Posts"){
            replaceFragment(new HomePostFragment());
        }else {
            replaceFragment(new HomeArticlesFragment());
        }


        return view;
    }


    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container , fragment);
        transaction.commit();

    }
}
