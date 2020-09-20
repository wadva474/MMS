package com.codebase.inmateapp.receivers;

import android.content.Context;
import android.content.Intent;

import com.google.android.mms.pdu_alt.DeliveryInd;
import com.google.android.mms.pdu_alt.GenericPdu;
import com.google.android.mms.pdu_alt.PduHeaders;
import com.google.android.mms.pdu_alt.PduParser;

public class MmsSentReceiver extends com.klinker.android.send_message.MmsSentReceiver {
    @Override
    public void onMessageStatusUpdated(Context context, Intent intent, int resultCode) {
        super.onMessageStatusUpdated(context, intent, resultCode);

        byte[] pushData = intent.getByteArrayExtra("data");
        PduParser parser = new PduParser(pushData);
        GenericPdu pdu = parser.parse();

        int status = ((DeliveryInd) pdu).getStatus();

        switch (status) {
            case PduHeaders.STATUS_RETRIEVED: {
                //message delivered. update storage
            }

            case PduHeaders.STATUS_EXPIRED: {
                //message not delivered. status expired
            }
            case PduHeaders.STATUS_REJECTED: {

            }
            case PduHeaders.STATUS_DEFERRED: {

            }
            case PduHeaders.STATUS_UNRECOGNIZED: {

            }
            case PduHeaders.STATUS_INDETERMINATE: {

            }
            case PduHeaders.STATUS_FORWARDED: {
                //mms not delivered. Forwarded
            }
            case PduHeaders.STATUS_UNREACHABLE: {
                //message not delivered receiver unreachable
            }
            default:
                //Unknown Error
        }

    }

}


