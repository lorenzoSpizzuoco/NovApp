package com.example.novapp2.ui.home.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.novapp2.R;
import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.entity.chat.message.Message;
import com.example.novapp2.entity.chat.message.MessageAdapter;
import com.example.novapp2.service.GroupChatsService;
import com.example.novapp2.service.MessageService;
import com.example.novapp2.service.UserService;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class OpenChatFragment extends Fragment {


    private final UserService userService = new UserService();
    private OpenChatViewModel openChatViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        openChatViewModel = new ViewModelProvider(this).get(OpenChatViewModel.class);
        return inflater.inflate(R.layout.fragment_open_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton backButton = view.findViewById(R.id.backButton);
        ExtendedFloatingActionButton sendButton = view.findViewById(R.id.send_button);
        TextInputLayout messageContent = view.findViewById(R.id.message_content);
        TextView emptyView = view.findViewById(R.id.noChatText);
        TextView groupName = view.findViewById(R.id.groupName);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        emptyView.setVisibility(View.GONE);
        Bundle args = getArguments();
        String groupId = args.getString("chatGroupId");

        /*mDatabase = FirebaseDatabase.getInstance()
                .getReference("groupChats")
                .child(groupId)
                .child("messages");*/


        loadGroupChatMessages(groupName, recyclerView, groupId);

        setUpSendButton(sendButton, messageContent, groupId);

        setUpBackButton(backButton);
    }

    private void loadGroupChatMessages(TextView groupName, RecyclerView recyclerView, String groupId) {
        Task<GroupChat> getGroupByIdTask = GroupChatsService.getGroupChatById(groupId);
        getGroupByIdTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                GroupChat activeGroup = task.getResult();
                groupName.setText(activeGroup.getTitle());
                List<Message> groupMessages = new ArrayList<>();

                MessageAdapter adapter = new MessageAdapter(groupMessages);

                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                // Observing viewModel
                openChatViewModel.getLastMessage(groupId).observe(getViewLifecycleOwner(), lastMessage -> {
                    adapter.updateData(lastMessage);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                });
            }
        });
    }

    private void setUpSendButton(ExtendedFloatingActionButton sendButton, TextInputLayout messageContent, String groupId) {
        sendButton.setOnClickListener(v -> {
            String content = Objects.requireNonNull(messageContent.getEditText()).getText().toString().trim();
            if (!content.equals("")) {
                MessageService.createMessage(content, userService.getCurrentUser().getEmail(), groupId);
                messageContent.getEditText().setText("");
            }
        });
    }

    private static void setUpBackButton(FloatingActionButton backButton) {
        backButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_openChat_to_navigationChat));
    }
}