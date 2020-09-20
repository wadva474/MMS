package com.codebase.inmateapp.databases;

public class DBTables {

    // for dialogs
    public static final String TBL_DIALOGS = "table_dialogs";
    public static final String DIALOG_DIALOG_ID = "dialog_dialog_id";
    public static final String DIALOG_IS_READ = "dialog_is_read";
    public static final String DIALOG_RECIPIENT_ID = "dialog_recipient_id";
    public static final String DIALOG_SENDER_ID = "dialog_sender_id";
    public static final String DIALOG_DIALOG_ADDRESS = "dialog_dialog_address";
    public static final String DIALOG_LAST_MESSAGE = "dialog_last_message";
    public static final String DIALOG_LAST_MESSAGE_DATE = "dialog_last_message_date";

    // for messages
    public static final String TBL_MESSAGES = "table_messages";
    public static final String MESSAGE_DIALOG_ID = "message_dialog_id";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_DATE = "message_date";
    public static final String MESSAGE_UUID = "message_uuid";
    public static final String MESSAGE_BODY = "message_body";
    public static final String MESSAGE_IS_READ = "message_is_read";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String MESSAGE_STATUS = "message_status";
    public static final String MESSAGE_SYNCED = "message_synced";
    public static final String MESSAGE_ADDRESS = "message_address";
    public static final String MESSAGE_ORIGIN = "message_origin";
    public static final String MESSAGE_RETRIES = "message_retries";
    public static final String MESSAGE_SENT_RESULT_MESSAGE = "message_sent_result_message";
    public static final String MESSAGE_SENT_RESULT_CODE = "message_sent_result_code";
    public static final String MESSAGE_DELIVERY_RESULT_CODE = "message_delivery_result_code";
    public static final String MESSAGE_DELIVERY_RESULT_MESSAGE = "message_delivery_result_message";
//    public static final String MESSAGE_RECIPIENT_ID = "message_receiver_id";
//    public static final String MESSAGE_SENDER_ID = "message_sender_id";
}
