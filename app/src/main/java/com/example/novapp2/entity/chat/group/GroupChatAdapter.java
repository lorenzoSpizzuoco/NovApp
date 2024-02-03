package com.example.novapp2.entity.chat.group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novapp2.R;
import com.example.novapp2.service.MessageService;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatViewHolder> {
    private final List<GroupChat> groupChats;

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
        String imageUrl = item.getImage();

        if (imageUrl != null) {
            Glide.with(holder.getImageView())
                    .load(imageUrl)
                    .centerCrop().placeholder(R.drawable.analisi).into(holder.getImageView());
        }

        holder.getDeleteButtonView().setOnClickListener(v -> {
            groupChats.remove(item);
            HomeFragment.getActiveUser().getGroupChats().remove(item.getID());
            UserService.updateUserById(HomeFragment.getActiveUser().getID(), HomeFragment.getActiveUser());
            Snackbar.make(v, R.string.removed_group_chat, Snackbar.LENGTH_SHORT).show();
            MessageService.createMessage(HomeFragment.getActiveUser().getEmail() + " " + "left the chat", HomeFragment.getActiveUser().getEmail(), item.getID());
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("chatGroupId", item.getID());

            Navigation.findNavController(v).navigate(R.id.action_navigation_chat_to_openChat, args);
        });
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

}
