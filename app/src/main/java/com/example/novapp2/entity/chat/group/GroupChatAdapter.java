package com.example.novapp2.entity.chat.group;

import android.os.Bundle;

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
import com.example.novapp2.sources.UserLogged;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
        setImage(holder, item);

        setUpDeleteButton(holder, item);

        holder.itemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("chatGroupId", item.getID());

            Navigation.findNavController(v).navigate(R.id.action_navigation_chat_to_openChat, args);
        });
    }

    private void setUpDeleteButton(@NonNull GroupChatViewHolder holder, GroupChat item) {
        holder.getDeleteButtonView().setOnClickListener(v -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.getContext()).setTitle(R.string.event_leave_group_chat)
                    .setMessage(R.string.message_leave_group_chat)
                    .setPositiveButton(R.string.ok_button, (di, i) -> {
                        removeGroup(item, v);
                    })
                    .setNegativeButton(R.string.dialog_close, (di, i) -> {
                    });

            builder.create();
            builder.show();
        });
    }

    private void removeGroup(GroupChat item, View v) {
        groupChats.remove(item);
        UserLogged.getUser().getGroupChats().remove(item.getID());
        UserService.updateUserById(UserLogged.getUser().getID(), UserLogged.getUser());
        Snackbar.make(v, R.string.removed_group_chat, Snackbar.LENGTH_SHORT).show();
        MessageService.createMessage(UserLogged.getUser().getEmail() + " " + v.getContext().getString(R.string.left_group_chat_msg), UserLogged.getUser().getEmail(), item.getID());
        notifyDataSetChanged();
    }

    private static void setImage(@NonNull GroupChatViewHolder holder, GroupChat item) {
        String imageUrl = item.getImage();

        if (imageUrl != null) {
            Glide.with(holder.getImageView())
                    .load(imageUrl)
                    .centerCrop().placeholder(R.drawable.analisi).into(holder.getImageView());
        }
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

}
