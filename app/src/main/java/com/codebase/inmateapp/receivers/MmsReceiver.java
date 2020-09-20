package com.codebase.inmateapp.receivers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Telephony;
import android.util.Log;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Objects;


public class MmsReceiver extends com.klinker.android.send_message.MmsReceivedReceiver {
    private static final String TAG = "MmsReceiverTag";
    //Uri for mms content
    private final Uri CONTENT_URI_PART = Uri.parse("content://mms/part");
    String mmsID, where, senderNumber, partId;
    Bitmap bitmap;
    String body;

    @Override
    public void onMessageReceived(Context context, Uri uri) {
        //Once an MMS is received, Order by date
        Cursor messageCursor = getLastMmsMessage(context);

        //Gets the message ID of the Latest MMS message
        if (messageCursor != null) {
            try {
                if (messageCursor.moveToFirst()) {
                    mmsID = messageCursor.getString(messageCursor.getColumnIndex(Telephony.MmsSms._ID));
                    where = "_id = " + mmsID;
                }

            } catch (Exception e) {
                System.out.println("MMSMonitor :: startMMSMonitoring Exception== " + e.getMessage());
            } finally {
                messageCursor.close();
            }
        }


        //Cursor for querying for latest Mms content
        Cursor cursor = context.getContentResolver().query
                (CONTENT_URI_PART, null, where, null, null);


        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        partId = cursor.getString(cursor.getColumnIndex(Telephony.Mms._ID));
                        String type = cursor.getString(cursor.getColumnIndex(Telephony.Mms.CONTENT_TYPE));
                        if ("text/plain".equals(type)) {
                            String data = cursor.getString(cursor
                                    .getColumnIndex("_data"));
                            if (data != null) {
                                body = getMmsText(context, partId);
                            } else {
                                body = cursor.getString(cursor.getColumnIndex("text"));
                            }
                        } else if ("image/jpeg".equals(type)
                                || "image/bmp".equals(type) || "image/gif".equals(type)
                                || "image/jpg".equals(type) || "image/png".equals(type)) {
                            bitmap = getMmsImage(context, partId);
                            senderNumber = getAddressNumber(context, mmsID);
                        }
                    }
                    while (cursor.moveToNext());
                }
            } catch (Exception e) {
                System.out.println("MMSMonitor :: startMMSMonitoring Exception== " + e.getMessage());
            } finally {
                cursor.close();
            }
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire(5000);
        sendMessage(context);
    }

    public static Cursor getLastMmsMessage(Context context) {
        Uri uri = Uri.parse("content://mms");
        String sortOrder = "date desc limit 1";
        return getMmsMessage(context, uri, sortOrder);
    }

    public static Cursor getMmsMessage(Context context, Uri uri, String sortOrder) {
        String[] projection = new String[]{
                Telephony.MmsSms._ID,
                Telephony.Sms.DATE,
                Telephony.Sms.READ,
                Telephony.Mms.MESSAGE_BOX,
                Telephony.Mms.MESSAGE_TYPE
        };

        return context.getContentResolver().query(uri, projection, null, null, sortOrder);
    }


    @Override
    public void onError(Context context, String s) {

    }



    public void sendMessage(Context mContext) {
        new Thread(() -> {
            Settings settings = new Settings();
            settings.setUseSystemSending(true);
            Transaction transaction = new Transaction(mContext, settings);
            Message message = new Message(body, senderNumber);
            message.setImage(bitmap);
            transaction.sendNewMessage(message, Long.parseLong(mmsID));
        }).start();


    }


    public String getMmsText(Context context, String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException ignored) {
            Log.d(TAG, Objects.requireNonNull(ignored.getMessage()));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }
        return sb.toString();
    }

    public Bitmap getMmsImage(Context context, String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException ignored) {
            Log.e(TAG, ignored.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }
        return bitmap;
    }

    private String getAddressNumber(Context context, String id) {
        String selectionAdd = "msg_id=" + id;
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = context.getContentResolver().query(uriAddress, null,
                selectionAdd, null, null);
        String name = null;
        assert cAdd != null;
        if (cAdd.moveToFirst()) {
            do {
                String number = cAdd.getString(cAdd.getColumnIndex("address"));
                Log.d(TAG, "Sender of mms is " + number);
                if (number != null) {
                    try {
                        name = number;
                    } catch (NumberFormatException nfe) {
                        name = number;
                    }
                }
            } while (cAdd.moveToNext());
            cAdd.close();

        }
        return name;
    }
}

