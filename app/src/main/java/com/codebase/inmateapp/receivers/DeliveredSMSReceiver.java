package com.codebase.inmateapp.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.utils.ProcessSms;
import com.codebase.inmateapp.works.WorkHelpers;

public class DeliveredSMSReceiver extends BroadcastReceiver {

    private static final String TAG = "DlvrSMSRcvr";
    private MessageModel messageModel = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "MESSAGE DELIVERED");
        int result = getResultCode();
        messageModel = (MessageModel) intent.getParcelableExtra(
                ProcessSms.DELIVERED_SMS_BUNDLE);
        String resultMessage = "";
        String status = "not_delivered";
        switch (result) {
            case Activity.RESULT_OK:
                resultMessage = context.getResources().getString(R.string.sms_delivered);
                Toast.makeText(context, context.getResources().getString(R.string.sms_delivered),
                        Toast.LENGTH_LONG);
                status = "delivered";
                break;
            case Activity.RESULT_CANCELED:
                resultMessage = context.getResources().getString(R.string.sms_not_delivered);
                Toast.makeText(context,
                        context.getResources().getString(R.string.sms_not_delivered),
                        Toast.LENGTH_LONG);
                status = "not_delivered";
                break;
        }

        if (messageModel != null) {
            messageModel.setDeliveryResultMessage(resultMessage);
            messageModel.setDeliveryResultCode(result);

            // Update this in a worker to guarantee it will run
            WorkHelpers.registerWorkManagerToUpdateMessage(context, messageModel.getUuid(), status);
        }
    }
}
