package com.codebase.inmateapp.databases;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.codebase.inmateapp.dao.DialogsDao;
import com.codebase.inmateapp.dao.MessagesDao;
import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.models.MessageModel;

@Database(entities = {
        DialogModel.class,
        MessageModel.class,}, version = 1, exportSchema = false)

//@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "inmate_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public static void clearAllTables(Context context) {
        AppDatabase.getInstance(context).clearAllTables();
    }

    public abstract DialogsDao dialogsDao();

    public abstract MessagesDao messagesDao();

}
