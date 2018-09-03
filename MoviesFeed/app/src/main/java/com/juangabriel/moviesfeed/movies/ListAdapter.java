package com.juangabriel.moviesfeed.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangabriel.moviesfeed.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {

    private List<ViewModel> list;

    public ListAdapter(List<ViewModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder listItemViewholder, int i) {
        listItemViewholder.itemName.setText(list.get(i).getName());
        listItemViewholder.countryName.setText(list.get(i).getCountry());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_fragment_title)
        public TextView itemName;

        @BindView(R.id.text_view_fragment_country)
        public TextView countryName;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
