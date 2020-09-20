package com.codebase.inmateapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebase.inmateapp.MainActivity;
import com.codebase.inmateapp.R;
import com.codebase.inmateapp.adapters.MessagesListAdapter;
import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.viewModels.MessagesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView rvMessagesList;
    private MessagesListAdapter messagesListAdapter;
    private MessagesViewModel messagesViewModel;
    private String dialogAddress = null;
    private List<MessageModel> messageModelList = new ArrayList<>();

    private final String TAG = MessagesFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        ((MainActivity) requireActivity()).HideShowBottomNavigationBar(this);

        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);

        rvMessagesList = root.findViewById(R.id.rv_messages_list);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dialogAddress = bundle.getString(DBTables.DIALOG_DIALOG_ADDRESS);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(dialogAddress);
        }

        messagesListAdapter = new MessagesListAdapter(requireContext(), MessagesFragment.this, messageModelList);
        rvMessagesList.setAdapter(messagesListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);
        rvMessagesList.setLayoutManager(layoutManager);
        messagesListAdapter.setLayoutManager(layoutManager);

        loadMessages(dialogAddress);
        return root;
    }

    private void loadMessages(String dialogAddress) {
        messagesViewModel.getSpecificDialogMessages(dialogAddress).observe(getViewLifecycleOwner(), chatMessageModels -> {
            Log.i(TAG, "MessagesModelsNull: " + (chatMessageModels == null));
            if (chatMessageModels != null && chatMessageModels.size() > 0) {
                for (int i = 0; i < chatMessageModels.size(); i++) {
                    messagesListAdapter.addLastMessage(chatMessageModels.get(i));
                    // last loop iteration, load recent messages from server
//                    if (!(i + 1 < chatMessageModels.size())) {
//                        //last iteration
//                        Log.i(TAG, "loopLAst");
//                        loadRecentMessagesServer(chatMessageModels.get(i).getCreatedAt() / 1000);
//                    }
                }
            }
        });
    }
}
