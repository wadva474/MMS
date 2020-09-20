package com.codebase.inmateapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AppGlobals extends Application {

    private static Context context;

    private static final String UNIQUE_ID = "unique_id";
    private static final String FIRST_RUN = "first_run";
    private static final String AUTO_DELETE_MESSAGES = "auto_delete_messages";
    private static final String AUTO_DELETE_PENDING_MESSAGES = "auto_delete_pending_messages";
    private static final String NUMBER_OF_RETRIES_FOR_PENDING_MESSAGES = "number_of_retries_for_pending_messages";
    private static final String AUTO_SYNC = "auto_sync";
    private static final String AUTO_SYNC_FREQUENCY = "auto_sync_frequency";
    private static SharedPreferences sPreferences;

    public static void setFirstRun(boolean firstRun) {
        sPreferences.edit().putBoolean(FIRST_RUN, firstRun).apply();
    }

    public static boolean isFirstRun(){
        return sPreferences.getBoolean(FIRST_RUN, true);
    }

    public static void setAutoDeleteMessages(boolean autoDeleteMessages) {
        sPreferences.edit().putBoolean(AUTO_DELETE_MESSAGES, autoDeleteMessages).apply();
    }

    public static boolean isAutoDeleteMessages() {
        return sPreferences.getBoolean(AUTO_DELETE_MESSAGES, false);
    }

    public static void setAutoDeletePendingMessages(boolean autoDeletePendingMessages) {
        sPreferences.edit().putBoolean(AUTO_DELETE_PENDING_MESSAGES, autoDeletePendingMessages).apply();
    }

    public static boolean isAutoDeletePendingMessages() {
        return sPreferences.getBoolean(AUTO_DELETE_PENDING_MESSAGES, false);
    }

    public static void setNumberOfRetriesForPendingMessages(int numberOfRetriesForPendingMessages) {
        sPreferences.edit().putInt(NUMBER_OF_RETRIES_FOR_PENDING_MESSAGES, numberOfRetriesForPendingMessages).apply();
    }

    public static int getNumberOfRetriesForPendingMessages() {
        return sPreferences.getInt(NUMBER_OF_RETRIES_FOR_PENDING_MESSAGES, 3);
    }

    public static void setAutoSync(boolean autoSync) {
        sPreferences.edit().putBoolean(AUTO_SYNC, autoSync).apply();
    }

    public static boolean isAutoSync() {
        return sPreferences.getBoolean(AUTO_SYNC, false);
    }

    public static void setAutoSyncFrequency(int autoSyncFrequency) {
        sPreferences.edit().putInt(AUTO_SYNC_FREQUENCY, autoSyncFrequency).apply();
    }

    public static int getAutoSyncFrequency() {
        return sPreferences.getInt(AUTO_SYNC_FREQUENCY, 60);
    }

    public static void setUniqueID(String uniqueID) {
        sPreferences.edit().putString(UNIQUE_ID, uniqueID).apply();
    }

    public static String getUniqueId() {
        return sPreferences.getString(UNIQUE_ID, "1");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppGlobals.context = getApplicationContext();
        FirebaseApp.initializeApp(this);

        FirebaseCrashlytics.getInstance().log("initialized"); // Force a crash

    }

    public static Context getAppContext() {
        return AppGlobals.context;
    }
}
