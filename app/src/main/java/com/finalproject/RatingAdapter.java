package com.finalproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.model.Rating;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private Rating rating;
    private Context context;
    private List<Rating> ratingList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvUser, tvFeedback;
        CardView cardView;
        RatingBar ratingBar;


        ViewHolder(View view) {
            super(view);
            ratingBar=view.findViewById(R.id.ratingBar);
            tvUser = view.findViewById(R.id.tvUser);
            tvFeedback = view.findViewById(R.id.tvFeedback);
            cardView = view.findViewById(R.id.cardView);



        }


    }

    public RatingAdapter(Context mContext, List<Rating> ratingList) {
        this.context = mContext;
        this.ratingList = ratingList;

    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);


        return new RatingAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RatingAdapter.ViewHolder holder, final int position) {

        rating = ratingList.get(position);
        holder.tvUser.setText(rating.getGivenUser());
        holder.tvFeedback.setText(rating.getMessage());
        holder.ratingBar.setRating(Float.parseFloat(rating.getStar()));


    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }


}