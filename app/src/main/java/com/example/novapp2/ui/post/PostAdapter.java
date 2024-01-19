package com.example.novapp2.ui.post;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.post.Post;
import com.google.android.material.chip.Chip;

import java.util.List;

public class PostAdapter extends  RecyclerView.Adapter{


    public interface OnItemClickListener {
        void onPostItemClick(Post post);

    }

    private Context context;
    private List<Post> postList;

    private OnItemClickListener onItemClickListener;

    public PostAdapter(Context context, List<Post> postList, OnItemClickListener onItemClickListener){

        this.postList = postList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

                // event layout
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.event_view, parent, false);
                return new EventViewHolder(view);

                // uni info layout
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);
                return new UniInfoViewHolder(view);

                // ripetizioni layout
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);
                return new RipetizioniViewHolder(view);

                // group layout
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);
                return new GroupViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)) {
            case 1:
                ((EventViewHolder) holder).bindView(position);
                break;
            case 2:
                ((UniInfoViewHolder) holder).bindView(position);
                break;
            case 3:
                ((RipetizioniViewHolder) holder).bindView(position);
                break;
            case 4:
                ((GroupViewHolder) holder).bindView(position);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return postList.get(position).getCategory();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();

    }

    // event view holder
    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView titleView;
        //public TextView descView;

        public Chip typechip;

        public TextView dateView;


        public CardView eventCardView;


        public EventViewHolder(@NonNull View itemView) {

            super(itemView);
            eventCardView = itemView.findViewById(R.id.eventCardView);
            imageView = itemView.findViewById(R.id.eventimageview);
            titleView = itemView.findViewById(R.id.eventTitle);
            //descView = itemView.findViewById(R.id.desc);

            typechip = itemView.findViewById(R.id.eventTypeChip);
            //eventCardView.setOnClickListener(this);
            //View eventView = itemView.findViewById(R.id.eventView);

            //eventView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Log.d("EVENT VIEW HOLDER", "inside bindView");
            try {
                imageView.setImageResource(postList.get(position).getImage());
                titleView.setText(postList.get(position).getTitle());
                typechip.setText("evento");
                typechip.setBackgroundColor(ContextCompat.getColor(context, R.color.main_red));
                dateView.setText(postList.get(position).getDate());
            }
            catch(NullPointerException e) {
                Log.e("EVENT VIEW HOLDER", e.getMessage());
            }

            //descView.setText(postList.get(position).getContent());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    // uni info viewHolder
    public class UniInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public UniInfoViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            titleView = itemView.findViewById(R.id.title);
            descView = itemView.findViewById(R.id.desc);
            courseCardView = itemView.findViewById(R.id.courseCardView);

            View postView = itemView.findViewById(R.id.courseView);

            //postView.setOnClickListener(this);
        }

        public void bindView(int position) {
            imageView.setImageResource(postList.get(position).getImage());
            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    public class RipetizioniViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public RipetizioniViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            titleView = itemView.findViewById(R.id.title);
            descView = itemView.findViewById(R.id.desc);
            courseCardView = itemView.findViewById(R.id.courseCardView);

            View postView = itemView.findViewById(R.id.courseView);

            //postView.setOnClickListener(this);
        }

        public void bindView(int position) {
            imageView.setImageResource(postList.get(position).getImage());
            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public GroupViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            titleView = itemView.findViewById(R.id.title);
            descView = itemView.findViewById(R.id.desc);
            courseCardView = itemView.findViewById(R.id.courseCardView);

            View postView = itemView.findViewById(R.id.courseView);

            //postView.setOnClickListener(this);
        }

        public void bindView(int position) {
            imageView.setImageResource(postList.get(position).getImage());
            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

}
