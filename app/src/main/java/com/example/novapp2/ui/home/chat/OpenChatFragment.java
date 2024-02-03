package com.example.novapp2.ui.home.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.novapp2.R;
import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.entity.chat.message.Message;
import com.example.novapp2.entity.chat.message.MessageAdapter;
import com.example.novapp2.service.GroupChatsService;
import com.example.novapp2.service.MessageService;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class OpenChatFragment extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton backButton = view.findViewById(R.id.backButton);
        Button sendButton = view.findViewById(R.id.send_button);
        TextInputLayout messageContent = view.findViewById(R.id.message_content);
        TextView emptyView = view.findViewById(R.id.noChatText);
        TextView groupName = view.findViewById(R.id.groupName);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        emptyView.setVisibility(View.GONE);
        Bundle args = getArguments();
        String groupId = args.getString("chatGroupId");

        mDatabase = FirebaseDatabase.getInstance()
                .getReference("groupChats")
                .child(groupId)
                .child("messages");


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

                mDatabase.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Message message = new Message(dataSnapshot.getKey(), (String) value.get("content"), (String) value.get("author"), (Long) value.get("timestamp"));
                        adapter.updateData(message);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("OpenChat", "Failed to read value.", error.toException());
                    }
                });
            }
        });

        sendButton.setOnClickListener(v -> {
            String content = Objects.requireNonNull(messageContent.getEditText()).getText().toString();
            if (!content.equals("")) {
                MessageService.createMessage(content, HomeFragment.getActiveUser().getEmail(), groupId);
                messageContent.getEditText().setText("");
            }
        });

        backButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_openChat_to_navigationChat));
    }
}