package com.codebase.inmateapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.codebase.inmateapp.works.WorkHelpers;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "Received: " + bundle.toString());

        SmsMessage[] messages = getMessagesFromIntent(intent);
        if (messages != null) {
            SmsMessage sms = messages[0];
            String address = sms.getOriginatingAddress();
            if (address != null && (address.contains("+1") || address.contains("+92"))) {
                address = address.replace("+1", "");
                address = address.replace("+92", "");
            }
//            PhoneNumberUtil.getInstance().format(PhoneNumberUtils.formatNumber(address, countryCode),
//                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            long date = sms.getTimestampMillis();
            String body;

            if (messages.length == 1 || sms.isReplace()) {
                body = sms.getDisplayMessageBody();
            } else {
                StringBuilder bodyText = new StringBuilder();
                for (int i = 0; i < messages.length; i++) {
                    bodyText.append(messages[i].getMessageBody());
                }
                body = bodyText.toString();
            }
            WorkHelpers.registerWorkManagerToInsertMessage(context, address, body, date);
        }
    }


    /**
     * Get the SMS message.
     *
     * @param intent - The SMS message intent.
     * @return SmsMessage
     */
    private SmsMessage[] getMessagesFromIntent(Intent intent) {

        Log.d(TAG, "getMessagesFromIntent(): getting SMS message");

        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

        if (messages == null) {
            return null;
        }

        if (messages.length == 0) {
            return null;
        }

        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;

        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }



}
