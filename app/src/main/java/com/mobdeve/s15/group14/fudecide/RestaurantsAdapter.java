package com.mobdeve.s15.group14.fudecide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// define adapter and viewholder
class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private ArrayList<RestaurantsModel> restaurants;

    public RestaurantsAdapter(ArrayList<RestaurantsModel> restaurants) {
        this.restaurants = restaurants;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // components for each row
        private LinearLayout resto_item;
        private TextView resto_name, resto_rating, resto_time, resto_loc;
        private ImageView resto_photo;

        // constructor
        public ViewHolder(final View view) {
            super(view);

            resto_name = view.findViewById(R.id.resto_name);
            resto_rating = view.findViewById(R.id.resto_rating);
            resto_time = view.findViewById(R.id.resto_time);
        }

    }

    // 3 methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.restaurant_item,
                parent, false);
        ViewHolder myViewHolder = new ViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String resto_name = restaurants.get(position).getRestoName();
       holder.resto_name.setText(resto_name);

       String resto_rating = restaurants.get(position).getOverallRating();
       holder.resto_rating.setText(resto_rating);

       String resto_time = restaurants.get(position).getOpenHours();
       holder.resto_time.setText(resto_time);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
