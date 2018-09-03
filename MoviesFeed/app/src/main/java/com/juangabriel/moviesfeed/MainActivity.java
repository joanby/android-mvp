package com.juangabriel.moviesfeed;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;


import com.juangabriel.moviesfeed.movies.ListAdapter;
import com.juangabriel.moviesfeed.movies.MoviesMVP;
import com.juangabriel.moviesfeed.movies.ViewModel;
import com.juangabriel.moviesfeed.root.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesMVP.View{

    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.activity_root_view)
    ViewGroup rootView;

    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;

    @Inject
    MoviesMVP.Presenter presenter;

    private ListAdapter listAdapter;
    private List<ViewModel> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        ((App) getApplication()).getComponent().inject(this);

        listAdapter = new ListAdapter(resultList);

        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.rxJavaUnsuscribe();
        resultList.clear();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(ViewModel viewModel) {
        resultList.add(viewModel);
        listAdapter.notifyItemInserted(resultList.size()-1);
        Log.d(TAG, "Información nueva: "+viewModel.getName());
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}