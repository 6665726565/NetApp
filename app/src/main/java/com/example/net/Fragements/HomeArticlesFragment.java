package com.example.net.Fragements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Adapters.ArticleFieldAdapter;
import com.example.net.Adapters.ArticlesAdapter;
import com.example.net.Models.Articles;
import com.example.net.Models.Fields;
import com.example.net.R;

import java.util.ArrayList;

public class HomeArticlesFragment extends Fragment {

    private ArrayList<Fields> fields = new ArrayList<>() ;
    private ArrayList<Articles> articles = new ArrayList<>() ;
    private ArticleFieldAdapter articleFieldAdapter;
    private ArticlesAdapter articlesAdapter;
    private RecyclerView fieldsRecycler , articleRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_articles_fragment, container ,false);

        fieldsRecycler = view.findViewById(R.id.field_recycler_view);
        articleRecycler = view.findViewById(R.id.article_recycler_view);

        handlFields();
        handlArticles();

        return view;
    }

    private void handlArticles() {
        Articles firstOne = new Articles();
        firstOne.setDescription("First you are gonna have to choose the programming language that fits you , after that you have to");
        firstOne.setTitle("Your Road Map in Mobile Development");
        firstOne.setReaders("157");

        Articles secondOne = new Articles();
        secondOne.setDescription("First you are gonna have to choose the programming language that fits you , after that you have to");
        secondOne.setTitle("Your Road Map in Mobile Development");
        secondOne.setReaders("001");

        Articles thirdOne = new Articles();
        thirdOne.setDescription("First you are gonna have to choose the programming language that fits you , after that you have to , First you are gonna have to choose the programming language that fits you , after that you have to");
        thirdOne.setTitle("Your Road Map in Mobile Development");
        thirdOne.setReaders("001");

        articles.add(firstOne);
        articles.add(secondOne);
        articles.add(thirdOne);

        articlesAdapter = new ArticlesAdapter(getContext(), articles, new ArticlesAdapter.MyClickInterface() {
            @Override
            public void onItemClicked(View view, int position) {

            }
        });
        articleRecycler.setLayoutManager(new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        articleRecycler.setNestedScrollingEnabled(false);
        articleRecycler.setAdapter(articlesAdapter);
    }

    private void handlFields() {
        Fields mobileDev = new Fields("mobile development");
        Fields webDev = new Fields("web development");
        Fields design = new Fields("design");

        fields.add(mobileDev);
        fields.add(webDev);
        fields.add(design);

        articleFieldAdapter = new ArticleFieldAdapter(getContext(), fields, new ArticleFieldAdapter.MyClickInterface() {
            @Override
            public void onItemClicked(View view, int position) {

            }
        });

        fieldsRecycler.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL , false));
        fieldsRecycler.setAdapter(articleFieldAdapter);
    }
}
