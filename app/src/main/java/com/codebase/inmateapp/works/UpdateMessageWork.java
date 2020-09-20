package com.codebase.inmateapp.works;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.repositories.MessagesRepository;
import com.codebase.inmateapp.utils.AppGlobals;
import com.codebase.inmateapp.utils.ProcessSms;

public class UpdateMessageWork extends Worker {

    public static final String UPDATE_MESSAGE_WORK = "update_sms_work";

    private final String TAG = "UpdateSMSWorker";

    private static final String WORK_STATUS = "work_status";
    private static final String WORK_RESULT = "work_result";
    private Context context;
    private MessagesRepository messagesRepository;
    private ProcessSms processSms;

    public UpdateMessageWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        messagesRepository = new MessagesRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        String uuid = getInputData().getString(DBTables.MESSAGE_UUID);
        String status = getInputData().getString(DBTables.MESSAGE_STATUS);

        MessageModel messageModel = messagesRepository.getMessageWithSpecificUuid(uuid);

        if (messageModel != null)  {
            messageModel.setStatus(status);
           if (messageModel.getStatus().equalsIgnoreCase("failed")) {

               if (processSms == null) {
                   processSms = new ProcessSms(context);
               }

                if (AppGlobals.isAutoDeletePendingMessages() &&
                        messageModel.getRetries() >= AppGlobals.getNumberOfRetriesForPendingMessages()) {
                    Log.d(TAG,
                            "Deleting failed message " + messageModel);

                    messagesRepository.delete(messageModel);
                    processSms.delSmsFromInbox(messageModel);
                } else {
                    messageModel.setRetries(messageModel.getRetries() + 1);
                    Log.d(TAG, "update message retries " + messageModel);

                    processSms.sendSms(messageModel, true);
                }
            }
                messagesRepository.insert(messageModel);
        } else {
            Log.e(TAG, "Update Failed - MessageModel Null");
        }
        return Result.success(outputData);
    }

}
