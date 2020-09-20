package com.codebase.inmateapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codebase.inmateapp.databases.DBTables;

@Entity(tableName = DBTables.TBL_DIALOGS)
public class DialogModel {

//    @NonNull
//    @ColumnInfo(name = DBTables.DIALOG_DIALOG_ID)
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @ColumnInfo(name = DBTables.DIALOG_RECIPIENT_ID)
//    private String recipientId;
//
//    @ColumnInfo(name = DBTables.DIALOG_SENDER_ID)
//    private String senderId;

    @ColumnInfo(name = DBTables.DIALOG_LAST_MESSAGE_DATE)
    private long lastMessageDate;

    @ColumnInfo(name = DBTables.DIALOG_LAST_MESSAGE)
    private String lastMessage;

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = DBTables.DIALOG_DIALOG_ADDRESS)
    private String address;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

//    public String getRecipientId() {
//        return recipientId;
//    }
//
//    public void setRecipientId(String recipientId) {
//        this.recipientId = recipientId;
//    }
//
//    public String getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(String senderId) {
//        this.senderId = senderId;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
