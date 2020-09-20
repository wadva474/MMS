package com.codebase.inmateapp.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.utils.ProcessSms;
import com.codebase.inmateapp.works.WorkHelpers;

public class SMSSentReceiver extends BroadcastReceiver {

    private final String TAG = "SntSMSRcvr";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "MESSAGE SENT");
        MessageModel messageModel = intent.getParcelableExtra(ProcessSms.SENT_SMS_BUNDLE);
        final int resultCode = getResultCode();
        boolean sentSuccess = false;
        Log.d(TAG, "smsSentReceiver onReceive result: " + resultCode);
        String resultMessage;
        switch (resultCode) {
            case 133404:
                /**
                 * HTC devices issue
                 * http://stackoverflow.com/questions/7526179/sms-manager-keeps-retrying-to-send-sms-on-htc-desire/7685238#7685238
                 */

                Log.e(TAG, "HTC desire issue: " + resultCode);

//                logActivities(context.getResources().getString(
//                        R.string.sms_not_delivered_htc_device_retry));
                // This intentionally returns, while the rest below does break and more after.
                return;
            case Activity.RESULT_OK:
                resultMessage = context.getResources().getString(R.string.sms_status_success);
                sentSuccess = true;
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                resultMessage = context.getResources()
                        .getString(R.string.sms_delivery_status_failed);
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                resultMessage = context.getResources()
                        .getString(R.string.sms_delivery_status_no_service);
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                resultMessage = context.getResources()
                        .getString(R.string.sms_delivery_status_null_pdu);
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                resultMessage = context.getResources()
                        .getString(R.string.sms_delivery_status_radio_off);
                break;
            default:
                resultMessage = context.getResources()
                        .getString(R.string.sms_not_delivered_unknown_error);
                break;
        }

        Log.d(TAG, "Result Message: " + resultMessage);

        if (messageModel != null) {
            messageModel.setSentResultMessage(resultMessage);
            messageModel.setSentResultCode(resultCode);
            Log.d(TAG, "message sent: " + messageModel);

            String status = sentSuccess ? "sent":"failed";

            // Update this in a worker to guarantee it will run
            WorkHelpers.registerWorkManagerToUpdateMessage(context, messageModel.getUuid(), status);
        } else {
            Log.e(TAG, "Message Model is Null!");
        }
    }

}
