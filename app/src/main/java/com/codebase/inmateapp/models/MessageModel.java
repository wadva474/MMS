package com.codebase.inmateapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codebase.inmateapp.databases.DBTables;

@Entity(tableName = DBTables.TBL_MESSAGES)
public class MessageModel implements Parcelable {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = DBTables.MESSAGE_UUID)
    private String uuid;

    @ColumnInfo(name = DBTables.MESSAGE_ID)
    private String id;

    @ColumnInfo(name = DBTables.MESSAGE_BODY)
    private String body;

//    @ColumnInfo(name = DBTables.MESSAGE_RECIPIENT_ID)
//    private String recipientId;
//
//    @ColumnInfo(name = DBTables.MESSAGE_SENDER_ID)
//    private String senderId;

    @ColumnInfo(name = DBTables.MESSAGE_ADDRESS)
    private String address;

    @ColumnInfo(name = DBTables.MESSAGE_DATE)
    private long date;

    @ColumnInfo(name = DBTables.MESSAGE_STATUS)
    private String status;

    @ColumnInfo(name = DBTables.MESSAGE_SYNCED)
    private boolean synced;

    @ColumnInfo(name = DBTables.MESSAGE_DIALOG_ID)
    private int dialogId;

    @ColumnInfo(name = DBTables.MESSAGE_ORIGIN)
    private String origin;

    @ColumnInfo(name = DBTables.MESSAGE_TYPE)
    private String type;

    @ColumnInfo(name = DBTables.MESSAGE_RETRIES)
    private int retries;

    @ColumnInfo(name = DBTables.MESSAGE_SENT_RESULT_CODE)
    private int sentResultCode;

    @ColumnInfo(name = DBTables.MESSAGE_SENT_RESULT_MESSAGE)
    private String sentResultMessage;

    @ColumnInfo(name = DBTables.MESSAGE_DELIVERY_RESULT_CODE)
    private int deliveryResultCode;

    @ColumnInfo(name = DBTables.MESSAGE_DELIVERY_RESULT_MESSAGE)
    private String deliveryResultMessage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String messageType) {
//        this.type = messageType;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getSentResultCode() {
        return sentResultCode;
    }

    public void setSentResultCode(int sentResultCode) {
        this.sentResultCode = sentResultCode;
    }

    public String getSentResultMessage() {
        return sentResultMessage;
    }

    public void setSentResultMessage(String sentResultMessage) {
        this.sentResultMessage = sentResultMessage;
    }

    public int getDeliveryResultCode() {
        return deliveryResultCode;
    }

    public void setDeliveryResultCode(int deliveryResultCode) {
        this.deliveryResultCode = deliveryResultCode;
    }

    public String getDeliveryResultMessage() {
        return deliveryResultMessage;
    }

    public void setDeliveryResultMessage(String deliveryResultMessage) {
        this.deliveryResultMessage = deliveryResultMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public MessageModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.id);
        dest.writeString(this.body);
        dest.writeString(this.address);
        dest.writeLong(this.date);
        dest.writeString(this.status);
        dest.writeByte(this.synced ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dialogId);
        dest.writeString(this.origin);
        dest.writeString(this.type);
        dest.writeInt(this.retries);
        dest.writeInt(this.sentResultCode);
        dest.writeString(this.sentResultMessage);
        dest.writeInt(this.deliveryResultCode);
        dest.writeString(this.deliveryResultMessage);
    }

    protected MessageModel(Parcel in) {
        this.uuid = in.readString();
        this.id = in.readString();
        this.body = in.readString();
        this.address = in.readString();
        this.date = in.readLong();
        this.status = in.readString();
        this.synced = in.readByte() != 0;
        this.dialogId = in.readInt();
        this.origin = in.readString();
        this.type = in.readString();
        this.retries = in.readInt();
        this.sentResultCode = in.readInt();
        this.sentResultMessage = in.readString();
        this.deliveryResultCode = in.readInt();
        this.deliveryResultMessage = in.readString();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}
