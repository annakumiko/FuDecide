package com.mobdeve.s15.group14.fudecide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {


    private ArrayList<RestaurantsModel> restaurants;
    private Context context;

    public FavoritesAdapter(Context context, ArrayList<RestaurantsModel> restaurants) {
        this.restaurants = restaurants;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // components for each row
        private LinearLayout resto_item;
        private TextView resto_name, resto_rating, resto_time, resto_loc;

        // constructor
        public ViewHolder(final View view) {
            super(view);

            resto_name = view.findViewById(R.id.resto_name);
            resto_rating = view.findViewById(R.id.resto_rating);
            resto_time = view.findViewById(R.id.resto_time);
            resto_loc = view.findViewById(R.id.resto_loc);

            resto_item = view.findViewById(R.id.resto_item);
        }

    }

    // 3 methods
    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.restaurant_item,
                parent, false);
        FavoritesAdapter.ViewHolder myViewHolder = new FavoritesAdapter.ViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        String resto_name = restaurants.get(position).getRestoName();
        holder.resto_name.setText(resto_name);

        String resto_rating = restaurants.get(position).getOverallRating();
        holder.resto_rating.setText(resto_rating);

        String resto_time = restaurants.get(position).getOpenHours();
        holder.resto_time.setText(resto_time);

        //location
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}

