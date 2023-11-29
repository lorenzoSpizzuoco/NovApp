package com.example.novapp2.ui.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    Context context;
    List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList){
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.course_view, parent, false));
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


}
