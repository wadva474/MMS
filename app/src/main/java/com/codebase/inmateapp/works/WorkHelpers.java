package com.codebase.inmateapp.works;

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.utils.AppGlobals;
import com.codebase.inmateapp.utils.DateUtils;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.concurrent.TimeUnit;

public class WorkHelpers {

    private static final String TAG = WorkHelpers.class.getSimpleName();

    public static void registerWorkManagerToSyncServer(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(AutoSyncServerWork.class)
                .setConstraints(constraints)
//                .setInitialDelay(AppGlobals.getAutoSyncFrequency(), TimeUnit.MINUTES)
                .setInitialDelay(AppGlobals.getAutoSyncFrequency(), TimeUnit.SECONDS)
                .addTag(AutoSyncServerWork.SYNC_MESSAGES_WITH_SERVER_WORK)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(
                AutoSyncServerWork.SYNC_MESSAGES_WITH_SERVER_WORK, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
        Log.d(TAG, "Registering sync from server work");
    }

    public static void registerWorkManagerToSendSMS(Context context, String messageUuid) {
        Data.Builder data = new Data.Builder();
        data.putString(DBTables.MESSAGE_UUID, messageUuid);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SendSMSWork.class)
                .setConstraints(constraints)
                .setInputData(data.build())
                .addTag(SendSMSWork.SEND_SMS_WORK)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(
                SendSMSWork.SEND_SMS_WORK, ExistingWorkPolicy.APPEND, oneTimeWorkRequest);
        Log.d(TAG, "Registering send SMS work");
    }

    public static void registerWorkManagerToSendMMS(Context context, String messageUuid) {
        Data.Builder data = new Data.Builder();
        data.putString(DBTables.MESSAGE_UUID, messageUuid);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SendMMSWork.class)
                .setConstraints(constraints)
                .setInputData(data.build())
                .addTag(SendMMSWork.SEND_MMS_WORK)
                .build();
        // TODO: change to APPEND
        WorkManager.getInstance(context).enqueueUniqueWork(
                SendMMSWork.SEND_MMS_WORK, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
        Log.d(TAG, "Registering send MMS work");

//        CommonUtils.appendMMSLogs(DateUtils.getDateFull(System.currentTimeMillis()) + " MMS WORK Registered");

        FirebaseCrashlytics.getInstance().log(DateUtils.getDateFull(System.currentTimeMillis()) + " MMS WORK Registered");
        FirebaseCrashlytics.getInstance().recordException(new Throwable(DateUtils.getDateFull(System.currentTimeMillis()) + " MMS WORK Registered"));
    }

    public static void registerWorkManagerToUpdateMessage(Context context,
                                                          String messageUuid, String status) {
        Data.Builder data = new Data.Builder();

        data.putString(DBTables.MESSAGE_UUID, messageUuid);
        data.putString(DBTables.MESSAGE_STATUS, status);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UpdateMessageWork.class)
                .setConstraints(constraints)
                .setInputData(data.build())
                .addTag(UpdateMessageWork.UPDATE_MESSAGE_WORK)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(
                UpdateMessageWork.UPDATE_MESSAGE_WORK, ExistingWorkPolicy.APPEND, oneTimeWorkRequest);
        Log.d(TAG, "Registering update message work");
    }

    public static void registerWorkManagerToInsertMessage(Context context,
                                                          String address, String body, long date) {
        Data.Builder data = new Data.Builder();

        data.putString(DBTables.MESSAGE_ADDRESS, address);
        data.putString(DBTables.MESSAGE_BODY, body);
        data.putLong(DBTables.MESSAGE_DATE, date);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(InsertMessageWork.class)
                .setConstraints(constraints)
                .setInputData(data.build())
                .addTag(InsertMessageWork.INSERT_MESSAGE_WORK)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(
                InsertMessageWork.INSERT_MESSAGE_WORK, ExistingWorkPolicy.APPEND, oneTimeWorkRequest);
        Log.d(TAG, "Registering insert message work");
    }

}
