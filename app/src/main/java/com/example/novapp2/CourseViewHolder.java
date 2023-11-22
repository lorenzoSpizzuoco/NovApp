package com.example.novapp2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CourseViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView titleView;
    public TextView descView;
    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        titleView = itemView.findViewById(R.id.title);
        descView = itemView.findViewById(R.id.desc);
    }
}
