package com.example.novapp2.entity.chat.message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.entity.post.PostAdapter;
import com.example.novapp2.ui.home.HomeFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private final List<Message> messageList;

    public MessageAdapter(List<Message> messages) {
        messageList = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch(viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_me, parent, false);
                return new meMessageViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_other, parent, false);
                return new otherMessageViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)){
            case 0:
                ((meMessageViewHolder) holder).bindView(position);
                break;
            case 1:
                ((otherMessageViewHolder) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getAuthor().equals(HomeFragment.getActiveUser().getEmail()))
            return 0;
        else return 1;
    }

    public class meMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView contentView;

        public meMessageViewHolder(@NonNull View view)  {
            super(view);
            contentView = (TextView) view.findViewById(R.id.messageContent);
        }

        public TextView getContentView() {
            return contentView;
        }

        public void bindView(int position) {
            contentView.setText(messageList.get(position).getContent());
        }
    }

    public class otherMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView contentView;
        private final TextView authorView;

        public otherMessageViewHolder(@NonNull View view)  {
            super(view);
            contentView = view.findViewById(R.id.messageContent);
            authorView = view.findViewById(R.id.messageAuthor);
        }

        public TextView getContentView() {
            return contentView;
        }

        public void bindView(int position) {
            contentView.setText(messageList.get(position).getContent());
            authorView.setText(messageList.get(position).getAuthor());
        }
    }
    public void updateData(Message newData) {
        messageList.add(newData);

        notifyDataSetChanged();
    }

}
