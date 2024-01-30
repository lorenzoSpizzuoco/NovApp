package com.example.novapp2.entity.chat.group;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;

public class GroupChatViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;
    private TextView authorView;

    public GroupChatViewHolder(@NonNull View view) {
        super(view);
        titleView = (TextView) view.findViewById(R.id.groupTitleDisplay);
        authorView = (TextView) view.findViewById(R.id.groupAuthorDisplay);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getAuthorView() {
        return authorView;
    }
}
