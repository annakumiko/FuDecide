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

    private ArrayList<RestaurantsModel> resto_data;

    public RestaurantsAdapter(ArrayList<RestaurantsModel> restaurants) {
        this.resto_data = resto_data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // components for each row
        private LinearLayout resto_item;
        private TextView resto_name, resto_rating, resto_time, resto_loc;
        private ImageView resto_photo;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            resto_item = (LinearLayout) itemView.findViewById(R.id.resto_item);
            resto_photo = (ImageView) itemView.findViewById(R.id.resto_photo);
            resto_name = (TextView) itemView.findViewById(R.id.resto_name);
            resto_rating = (TextView) itemView.findViewById(R.id.resto_rating);
            resto_time = (TextView) itemView.findViewById(R.id.resto_time);
            resto_loc = (TextView) itemView.findViewById(R.id.resto_loc);
        }

        // setters
        //public void setRestoPhoto(String r_photo) { this.resto_photo.setText(r_photo); }
        public void setRestoName(String r_name) { this.resto_name.setText(r_name); }
        public void setRestoRating(int r_rating) { this.resto_rating.setText(r_rating); }
        //public void setRestoTime(String r_time) { this.resto_name.setText(r_time);}
        //public void setRestoLoc(String r_loc) { this.resto_name.setText(r_loc);}
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
       // holder.setRestoPhoto.setImageURI(resto_data.get(position).uri);
        holder.setRestoName(resto_data.get(position).getRestoName());
        holder.setRestoRating(resto_data.get(position).getOverallRating());
       // holder.setRestoTime(resto_data.get(position).getRestoName());
       // holder.setRestoLoc(resto_data.get(position).getRestoName());
    }

    @Override
    public int getItemCount() {
        return resto_data.size();
    }


}
