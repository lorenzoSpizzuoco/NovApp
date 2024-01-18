package com.example.novapp2.ui.post;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.post.Post;

import java.util.List;

// RecyclerView.Adapter<PostAdapter.PostViewHolder>
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
    /*
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);;

        switch (viewType) {
            case 1:
                LayoutInflater.from(context).inflate(R.layout.event_view, parent, false);
                break;


        }

        return new PostAdapter.PostViewHolder(view);
    }

     */
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.event_view, parent, false);
                return new EventViewHolder(view);
            default:
                view = LayoutInflater.from(context).inflate(R.layout.course_view, parent, false);
                return new PostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)) {
            case 1:
                ((EventViewHolder) holder).bindView(position);
                break;
            default:
                ((PostViewHolder) holder).bindView(position);

        }
        //holder.courseCardView.setAnimation(AnimationUtils.loadAnimation(holder.courseCardView.getContext(), R.anim.fall_down_animation));
    }

    @Override
    public int getItemViewType(int position) {
        if (postList.get(position).getCategory().equals("event")) {
            return 1;
        }
        return 0;
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

    // implements View.OnClickListener
    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public PostViewHolder(@NonNull View itemView) {

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


        public void bindView(int position) {
            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
            imageView.setImageResource(postList.get(position).getImage());
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public EventViewHolder(@NonNull View itemView) {

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

    /*
    @Override
    public void onClick(View v) {
        onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
    }
    */

    }
}
