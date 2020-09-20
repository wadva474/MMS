package com.codebase.inmateapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.utils.DateUtils;

import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter {

    public static final String TAG = "MessageListAdapter";
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_PHOTO_SENT = 3;
    private static final int VIEW_TYPE_PHOTO_RECEIVED = 4;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<MessageModel> messagesList;

    public MessagesListAdapter(Context context, Fragment parentFragment, List<MessageModel> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageModel message = messagesList.get(position);
        if (message.getOrigin().equals("server")) {
            // If the current user is the sender of the message
//           if (message.getType().equalsIgnoreCase("photo")) {
//                return VIEW_TYPE_PHOTO_SENT;
//            } else {
                return VIEW_TYPE_MESSAGE_SENT;
//            }
        } else {
            // If some other user sent the message
//            if (message.getType().equalsIgnoreCase("photo")) {
//                return VIEW_TYPE_PHOTO_RECEIVED;
//            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
//            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_message_sent, parent, false);
                return new SentMessageHolder(view);
            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            case VIEW_TYPE_PHOTO_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_photo_message_sent, parent, false);
                return new PhotoMessageHolder(view);
            case VIEW_TYPE_PHOTO_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_photo_message_received, parent, false);
                return new PhotoMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel message = messagesList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_PHOTO_SENT:
            case VIEW_TYPE_PHOTO_RECEIVED:
                ((PhotoMessageHolder) holder).bind(message);
                break;
        }
    }

    public void setMessagesList(List<MessageModel> messagesList) {
        this.messagesList = messagesList;
        notifyDataSetChanged();
    }

    public void addLastMessage(MessageModel message) {
        boolean messageAlreadyExists = false;
        for (int i = 0; i < messagesList.size(); i++) {
            if (message.getUuid().equals(messagesList.get(i).getUuid())) {
                messageAlreadyExists = true;
                // update message only if data is different
                if (!message.getStatus().equalsIgnoreCase(messagesList.get(i).getStatus())
                || message.isSynced() != messagesList.get(i).isSynced()) {
                    messagesList.set(i, message);
                    notifyItemChanged(i);
                }
                break;
            }
        }
        if (!messageAlreadyExists) {
            messagesList.add(message);
            notifyItemInserted(messagesList.size() - 1);
            layoutManager.scrollToPosition(messagesList.size() - 1);
        }
    }

//    public void updateMessage(MessageModel messageModel) {
//        int position = this.t(messageModel);
//        this.notifyItemChanged(position);
//    }

    public void clear() {
        int size = messagesList.size();
        messagesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvStatus, tvSynced;

        SentMessageHolder(View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_body);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvSynced = itemView.findViewById(R.id.tv_synced);
        }

        void bind(MessageModel message) {
            tvMessage.setText(message.getBody());

            // Format the stored timestamp into a readable String using method.
            Log.i(TAG, "" + message.getDate());
            tvTime.setText(DateUtils.formatDateTime(message.getDate()));

            String status = message.getStatus();
            tvStatus.setText(status);
            int statusColor;
            if (status.equalsIgnoreCase("sent")) {
                statusColor = Color.BLUE;
            } else if (status.equalsIgnoreCase("delivered")) {
                statusColor = Color.GREEN;
            } else {
                statusColor = Color.RED;
            }
            tvStatus.setTextColor(statusColor);

            int syncedColor;
            String syncedText;
            if (message.isSynced()) {
                syncedColor = Color.DKGRAY;
                syncedText = "synced";
            } else {
                syncedColor = Color.GRAY;
                syncedText = "un-synced";
            }
            tvSynced.setText(syncedText);
            tvSynced.setTextColor(syncedColor);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvSynced;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_body);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvSynced = itemView.findViewById(R.id.tv_synced);
        }

        void bind(MessageModel message) {
            tvMessage.setText(message.getBody());
            // Format the stored timestamp into a readable String
            tvTime.setText(DateUtils.formatDateTime(message.getDate()));

            int syncedColor;
            String syncedText;
            if (message.isSynced()) {
                syncedColor = Color.DKGRAY;
                syncedText = "synced";
            } else {
                syncedColor = Color.GRAY;
                syncedText = "un-synced";
            }
            tvSynced.setText(syncedText);
            tvSynced.setTextColor(syncedColor);
        }
    }

    private class PhotoMessageHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
//        SimpleDraweeView sdvPhoto;
        ProgressBar pbPhoto;

        PhotoMessageHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
//            sdvPhoto = itemView.findViewById(R.id.sdv_chat_photo);
            pbPhoto = itemView.findViewById(R.id.pb_photo);
        }

        void bind(MessageModel message) {

        }
    }

}
