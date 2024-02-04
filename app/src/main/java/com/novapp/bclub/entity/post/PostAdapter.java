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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

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
                view = LayoutInflater.from(context).inflate(R.layout.info_view, parent, false);
                return new UniInfoViewHolder(view);

                // ripetizioni layout
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.ripetizioni_view, parent, false);
                return new RipetizioniViewHolder(view);

                // group layout
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.group_view, parent, false);
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
        public TextView dateView;

        public TextView descView;
        public CardView eventCardView;

        public TextView placeView;


        public EventViewHolder(@NonNull View itemView) {

            super(itemView);
            eventCardView = itemView.findViewById(R.id.eventCardView);
            imageView = itemView.findViewById(R.id.eventimageview);
            titleView = itemView.findViewById(R.id.eventTitle);
            dateView = itemView.findViewById(R.id.eventDate);
            descView = itemView.findViewById(R.id.eventDesc);
            placeView = itemView.findViewById(R.id.eventPlace);
            eventCardView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Log.d("EVENT VIEW HOLDER", "inside bindView");

            if (postList.get(position).getPostImage() == null) {
                imageView.setImageResource(postList.get(position).getImage());
            }
            else {
                Glide.with(context)
                        .load(postList.get(position).getPostImage())
                        .centerCrop()
                        .placeholder(R.drawable.analisi)
                        .into(imageView);
            }

            titleView.setText(postList.get(position).getTitle());
            dateView.setText(postList.get(position).getDate());
            descView.setText(postList.get(position).getContent());
            placeView.setText(postList.get(position).getPlace());

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    // uni info viewHolder
    public class UniInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleView;
        public TextView descView;
        public CardView infoCardView;

        public TextView dateView;

        //public TextView placeView;

        public UniInfoViewHolder(@NonNull View itemView) {

            super(itemView);
            titleView = itemView.findViewById(R.id.infoTitle);
            descView = itemView.findViewById(R.id.infodesc);
            infoCardView = itemView.findViewById(R.id.infoUniCardView);
            dateView = itemView.findViewById(R.id.infoDate);
            //placeView = itemView.findViewById(R.id.infoPlace);


            infoCardView.setOnClickListener(this);
        }

        public void bindView(int position) {
            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
            dateView.setText(postList.get(position).getDate());
            //placeView.setText(postList.get(position).getPlace());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    public class RipetizioniViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView titleView;
        public TextView descView;
        public CardView courseCardView;

        public TextView dateView;

        public RipetizioniViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.ripetizioniImageView);
            titleView = itemView.findViewById(R.id.ripetizioniTitle);
            descView = itemView.findViewById(R.id.ripetizioniDesc);
            courseCardView = itemView.findViewById(R.id.ripetizioniCardView);
            dateView = itemView.findViewById(R.id.ripetizioniDate);
            courseCardView.setOnClickListener(this);
        }

        public void bindView(int position) {
            /*
            if (postList.get(position).getPostImage() == null) {
                imageView.setImageResource(postList.get(position).getImage());
            }
            else {
                String base64Econded = postList.get(position).getPostImage();
                //imageView.setImageBitmap(base64ToBitmap(base64Econded));
            }
            */

            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
            dateView.setText(postList.get(position).getDate());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ShapeableImageView imageView;
        public TextView titleView;
        public TextView descView;

        public TextView dateView;
        public CardView groupCardView;

        public GroupViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.groupimageview);
            titleView = itemView.findViewById(R.id.groupTitle);
            descView = itemView.findViewById(R.id.groupDesc);
            groupCardView = itemView.findViewById(R.id.groupCardView);
            dateView = itemView.findViewById(R.id.groupDate);
            imageView.setShapeAppearanceModel(
                    imageView.getShapeAppearanceModel()
                            .toBuilder()
                            .setAllCorners(CornerFamily.ROUNDED, 30)
                            .build()
            );
            groupCardView.setOnClickListener(this);
        }

        public void bindView(int position) {
            if (postList.get(position).getPostImage() == null) {
                imageView.setImageResource(postList.get(position).getImage());
            }
            else {
                Glide.with(context)
                        .load(postList.get(position).getPostImage())
                        .centerCrop()
                        .placeholder(R.drawable.analisi)
                        .into(imageView);
            }

            titleView.setText(postList.get(position).getTitle());
            descView.setText(postList.get(position).getContent());
            dateView.setText(postList.get(position).getDate());
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
        }

    }

}
