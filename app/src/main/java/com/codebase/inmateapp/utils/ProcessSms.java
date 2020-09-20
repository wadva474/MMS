package com.codebase.inmateapp.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.codebase.inmateapp.models.MessageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//@Singleton
public class ProcessSms {

    private static final String TAG = ProcessSms.class.getSimpleName();
    public static String DELIVERED = "SMS_DELIVERED";

        public static final String SENT_SMS_BUNDLE = "sent";

        public static final String DELIVERED_SMS_BUNDLE = "delivered";

        private static final String SMS_CONTENT_URI = "content://sms/conversations/";

        private static final String SMS_CONTENT_INBOX = "content://sms/inbox";

        private static String SENT = "SMS_SENT";

        private static final String CLASS_TAG = ProcessSms.class.getSimpleName();

        private Context mContext;

//        @Inject
        public ProcessSms(Context context) {
            mContext = context;
        }

        /**
         * Send message as SMS to a phone number. When sendDeliveryReport is set to true
         * the message {@link SmsMessage} will be sent as part of the delivery report Intent. So you
         * can know which message actually got delivered.
         *
         * @param message            The message of the SMS
         * @param sendDeliveryReport Whether to send delivery report or not
         */
        public void sendSms(MessageModel message, boolean sendDeliveryReport) {
            Log.d(CLASS_TAG, String.format("sendSms(): Sends SMS to a number: sendTo: %s message: %s",
                    message.getAddress(),
                    message.getBody()));
            ArrayList<PendingIntent> sentIntents = new ArrayList<>();
            ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(message.getBody());

            for (int i = 0; i < parts.size(); i++) {

                Intent sentMessageIntent = new Intent(SENT);
                sentMessageIntent.putExtra(SENT_SMS_BUNDLE, message);

                PendingIntent sentIntent = PendingIntent
                        .getBroadcast(mContext, (int) System.currentTimeMillis(), sentMessageIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                Intent delivered = new Intent(DELIVERED);
                delivered.putExtra(DELIVERED_SMS_BUNDLE, message);

                PendingIntent deliveryIntent = PendingIntent
                        .getBroadcast(mContext, (int) System.currentTimeMillis(), delivered,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                sentIntents.add(sentIntent);

                deliveryIntents.add(deliveryIntent);
            }
            if (sendDeliveryReport) {
                sms.sendMultipartTextMessage(message.getAddress(), null, parts, sentIntents,
                        deliveryIntents);
                return;
            }

            sms.sendMultipartTextMessage(message.getAddress(), null, parts, sentIntents, null);
        }


    /**
     * Delete SMS from the message app inbox
     *
     * @param messageModel The message to be deleted
     * @return true if deleted otherwise false
     */
    public boolean delSmsFromInbox(MessageModel messageModel) {
        Log.d(CLASS_TAG, "delSmsFromInbox(): Delete SMS message app inbox");
        final long threadId = getThreadId(messageModel);
        if (threadId >= 0) {
            Uri smsUri = ContentUris.withAppendedId(SmsQuery.SMS_CONVERSATION_URI,
                            threadId);
            int rowsDeleted = mContext.getContentResolver().delete(smsUri, null, null);
            if (rowsDeleted > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

        /**
         * Import messages from the messages app's table with a limitation of 10 messages.
         *
         * @return An empty list of {@link SmsMessage} when know message is imported or the total number
         * of messages imported.
         */
        public List<MessageModel> importMessages() {
            Log.d(CLASS_TAG, "import messages from messages app");
//            Uri uriSms = Uri.parse(SMS_CONTENT_INBOX);
//            uriSms = uriSms.buildUpon().appendQueryParameter("LIMIT", "10").build();
//            String[] projection = {
//                    "_id", "address", "date", "body"
//            };

            Cursor c = mContext.getContentResolver().query(Telephony.Sms.CONTENT_URI, SmsQuery.PROJECTION, null,
                    null, Telephony.Sms.DEFAULT_SORT_ORDER);
            List<MessageModel> messages = new ArrayList<>();
            if (c != null && c.getCount() > 0) {
                try {
                    if (c.moveToFirst()) {
                        do {
                            MessageModel message = new MessageModel();

                            final long messageDate = c.getLong(c.getColumnIndex("date"));
                            message.setDate(messageDate);
                            message.setId(c.getString(c.getColumnIndex("_id")));
                            message.setAddress(c.getString(c.getColumnIndex("address")));
                            message.setBody(c.getString(c.getColumnIndex("body")));
                            message.setUuid(getUuid());
                            // Treat imported messages as failed
                            message.setStatus("failed");
                            message.setOrigin("mobile");

                            messages.add(message);
                        } while (c.moveToNext());
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            return messages;
        }

        /**
         * Tries to locate the thread id given the address (phone number or email) of the message
         * sender.
         *
         * @return the thread id
         */
        private long getThreadId(MessageModel messageModel) {
            Log.d(CLASS_TAG, "getId(): thread id");
            StringBuilder sb = new StringBuilder();
            sb.append(
                    Telephony.Sms.Inbox.ADDRESS + "=" + DatabaseUtils
                            .sqlEscapeString(messageModel.getAddress())
                            + " AND ");
            sb.append(Telephony.Sms.Inbox.BODY + "=" + DatabaseUtils
                    .sqlEscapeString(messageModel.getBody()));
            Cursor c = mContext.getContentResolver()
                    .query(SmsQuery.INBOX_CONTENT_URI, SmsQuery.PROJECTION, sb.toString(), null,
                            SmsQuery.SORT_ORDER);
            if (c != null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    long threadId = c.getLong(c.getColumnIndex(Telephony.Sms.Inbox.THREAD_ID));
                    c.close();
                    return threadId;
                }
                c.close();
            }
            return 0;
        }


        /**
         * A basic SmsQuery based on android.provider.Telephony.Sms.Inbox
         */
        @SuppressLint("NewApi")
        private interface SmsQuery {

            Uri INBOX_CONTENT_URI = Telephony.Sms.Inbox.CONTENT_URI;
            Uri SMS_CONVERSATION_URI = Telephony.Sms.Conversations.CONTENT_URI;
            String[] PROJECTION = {
                    Telephony.Sms._ID,
                    Telephony.Sms.ADDRESS,
                    Telephony.Sms.BODY,
                    Telephony.Sms.DATE,
            };
            String SORT_ORDER = Telephony.Sms.Inbox.DEFAULT_SORT_ORDER;
        }

        public String getUuid() {
            return UUID.randomUUID().toString();
        }

}
