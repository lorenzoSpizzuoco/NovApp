package com.example.novapp2.ui.course;

import android.content.Context;
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

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    Context context;
    List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList){
        this.courseList = courseList;
        this.context = context;
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);
        return new CourseViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.titleView.setText(courseList.get(position).getTitle());
        holder.descView.setText(courseList.get(position).getDesc());
        holder.imageView.setImageResource(courseList.get(position).getImg());
        holder.courseCardView.setAnimation(AnimationUtils.loadAnimation(holder.courseCardView.getContext(), R.anim.fall_down_animation));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            titleView = itemView.findViewById(R.id.title);
            descView = itemView.findViewById(R.id.desc);
            courseCardView = itemView.findViewById(R.id.courseCardView);

            View courseView = itemView.findViewById(R.id.courseView);
            courseView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Handle item click here
                    // You can access your data using courseList.get(position)
                }
            });
        }
    }
}
