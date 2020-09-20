package com.codebase.inmateapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.codebase.inmateapp.dao.DialogsDao;
import com.codebase.inmateapp.databases.AppDatabase;
import com.codebase.inmateapp.models.DialogModel;

import java.util.List;

public class DialogsRepository {

    private DialogsDao dialogsDao;

    public DialogsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dialogsDao = database.dialogsDao();
    }

//    public LiveData<List<DialogModel>> getAllDialogsFromSystem() {
//
//
//
//
////            QBChatHelper.getInstance().RetrieveDialogs(userId, new QBRequestCallBack<ArrayList<QBChatDialog>>() {
////                @Override
////                public void onSuccess(ArrayList<QBChatDialog> result) {
////
////                    AppGlobals.getInstance().SetDownloadStatus("QBChatDialogRepository");
////                    if (result.size() > 0) {
////                        for (int i = 0; i < result.size(); i++) {
////                            insert(ModelsHelper.GetChatDialogModel(result.get(i)));
////                        }
////                    }
////
////                    callBackData.setValue(new Resource(Resource.Status.SUCCESS, getAllQBChatDialog(), null));
////                }
////            });
//
//        return getAllDialogs();
//    }

    public LiveData<List<DialogModel>> getAllDialogs() {
        return dialogsDao.getAllDialogs();
    }


    public DialogModel getDialogByAddress(String address) {
        return dialogsDao.getDialogByAddress(address);
    }

    public void insert(DialogModel dialogModel) {
        new CRUDDialogAsyncTask(dialogsDao, 0).execute(dialogModel);
    }

    public void update(DialogModel dialogModel) {
        new CRUDDialogAsyncTask(dialogsDao, 1).execute(dialogModel);
    }

    public void delete(DialogModel dialogModel) {
        new CRUDDialogAsyncTask(dialogsDao, 2).execute(dialogModel);
    }

    // ASYNC TASKS /////////////////////////////////////////////////////////////////////////////////
    private static class CRUDDialogAsyncTask extends AsyncTask<DialogModel, Void, Void> {
        private DialogsDao dialogsDao;
        private int mCondition;

        private CRUDDialogAsyncTask(DialogsDao dialogsDao, int pCondition) {
            this.dialogsDao = dialogsDao;
            this.mCondition = pCondition;
        }

        @Override
        protected Void doInBackground(DialogModel... dialogModels) {
            switch (mCondition) {
                case 0:
                    dialogsDao.insert(dialogModels[0]);
                    break;
                case 1:
                    dialogsDao.update(dialogModels[0]);
                    break;
                case 2:
                    dialogsDao.delete(dialogModels[0]);
                    break;
                default:
            }
            return null;
        }
    }

}
