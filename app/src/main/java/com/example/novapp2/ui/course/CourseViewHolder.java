package com.example.novapp2.ui.course;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;


public class CourseViewHolder extends RecyclerView.ViewHolder {

    public static final String TAG = "CourseViewHolder";

    public ImageView imageView;
    public TextView titleView;
    public TextView descView;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        titleView = itemView.findViewById(R.id.title);
        descView = itemView.findViewById(R.id.desc);

        View courseView = itemView.findViewById(R.id.courseView);
        courseView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Log.i(TAG, "onClick: " + titleView.getText());
            }
        });
    }
}
