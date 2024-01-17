package com.example.novapp2.ui.ad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.ui.*;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AdViewAdapter extends RecyclerView.Adapter<AdViewAdapter.AdViewHolder> {

    private String TAG = AdViewAdapter.class.getSimpleName();
    private final List<Ad> adList;
    private final OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.ad_view, parent, false);

        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        holder.bind(adList.get(position));
        //holder.cardView.setAnimation(AnimationUtils.loadAnimation(holder.cardView.getContext(), R.anim.fall_down_animation));
    }

    @Override
    public int getItemCount() {

        if (adList != null) {
            return adList.size();
        }
        else {
            return 0;
        }

    }

    public interface OnItemClickListener {
        void onAdItemClick(Ad ad);
    }



    public AdViewAdapter(List<Ad> adList, OnItemClickListener onItemClickListener) {
        this.adList = adList;
        this.onItemClickListener = onItemClickListener;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String tag = AdViewHolder.class.getSimpleName();
        private final TextView textViewTitle;
        private final TextView textViewDesc;
        private final ImageView imageView;

        private final CardView cardView;

        public AdViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.AdImageView);
            textViewTitle = itemView.findViewById(R.id.AdTitleView);
            textViewDesc = itemView.findViewById(R.id.AdDescView);
            cardView = itemView.findViewById(R.id.courseView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Snackbar.make(v, textViewTitle.getText(), Snackbar.LENGTH_SHORT).show();
            onItemClickListener.onAdItemClick(adList.get(getAdapterPosition()));
        }

        public void bind(Ad ad) {
            textViewDesc.setText(ad.getContent());
            textViewTitle.setText(ad.getTitle());
            // default, just for testing
            imageView.setImageResource(R.drawable.analisi);
        }
    }



}