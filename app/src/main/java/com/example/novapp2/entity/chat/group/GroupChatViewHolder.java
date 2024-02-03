package com.example.novapp2.entity.chat.group;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupChatViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final TextView authorView;
    private final ImageView imageView;
    private final FloatingActionButton deleteButtonView;


    public GroupChatViewHolder(@NonNull View view) {
        super(view);
        titleView = view.findViewById(R.id.groupTitleDisplay);
        authorView = view.findViewById(R.id.groupAuthorDisplay);
        imageView = view.findViewById(R.id.imageView);
        deleteButtonView = view.findViewById(R.id.deleteButton);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getAuthorView() {
        return authorView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public FloatingActionButton getDeleteButtonView() {
        return deleteButtonView;
    }
}
