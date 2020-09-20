package com.codebase.inmateapp.services;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.codebase.inmateapp.repositories.MessagesRepository;
import com.codebase.inmateapp.utils.AppGlobals;

public class SMSSendingService extends Service {

    private MessagesRepository messagesRepository;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = AppGlobals.getAppContext();
        messagesRepository = new MessagesRepository((Application) context);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
