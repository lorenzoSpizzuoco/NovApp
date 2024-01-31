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
import android.widget.TextView;
import android.widget.Toast;

import com.example.novapp2.R;
import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.entity.chat.group.GroupChatAdapter;
import com.example.novapp2.service.GroupChatsService;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ChatFragment extends Fragment {

    private List<GroupChat> groupChats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView emptyView = view.findViewById(R.id.noChatText);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        emptyView.setVisibility(View.GONE);

        List<String> userGroups = HomeFragment.getActiveUser().getGroupChats();
        if(userGroups != null) {
            groupChats = new ArrayList<>();

            for (String id : userGroups) {
                Task<GroupChat> groupChatTask = GroupChatsService.getGroupChatById(id);
                groupChatTask.addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        GroupChat groupChat = t.getResult();
                        groupChats.add(groupChat);

                        GroupChatAdapter adapter = new GroupChatAdapter(groupChats);
                        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        //TODO error handling
                    }
                });
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}