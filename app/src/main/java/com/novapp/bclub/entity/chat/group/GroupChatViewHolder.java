package com.novapp.bclub.entity.chat.group;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novapp.bclub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class GroupChatViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final TextView authorView;
    private final ShapeableImageView imageView;
    private final FloatingActionButton deleteButtonView;
    private final Context context;

    public GroupChatViewHolder(@NonNull View view) {
        super(view);
        context = view.getContext();
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

    public Context getContext() {
        return context;
    }

}
