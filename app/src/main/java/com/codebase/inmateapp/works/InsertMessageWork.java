package com.codebase.inmateapp.works;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.repositories.DialogsRepository;
import com.codebase.inmateapp.repositories.MessagesRepository;
import com.codebase.inmateapp.utils.ProcessSms;

public class InsertMessageWork extends Worker {

    public static final String INSERT_MESSAGE_WORK = "insert_message_work";

    private final String TAG = "InsertSMSWorker";

    private static final String WORK_STATUS = "work_status";
    private static final String WORK_RESULT = "work_result";
    private Context context;
    private MessagesRepository messagesRepository;
    private DialogsRepository dialogsRepository;
    private ProcessSms processSms;

    public InsertMessageWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        messagesRepository = new MessagesRepository((Application) context);
        dialogsRepository = new DialogsRepository((Application) context);
        processSms = new ProcessSms(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        String address = getInputData().getString(DBTables.MESSAGE_ADDRESS);
        String body = getInputData().getString(DBTables.MESSAGE_BODY);
        long date = getInputData().getLong(DBTables.MESSAGE_DATE, 0);

        MessageModel messageModel = new MessageModel();
        messageModel.setAddress(address);
        messageModel.setBody(body);
        messageModel.setUuid(processSms.getUuid());
        messageModel.setStatus("new");
        messageModel.setType("sms");
        messageModel.setSynced(false);
        messageModel.setOrigin("mobile");
        messageModel.setDate(date);

        Log.d(TAG, "DATE TO INSERT: " + date);

        messagesRepository.insert(messageModel);

        DialogModel dialogModel = dialogsRepository.getDialogByAddress(messageModel.getAddress());
        if (dialogModel == null) {
            dialogModel = new DialogModel();
            dialogModel.setAddress(messageModel.getAddress());
        }

        dialogModel.setLastMessageDate(messageModel.getDate());
        dialogModel.setLastMessage(messageModel.getBody());

        dialogsRepository.insert(dialogModel);
        return Result.success(outputData);
    }

}
