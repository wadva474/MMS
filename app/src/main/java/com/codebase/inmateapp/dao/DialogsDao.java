package com.codebase.inmateapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.DialogModel;

import java.util.List;

@Dao
public interface DialogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DialogModel dialogModel);

    @Update
    void update(DialogModel dialogModel);

    @Delete
    void delete(DialogModel dialogModel);

    @Query("DELETE FROM " + DBTables.TBL_DIALOGS)
    void deleteAllChatDialogs();

    @Query("SELECT * FROM " + DBTables.TBL_DIALOGS + " ORDER BY " + DBTables.DIALOG_LAST_MESSAGE_DATE + " DESC")
    LiveData<List<DialogModel>> getAllDialogs();

    @Query("SELECT * FROM " + DBTables.TBL_DIALOGS + " WHERE " + DBTables.DIALOG_DIALOG_ADDRESS + " = :address")
    DialogModel getDialogByAddress(String address);
}
