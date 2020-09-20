package com.codebase.inmateapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.utils.AppGlobals;
import com.codebase.inmateapp.utils.Helpers;
import com.codebase.inmateapp.works.AutoSyncServerWork;
import com.codebase.inmateapp.works.WorkHelpers;

public class AutomationSettings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final String TAG = "Automation";
    LinearLayout llAutoDeleteMessages, llAutoDeletePendingMessages, llNumberOfRetriesForPendingMessages,
            llAutoSync, llAutoSyncFrequency;
    Switch sAutoDeleteMessages, sAutoDeletePendingMessages, sAutoSync;
    TextView tvNumberOfRetriesForPendingMessagesValue, tvAutoSyncFrequencyValue;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_automation_settings, container, false);

        llAutoDeleteMessages = view.findViewById(R.id.ll_auto_delete_messages);
        llAutoDeleteMessages.setOnClickListener(this);
        sAutoDeleteMessages = view.findViewById(R.id.s_auto_delete_messages);

        llAutoDeletePendingMessages = view.findViewById(R.id.ll_auto_delete_pending_messages);
        llAutoDeletePendingMessages.setOnClickListener(this);
        sAutoDeletePendingMessages = view.findViewById(R.id.s_auto_delete_pending_messages);

        llNumberOfRetriesForPendingMessages = view.findViewById(R.id.ll_number_of_retries_for_pending_messages);
        llNumberOfRetriesForPendingMessages.setOnClickListener(this);
        tvNumberOfRetriesForPendingMessagesValue = view.findViewById(R.id.tv_number_of_retires_for_pending_messages_value);

        llAutoSync = view.findViewById(R.id.ll_auto_sync);
        llAutoSync.setOnClickListener(this);
        sAutoSync = view.findViewById(R.id.s_auto_sync);

        llAutoSyncFrequency = view.findViewById(R.id.ll_auto_sync_frequency);
        llAutoSyncFrequency.setOnClickListener(this);
        tvAutoSyncFrequencyValue = view.findViewById(R.id.tv_auto_sync_frequency_value);

        setViews();

        sAutoDeleteMessages.setOnCheckedChangeListener(this);
        sAutoDeletePendingMessages.setOnCheckedChangeListener(this);
        sAutoSync.setOnCheckedChangeListener(this);

        return view;
    }

    private void setViews() {
        sAutoDeleteMessages.setChecked(AppGlobals.isAutoDeleteMessages());

        boolean isAutoDeletePendingMessages = AppGlobals.isAutoDeletePendingMessages();
        sAutoDeletePendingMessages.setChecked(isAutoDeletePendingMessages);
        llNumberOfRetriesForPendingMessages.setEnabled(isAutoDeletePendingMessages);
        llNumberOfRetriesForPendingMessages.setAlpha(isAutoDeletePendingMessages ? 1f : .5f);
        tvNumberOfRetriesForPendingMessagesValue.setText(String.valueOf(AppGlobals.getNumberOfRetriesForPendingMessages()));

        boolean isAutoSync = AppGlobals.isAutoSync();
        sAutoSync.setChecked(AppGlobals.isAutoSync());
        llAutoSyncFrequency.setEnabled(isAutoSync);
        llAutoSyncFrequency.setAlpha(isAutoSync ? 1f : .5f);
        tvAutoSyncFrequencyValue.setText(String.valueOf(AppGlobals.getAutoSyncFrequency()));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_auto_delete_messages:
                sAutoDeleteMessages.setChecked(!sAutoDeleteMessages.isChecked());
                break;
            case R.id.ll_auto_delete_pending_messages:
                sAutoDeletePendingMessages.setChecked(!sAutoDeletePendingMessages.isChecked());
                break;
            case R.id.ll_number_of_retries_for_pending_messages:
                Helpers.showFloatingInputDialog(requireContext(),
                        getString(R.string.label_number_of_retries_for_pending_messages),
                        String.valueOf(AppGlobals.getNumberOfRetriesForPendingMessages()), value -> {
                    AppGlobals.setNumberOfRetriesForPendingMessages(Integer.parseInt(value));
                    tvNumberOfRetriesForPendingMessagesValue.setText(value);
                        });
                break;
            case R.id.ll_auto_sync:
                sAutoSync.setChecked(!sAutoSync.isChecked());
                break;
            case R.id.ll_auto_sync_frequency:
                Helpers.showFloatingInputDialog(requireContext(),
                        getString(R.string.label_auto_sync_frequency), String.valueOf(AppGlobals.getAutoSyncFrequency()), value -> {
                            AppGlobals.setAutoSyncFrequency(Integer.parseInt(value));
                            tvAutoSyncFrequencyValue.setText(value);
                            WorkHelpers.registerWorkManagerToSyncServer(requireContext());
                        });
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()) {
            case R.id.s_auto_delete_messages:
                AppGlobals.setAutoDeleteMessages(b);
                break;
            case R.id.s_auto_delete_pending_messages:
                AppGlobals.setAutoDeletePendingMessages(b);
                llNumberOfRetriesForPendingMessages.setEnabled(b);
                llNumberOfRetriesForPendingMessages.setAlpha(b ? 1f : .5f);
                break;
            case R.id.s_auto_sync:
                AppGlobals.setAutoSync(b);
                llAutoSyncFrequency.setEnabled(b);
                llAutoSyncFrequency.setAlpha(b ? 1f : .5f);
                if (b) {
                    WorkHelpers.registerWorkManagerToSyncServer(requireContext());
                } else {
                    WorkManager.getInstance(requireContext()).cancelUniqueWork(AutoSyncServerWork.SYNC_MESSAGES_WITH_SERVER_WORK);
                }
                break;
        }
    }


}
