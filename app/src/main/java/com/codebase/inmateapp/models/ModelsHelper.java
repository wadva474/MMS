package com.codebase.inmateapp.models;

import android.database.Cursor;

public class ModelsHelper {

    // For messages from device
    public static MessageModel getMessageModel(Cursor c, String uuid) {
        MessageModel res = new MessageModel();

        final long messageDate = c.getLong(c.getColumnIndex("date"));
        res.setDate(messageDate);
        res.setId(c.getString(c.getColumnIndex("_id")));
        res.setAddress(c.getString(c.getColumnIndex("address")));
        res.setBody(c.getString(c.getColumnIndex("body")));
        res.setUuid(uuid);
//        res.setType(c.getInt(c.getColumnIndex("type")));
//        // Treat imported messages as failed
//        res.setStatus(c.getInt(c.getColumnIndex("status")));
        return res;
    }

    // For new messages from server
    public static MessageModel getMessageModel(ResponseModel.Message message) {
        MessageModel res = new MessageModel();
        res.setDate(System.currentTimeMillis());
        res.setUuid(message.getUuid());
        res.setBody(message.getMessage());
        res.setStatus("pending");
        res.setType("sms");
        res.setSynced(false);
        res.setAddress(message.getTo());
        res.setOrigin("server");

        return res;
    }

}
