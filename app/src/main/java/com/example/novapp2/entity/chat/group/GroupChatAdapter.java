package com.example.novapp2.entity.chat.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatViewHolder> {
    private List<GroupChat> groupChats;

    public GroupChatAdapter(List<GroupChat> gChats) {
        groupChats = gChats;
    }

    @NonNull
    @Override
    public GroupChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chat_view, parent, false);
        return new GroupChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatViewHolder holder, int position) {
        GroupChat item = groupChats.get(position);
        holder.getTitleView().setText(item.getTitle());
        holder.getAuthorView().setText(item.getAuthor());
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

}
