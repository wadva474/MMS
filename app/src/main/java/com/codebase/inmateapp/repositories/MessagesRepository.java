package com.codebase.inmateapp.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.codebase.inmateapp.dao.MessagesDao;
import com.codebase.inmateapp.databases.AppDatabase;
import com.codebase.inmateapp.interfaces.ApiCallback;
import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.models.ModelsHelper;
import com.codebase.inmateapp.models.ResponseModel;
import com.codebase.inmateapp.network.RetrofitUtility;
import com.codebase.inmateapp.utils.AppGlobals;
import com.codebase.inmateapp.utils.ProcessSms;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesRepository {

    private MessagesDao messagesDao;
    private DialogsRepository dialogsRepository;
    private ProcessSms processSms;

    private final String TAG = "MsgRepo";

    public MessagesRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        messagesDao = database.messagesDao();
        processSms = new ProcessSms(application);

        dialogsRepository = new DialogsRepository(application);
    }

    public LiveData<MessageModel> getLastMessage(String dialogId) {
        return messagesDao.getLastMessage(dialogId);
    }

    public LiveData<List<MessageModel>> getSpecificDialogMessages(String dialogAddress) {
        return messagesDao.getSpecificDialogMessages(dialogAddress);
    }

    public List<MessageModel> getAllMessagesToBePublished() {
        return messagesDao.getAllMessagesToBePublished();
    }

    public List<MessageModel> getAllMessagesToBeUpdated() {
        return messagesDao.getAllMessagesToBeUpdated();
    }

    public MessageModel getMessageWithSpecificUuid(String uuid) {
        return messagesDao.getMessageWithSpecificUuid(uuid);
    }

    public LiveData<List<MessageModel>> getAllPendingMessages() {
        return messagesDao.getAllPendingMessages();
    }

    public void getMessagesFromServer(ApiCallback apiCallback) {
        Call<ResponseModel> getMessagesToSendServer = RetrofitUtility.getInstance().getMessagesFromServer("cfcc69fa_" + AppGlobals.getUniqueId(), "send");
        getMessagesToSendServer.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().getPayload().isSuccess()) {

                    Log.d(TAG, "RES: " + response.body().getPayload());
                    List<ResponseModel.Message> messageListFromServer = response.body().getPayload().getMessages();
                    List<MessageModel> messageModelList = new ArrayList<>();
                    if (messageListFromServer != null && messageListFromServer.size() > 0) {
                        for (int i = 0; i < messageListFromServer.size(); i++) {
                            MessageModel messageModel = ModelsHelper.getMessageModel(messageListFromServer.get(i));

                            Log.d(TAG, "UUID: " + messageModel.getUuid());

                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            Callable<String> callable = () -> {
                                DialogModel dialogModel = dialogsRepository.getDialogByAddress(messageModel.getAddress());
                                System.out.println("Entered Callable");
                                if (dialogModel == null) {
                                    Log.d(TAG, "DIALOG BY ADDRESS IS NULL");
                                    dialogModel = new DialogModel();
                                    dialogModel.setAddress(messageModel.getAddress());
                                    dialogModel.setLastMessage(messageModel.getBody());
                                    dialogModel.setLastMessageDate(messageModel.getDate());
                                    dialogsRepository.insert(dialogModel);
                                } else {
                                    dialogModel.setLastMessage(messageModel.getBody());
                                    dialogModel.setLastMessageDate(messageModel.getDate());
                                    dialogsRepository.insert(dialogModel);
                                }
                                return "Success";
                            };

                            Future<String> future = executorService.submit(callable);
                            String result = null;
                            try {
                                result = future.get();
                                executorService.shutdown();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(result);

                            messageModelList.add(messageModel);
                            insert(messageModel);
                        }
                        Log.d(TAG, "FromServer: SUCCESS - " + messageListFromServer.size());
                    } else {
                        Log.d(TAG, "FromServer: SUCCESS - EMPTY");
                    }
                    apiCallback.onSuccess(messageModelList);
                } else {
                        Log.e(TAG, "FromServer: FAILED - " + response.body().getPayload().getError());
                        apiCallback.onError("FromServer: FAILED - " + response.body().getPayload().getError());
                    }

//                } else {
//                    Log.e(TAG, "FromServer: FAILED - " + response.message());
//                    apiCallback.onError("FromServer: FAILED - " + response.message());
//                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, "FromServer: FAILED - " + t.getLocalizedMessage());
                apiCallback.onError("FromServer: FAILED - " + t.getLocalizedMessage());
            }
        });
    }

    public void updateMessagesOnServer(ApiCallback apiCallback) {
        List<MessageModel> messageModelListToUpdate = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            System.out.println("Entered Callable for Update");
            List<MessageModel> messageModelList = getAllMessagesToBeUpdated();
            messageModelListToUpdate.addAll(messageModelList);
            return "Success";
        };

        Future<String> future = executorService.submit(callable);
        String result = null;
        try {
            result = future.get();
            executorService.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result);

        if (messageModelListToUpdate.size() > 0) {

            ArrayList<String> sentMessagesIdsList = new ArrayList<>();

            for (int i = 0; i < messageModelListToUpdate.size(); i++) {
                sentMessagesIdsList.add('"' + messageModelListToUpdate.get(i).getUuid() + '"');
            }

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("device_id", AppGlobals.getUniqueId())
                    .addFormDataPart("sent_message_ids", sentMessagesIdsList.toString())
                    .build();


            Call<ResponseModel> updateMessagesOnServer = RetrofitUtility.getInstance().
                    updateMessagesOnServer("cfcc69fa", "sent", requestBody);
            updateMessagesOnServer.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getPayload().isSuccess()) {
                            Log.d(TAG, "UpdateOnServer: SUCCESS");
                            for (int i = 0; i < messageModelListToUpdate.size(); i++) {
                                MessageModel messageModel = messageModelListToUpdate.get(i);
                                messageModel.setSynced(true);
                                insert(messageModel);
                            }
                            apiCallback.onSuccess("True");
                        } else {
                            Log.e(TAG, "UpdateOnServer: FAILED - " + response.body().getPayload().getError());
                            apiCallback.onError("UpdateOnServer: FAILED - " + response.body().getPayload().getError());
                        }
                    } else {
                        Log.e(TAG, "UpdateOnServer: FAILED - " + response.message());
                        apiCallback.onError("UpdateOnServer: FAILED - " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e(TAG, "UpdateOnServer: FAILED - " + t.getLocalizedMessage());
                    apiCallback.onError("UpdateOnServer: FAILED - " + t.getLocalizedMessage());
                }
            });

        } else {
            apiCallback.onSuccess("LIST IS EMPTY");
        }
    }

    public void publishMessagesToServer(ApiCallback apiCallback) {

        List<MessageModel> messageModelListToPublish = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            System.out.println("Entered Callable");
            List<MessageModel> messageModelList = getAllMessagesToBePublished();
            messageModelListToPublish.addAll(messageModelList);
            return "Success";
        };

        Future<String> future = executorService.submit(callable);
        String result = null;
        try {
            result = future.get();
            executorService.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result);

        if (messageModelListToPublish.size() > 0) {
            JsonArray body = new JsonArray();
            for (int i = 0; i < messageModelListToPublish.size(); i++) {
                MessageModel messageModel = messageModelListToPublish.get(i);
                JsonObject object = new JsonObject();
                object.addProperty("message_id", messageModel.getUuid());
                object.addProperty("message", messageModel.getBody());
                object.addProperty("from", messageModel.getAddress());
                object.addProperty("message_type", messageModel.getType());
                body.add(object);
            }

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("device_id", AppGlobals.getUniqueId())
                    .addFormDataPart("messages", body.toString())
                    .build();

            Call<ResponseModel> publishMessagesToServer = RetrofitUtility.getInstance().publishMessagesToServer(
                    "cfcc69fa", "incoming", requestBody);
            publishMessagesToServer.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getPayload().isSuccess()) {
                            Log.d(TAG, "PublishToServer: SUCCESS");
                            for (int i = 0; i < messageModelListToPublish.size(); i++) {
                                MessageModel messageModel = messageModelListToPublish.get(i);
                                messageModel.setSynced(true);
                                insert(messageModel);
                            }
                            apiCallback.onSuccess("True");
                        } else {
                            Log.e(TAG, "PublishToServer: FAILED - " + response.body().getPayload().getError());
                            apiCallback.onError("PublishToServer: FAILED - " + response.body().getPayload().getError());
                        }
                    } else {
                        Log.e(TAG, "PublishToServer: FAILED - " + response.message());
                        apiCallback.onError("PublishToServer: FAILED - " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e(TAG, "PublishToServer: FAILED - " + t.getLocalizedMessage());
                    apiCallback.onError("PublishToServer: FAILED - " + t.getLocalizedMessage());
                }
            });
        } else {
            apiCallback.onSuccess("LIST IS EMPTY");
        }

    }

    public void getMessagesFromDevice() {
        List<MessageModel> messageModelList = processSms.importMessages();

        List<DialogModel> dialogModelList = new ArrayList<>();

        for(int i = 0; i < messageModelList.size(); i++) {
            DialogModel dialogModel = null;
            MessageModel messageModel = messageModelList.get(i);
            for (int j = 0; j < dialogModelList.size(); j++) {
                if (dialogModelList.get(j).getAddress().equalsIgnoreCase(messageModelList.get(i).getAddress())) {

                    Log.d(TAG, "DialogAlreadyExists");
                    dialogModel = dialogModelList.get(j);
                    break;
                }
            }
            if (dialogModel == null) {
                dialogModel = new DialogModel();
                dialogModel.setAddress(messageModel.getAddress());
                dialogModel.setLastMessage(messageModel.getBody());
                dialogModel.setLastMessageDate(messageModel.getDate());
                dialogModelList.add(dialogModel);
                dialogsRepository.insert(dialogModel);
            }

//            if (!dialogModelList.contains(dialogModel)) {
//            }

            Log.d(TAG, "MsgId: " + messageModel.getId());
            Log.d(TAG, "MsgAddress: " + messageModel.getAddress());
            Log.d(TAG, "TIME: " + messageModel.getDate());
            insert(messageModel);
    }
    }

    public void insert(MessageModel messageModel) {
        new CRUDMessageAsyncTask(messagesDao, 0).execute(messageModel);
    }

    public void update(MessageModel messageModel) {
        new CRUDMessageAsyncTask(messagesDao, 1).execute(messageModel);
    }

    public void delete(MessageModel messageModel) {
        new CRUDMessageAsyncTask(messagesDao, 2).execute(messageModel);
    }

    // ASYNC TASKS /////////////////////////////////////////////////////////////////////////////////
    private static class CRUDMessageAsyncTask extends AsyncTask<MessageModel, Void, Void> {
        private MessagesDao messagesDao;
        private int mCondition;

        private CRUDMessageAsyncTask(MessagesDao messagesDao, int pCondition) {
            this.messagesDao = messagesDao;
            this.mCondition = pCondition;
        }

        @Override
        protected Void doInBackground(MessageModel... messageModels) {
            switch (mCondition) {
                case 0:
                    messagesDao.insert(messageModels[0]);
                    break;
                case 1:
                    messagesDao.update(messageModels[0]);
                    break;
                case 2:
                    messagesDao.delete(messageModels[0]);
                    break;
                default:
            }

            return null;
        }
    }

}
