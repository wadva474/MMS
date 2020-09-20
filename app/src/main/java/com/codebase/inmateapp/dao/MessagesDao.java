package com.codebase.inmateapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.MessageModel;

import java.util.List;

@Dao
public interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MessageModel messageModel);

    @Update
    void update(MessageModel messageModel);

    @Delete
    void delete(MessageModel messageModel);

    @Query("DELETE FROM " + DBTables.TBL_MESSAGES)
    void deleteAllChatDialogs();

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES + " ORDER BY " + DBTables.MESSAGE_DATE + " ASC")
    List<MessageModel> getAllMessages();

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_ADDRESS + " = :dialogAddress")
//            + " ORDER BY " + DBTables.MESSAGE_DATE + " DESC")
    LiveData<List<MessageModel>> getSpecificDialogMessages(String dialogAddress);

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_UUID + " = :uuid")
    MessageModel getMessageWithSpecificUuid(String uuid);

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_DIALOG_ID + " = :dialogId"
            + " ORDER BY " + DBTables.MESSAGE_DATE + " DESC LIMIT 1")
    LiveData<MessageModel> getLastMessage(String dialogId);

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_STATUS +  " LIKE '%pending%' ")
    LiveData<List<MessageModel>> getAllPendingMessages();

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_SYNCED +  " = 0 "
            + " AND " + DBTables.MESSAGE_ORIGIN +  " LIKE '%mobile%' ")
    List<MessageModel> getAllMessagesToBePublished();

    @Query("SELECT * FROM " + DBTables.TBL_MESSAGES
            + " WHERE " + DBTables.MESSAGE_SYNCED +  " = 0 "
            + " AND " + DBTables.MESSAGE_ORIGIN +  " LIKE '%server%' "
            + " AND " + DBTables.MESSAGE_STATUS +  " LIKE '%sent%' "
            + " OR " + DBTables.MESSAGE_STATUS +  " LIKE '%not_delivered%' "
            + " OR " + DBTables.MESSAGE_STATUS +  " LIKE '%delivered%' ")
    List<MessageModel> getAllMessagesToBeUpdated();
}