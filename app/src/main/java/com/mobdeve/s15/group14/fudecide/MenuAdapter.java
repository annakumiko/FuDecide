package com.mobdeve.s15.group14.fudecide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private static final String TAG = "MenuAdapter";

    private ArrayList<MenuModel> menuItems;
    private Context context;

    public MenuAdapter(Context context, ArrayList<MenuModel> menuItems){
        this.menuItems = menuItems;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout menu_item;
        private TextView itemName, itemPrice;
        private ImageView itemPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemPhoto = itemView.findViewById(R.id.itemPhoto);

            menu_item = itemView.findViewById(R.id.menuItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.menu_item,
                parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemName = menuItems.get(position).getItemName();
        holder.itemName.setText(itemName);

        String itemPrice = menuItems.get(position).getItemPrice();
        holder.itemPrice.setText("P " + itemPrice);

        String photo_url = menuItems.get(position).getItemPhoto();
        Picasso.get().load(photo_url).into(holder.itemPhoto);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
