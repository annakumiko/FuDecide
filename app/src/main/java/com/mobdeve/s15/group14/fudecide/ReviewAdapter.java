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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private static final String TAG = "ReviewAdapter";

    private ArrayList<ReviewModel> reviews;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout review_item;
        private TextView userName, reviewText, rating, restauName;
        private ImageView editReviewBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            reviewText = itemView.findViewById(R.id.reviewText);
            rating = itemView.findViewById(R.id.rating);
            restauName = itemView.findViewById(R.id.restauName);
            editReviewBtn = itemView.findViewById(R.id.editReviewBtn);

            review_item = itemView.findViewById(R.id.review_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_item,
                parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uname = reviews.get(position).getName();
        holder.userName.setText(uname);

        String reviewTxt = reviews.get(position).getReviewText();
        holder.reviewText.setText(reviewTxt);

        Double rate = reviews.get(position).getRating();
        holder.rating.setText(String.valueOf(rate));

        String restName = reviews.get(position).getRestoName();
        holder.restauName.setText(restName);

        // show edit button if review is from current user

        holder.editReviewBtn.setOnClickListener(v ->{
            Intent intent = new Intent(this.context, EditReviewActivity.class);
            intent.putExtra("name", uname);
            intent.putExtra("reviewText", reviewTxt);
            intent.putExtra("rating", rate);
            intent.putExtra("restauName", restName);
            this.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
