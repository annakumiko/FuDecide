package com.mobdeve.s15.group14.fudecide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RestViewHolder extends RecyclerView.ViewHolder {
    public TextView restName;
    public TextView desc;

    public RestViewHolder(@NonNull View ItemView) {
        super(ItemView);
        restName = ItemView.findViewById(R.id.restName);
        desc = ItemView.findViewById(R.id.desc);
    }

}
