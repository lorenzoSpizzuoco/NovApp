package com.example.novapp2.ui.post;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;

//implements View.OnClickListener{
public class GenericViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView titleView;
    public TextView descView;
    public CardView courseCardView;

    public GenericViewHolder(@NonNull View itemView) {

        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        titleView = itemView.findViewById(R.id.title);
        descView = itemView.findViewById(R.id.desc);
        courseCardView = itemView.findViewById(R.id.courseCardView);

        View postView = itemView.findViewById(R.id.courseView);

        //postView.setOnClickListener(this);
    }

    /*
    @Override
    public void onClick(View v) {
        onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
    }
    */

}

