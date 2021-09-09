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

// define adapter and viewholder
class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private static final String TAG = "RestaurantsAdapter";

    private ArrayList<RestaurantsModel> restaurants;
    private Context context;

    public RestaurantsAdapter(Context context, ArrayList<RestaurantsModel> restaurants) {
        this.restaurants = restaurants;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // components for each row
        private LinearLayout resto_item;
        private TextView resto_name, resto_rating, resto_time, resto_loc, resto_desc;
        private ImageView resto_photo;

        // constructor
        public ViewHolder(final View view) {
            super(view);

            resto_name = view.findViewById(R.id.resto_name);
            resto_rating = view.findViewById(R.id.resto_rating);
            resto_time = view.findViewById(R.id.resto_time);
            //location

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
       String resto_name = restaurants.get(position).getRestoName();
       holder.resto_name.setText(resto_name);

       String resto_rating = restaurants.get(position).getOverallRating();
       holder.resto_rating.setText(resto_rating);

       String resto_time = restaurants.get(position).getOpenHours();
       holder.resto_time.setText(resto_time);

       String resto_desc = restaurants.get(position).getRestoDescription();
       holder.resto_desc.setText(resto_desc);

        //location

//       String resto_photo = restaurants.get(position).getRestoPhoto();
//       holder.resto_photo.setImageURI(resto_photo);

       //pass data of restaurant item to restaurant page
       holder.resto_item.setOnClickListener(v -> {
            Intent intent = new Intent(this.context, RestaurantPageActivity.class);
            intent.putExtra("restoNameTv", resto_name);
            intent.putExtra("ratingTv", resto_rating);
            intent.putExtra("openHoursTv", resto_time);
            intent.putExtra("descTv", resto_desc);
//            intent.putExtra("distanceTv", resto_loc);
//            intent.putExtra("restoImg", resto_photo);

           this.context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
