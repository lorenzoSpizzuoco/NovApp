package com.example.novapp2.ui.home.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.R;
import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.entity.chat.group.GroupChatAdapter;
import com.example.novapp2.entity.chat.group.GroupChatDB;
import com.example.novapp2.entity.chat.group.GroupChatFactory;

import java.util.List;


public class FragmentChat extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GroupChatDB.saveGroupChatToDB(new GroupChat("Il gruppo di dio", "dio"));

        List<GroupChat> groupChats = GroupChatFactory.createGroupChats();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GroupChatAdapter adapter = new GroupChatAdapter(groupChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }
}