package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RestViewHolder extends RecyclerView.ViewHolder {
    public TextView resto_name, resto_rating, resto_time, resto_loc;

    public RestViewHolder(@NonNull View ItemView) {
        super(ItemView);
        resto_name = ItemView.findViewById(R.id.resto_name);
        resto_rating = ItemView.findViewById(R.id.resto_rating);
        resto_time = ItemView.findViewById(R.id.resto_time);
        resto_loc = ItemView.findViewById(R.id.resto_loc);
    }

}
