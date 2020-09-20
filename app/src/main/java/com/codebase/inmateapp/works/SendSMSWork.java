package com.codebase.inmateapp.works;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.repositories.MessagesRepository;
import com.codebase.inmateapp.utils.ProcessSms;

public class SendSMSWork extends Worker {

    public static final String SEND_SMS_WORK = "send_sms_work";

    private final String TAG = "SendSMSWorker";

    private static final String WORK_STATUS = "work_status";
    private static final String WORK_RESULT = "work_result";
    private Context context;

    private MessagesRepository messagesRepository;

    private ProcessSms processSms;

    public SendSMSWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        messagesRepository = new MessagesRepository((Application) context);
        processSms = new ProcessSms(context);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        String uuid = getInputData().getString(DBTables.MESSAGE_UUID);
        MessageModel messageModel = messagesRepository.getMessageWithSpecificUuid(uuid);

        if (messageModel != null)  {
            processSms.sendSms(messageModel, true);
        } else {
            Log.e(TAG, "MessageModel is NULL - UUID: " + uuid);
        }

        return Result.success(outputData);
    }

}
