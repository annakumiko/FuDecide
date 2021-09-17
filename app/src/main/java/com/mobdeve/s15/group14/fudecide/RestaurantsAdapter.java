package com.mobdeve.s15.group14.fudecide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

// define adapter and viewholder
class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> implements Filterable {

    private ArrayList<RestaurantDist> restaurants;
    private ArrayList<RestaurantDist> restaurantsFull;
    private Context context;

    public RestaurantsAdapter(Context context, ArrayList<RestaurantDist> restaurants) {
        this.restaurants = restaurants;
        this.context = context;

        restaurantsFull = new ArrayList<>(restaurants);  // used for search view
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
            resto_loc = view.findViewById(R.id.resto_loc);
            resto_photo = view.findViewById(R.id.resto_photo);

            resto_item = view.findViewById(R.id.resto_item);
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
       String resto_name = restaurants.get(position).getRestaurant().getRestoName();
       holder.resto_name.setText(resto_name);

       String resto_rating = restaurants.get(position).getRestaurant().getOverallRating();
       holder.resto_rating.setText(resto_rating);

       String resto_time = restaurants.get(position).getRestaurant().getOpenHours();
       holder.resto_time.setText(resto_time);

       String resto_url = restaurants.get(position).getRestaurant().getRestoIcon();
       Picasso.get().load(resto_url).into(holder.resto_photo);

       Float dist = restaurants.get(position).getDistance(); // get distance
       DecimalFormat df2 = new DecimalFormat("#.##"); // limit decimal places to 2
       String resto_loc = df2.format(dist) + "m";
       holder.resto_loc.setText(resto_loc);

        //pass data of restaurant item to restaurant page
        holder.resto_item.setOnClickListener(v -> {
            Intent intent = new Intent(this.context, RestaurantPageActivity.class);
            intent.putExtra("restoNameTv", resto_name);
            intent.putExtra("distanceTv", resto_loc);
            this.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    @Override
    public Filter getFilter() {
        return restaurantFilter;
    }

    private Filter restaurantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RestaurantDist> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) { // if input field is empty
                filteredList.addAll(restaurantsFull);
            } else {
                String key = constraint.toString().toLowerCase().trim();

                for (RestaurantDist resto : restaurantsFull) {
                    if (resto.getRestaurant().getRestoName().toLowerCase().contains(key)) {
                        filteredList.add(resto);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurants.clear();
            restaurants.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
