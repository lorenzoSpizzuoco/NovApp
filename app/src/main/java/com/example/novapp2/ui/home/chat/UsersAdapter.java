package com.example.novapp2.ui.home.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.entity.User;
import com.example.novapp2.databinding.UserChatBinding;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserChatBinding itemContainerUserBinding = UserChatBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        UserChatBinding binding;

        UserViewHolder(UserChatBinding userChatBinding) {
            super(userChatBinding.getRoot());
            binding = userChatBinding;
        }

        void setUserData(User user) {
            binding.username.setText(user.name);
            binding.email.setText(user.email);
            // TODO pic
        }
    }


}
