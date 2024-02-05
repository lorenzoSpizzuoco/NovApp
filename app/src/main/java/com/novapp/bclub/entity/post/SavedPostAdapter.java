package com.novapp.bclub.entity.post;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.novapp.bclub.R;

import java.util.List;

public class SavedPostAdapter extends  RecyclerView.Adapter{

    private final Context context;
    private List<Post> postList;
    private final PostAdapter.OnItemClickListener onItemClickListener;

    public SavedPostAdapter(Context context, List<Post> postList, PostAdapter.OnItemClickListener onItemClickListener) {
        this.postList = postList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.saved_card_view, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((SavedViewHolder) holder).bindView(position);
    }

    public int getItemViewType(int position) {
        return postList.get(position).getCategory();
    }

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
    public class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView savedCardView;
        public ImageView imageView;
        public TextView titleTextView;
        //public TextView subtitleTextView;
        public TextView descTextView;

        public SavedViewHolder(@NonNull View itemView) {

            super(itemView);
            savedCardView = itemView.findViewById(R.id.saved_card_view);
            imageView = itemView.findViewById(R.id.savedCardImageView);
            titleTextView = itemView.findViewById(R.id.savedPostTitleTextView);
            //subtitleTextView = itemView.findViewById(R.id.savedPostSubtitleTextView);
            descTextView = itemView.findViewById(R.id.savedPostDescTextView);
            savedCardView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Log.d("EVENT VIEW HOLDER", "inside bindView");
            //imageView.setImageResource(postList.get(position).getImage());
            if(postList.get(position).getCategory() != 2) {
                Glide.with(context)
                        .load(postList.get(position).getPostImage())
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView);

            }
            else {
                imageView.setVisibility(View.GONE);
            }

            titleTextView.setText(postList.get(position).getTitle());
            descTextView.setText(postList.get(position).getContent());
            //subtitleTextView.setText(postList.get(position).getPlace());

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }



}
