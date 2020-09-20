package com.codebase.inmateapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.utils.DateUtils;

import java.util.List;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    private Context context;
    private List<DialogModel> dialogsList;
    private onItemClickListener mItemClickListener;


    public DialogsAdapter(Context context, List<DialogModel> dialogsList) {
        this.context = context;
        this.dialogsList = dialogsList;
    }

    @NonNull
    @Override
    public DialogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogsAdapter.ViewHolder holder, int position) {
        DialogModel dialogModel = dialogsList.get(position);

        holder.tvDialogTitle.setText(dialogModel.getAddress());
        holder.tvDialogDescription.setText(dialogModel.getLastMessage());
        holder.tvDialogLastMessageDate.setText(DateUtils.getTimeAgo(dialogModel.getLastMessageDate()));
    }

    @Override
    public int getItemCount() {
        return dialogsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDialogTitle;
        private TextView tvDialogDescription;
        private TextView tvDialogLastMessageDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDialogTitle = itemView.findViewById(R.id.tv_dialog_title);
            tvDialogDescription = itemView.findViewById(R.id.tv_dialog_description);
            tvDialogLastMessageDate = itemView.findViewById(R.id.tv_dialog_last_message_date);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (mItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    mItemClickListener.onItemClick(dialogsList.get(position));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(DialogModel dialogModel);
    }

    public void setonItemClickListener(onItemClickListener pListener) {
        this.mItemClickListener = pListener;
    }

    public void setValues(List<DialogModel> dialogModelList) {
        this.dialogsList = dialogModelList;
        notifyDataSetChanged();
    }
}
