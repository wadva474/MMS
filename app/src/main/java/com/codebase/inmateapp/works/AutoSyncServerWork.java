package com.codebase.inmateapp.works;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codebase.inmateapp.interfaces.ApiCallback;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.repositories.MessagesRepository;

import java.util.List;

public class AutoSyncServerWork extends Worker {

    public static final String SYNC_MESSAGES_WITH_SERVER_WORK = "auto_sync_from_server_work";

    private final String TAG = "SyncWorker";

    private static final String WORK_STATUS = "work_status";
    private static final String WORK_RESULT = "work_result";
    private Context context;

    private MessagesRepository messagesRepository;

    public AutoSyncServerWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        messagesRepository = new MessagesRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString(WORK_STATUS);
        Log.d(TAG, taskData.toString());
        Log.d(TAG, "STATUS: " + taskDataString);
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        messagesRepository.getMessagesFromServer(new ApiCallback() {
            @Override
            public void onSuccess(Object result) {
//                WorkHelpers.registerWorkManagerToSyncServer(context);
                List<MessageModel> messageModelList = (List<MessageModel>) result;
                if (messageModelList.size() > 0) {
                    for (int i = 0; i < messageModelList.size(); i++) {
                        WorkHelpers.registerWorkManagerToSendSMS(context, messageModelList.get(i).getUuid());
                    }
                } else {
                    Log.d(TAG, "GET SERVER MESSAGES SUCCESS - EMPTY");
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "GET SERVER MESSAGES FAILED - " + error);
//                WorkHelpers.registerWorkManagerToSyncServer(context);
            }
        });

        messagesRepository.publishMessagesToServer(new ApiCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.d(TAG, "PUBLISH SERVER MESSAGES SUCCESS - " + result);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "PUBLISH SERVER MESSAGES FAILED - " + error);
            }
        });

        messagesRepository.updateMessagesOnServer(new ApiCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.d(TAG, "UPDATE SERVER MESSAGES SUCCESS - " + result);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "UPDATE SERVER MESSAGES FAILED - " + error);
            }
        });

        WorkHelpers.registerWorkManagerToSyncServer(context);
        return Result.success(outputData);
    }

}
