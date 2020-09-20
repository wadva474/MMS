package com.codebase.inmateapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();
    private static final String LOGS_DIRECTORY_NAME = "InmateMedia/Logs";


    /**
     * Gets the current users phone number
     *
     * @param context is the context of the activity or service
     * @return a string of the phone number on the device
     */
    @SuppressLint("MissingPermission")
    public static String getMyPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    public static void appendMMSLogs(String text) {

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), LOGS_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + LOGS_DIRECTORY_NAME + " directory");
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "mmslogfile.txt");
        try {
            mediaFile.createNewFile();
            if (mediaFile.exists()) {
                try {
                    //BufferedWriter for performance, true to set append to file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(mediaFile, true));
                    buf.append(text);
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
