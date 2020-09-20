package com.codebase.inmateapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.codebase.inmateapp.interfaces.ApiCallback;
import com.codebase.inmateapp.models.MessageModel;
import com.codebase.inmateapp.repositories.MessagesRepository;

import java.util.List;

public class MessagesViewModel extends AndroidViewModel {

    private MessagesRepository messagesRepository;

    public MessagesViewModel(@NonNull Application application) {
        super(application);
        messagesRepository = new MessagesRepository(application);
    }

    public LiveData<List<MessageModel>> getSpecificDialogMessages(String dialogAddress) {
        return messagesRepository.getSpecificDialogMessages(dialogAddress);
    }

    public LiveData<MessageModel> getLastMessage(String pStrDialogId) {
        return messagesRepository.getLastMessage(pStrDialogId);
    }

    public void getMessagesFromDevice(){
        messagesRepository.getMessagesFromDevice();
    }

    public void getMessagesFromServer(ApiCallback apiCallback) {
        messagesRepository.getMessagesFromServer(apiCallback);
    }



    public void insert(MessageModel messageModel) {
        messagesRepository.insert(messageModel);
    }

    public void update(MessageModel messageModel) {
        messagesRepository.update(messageModel);
    }

    public void delete(MessageModel messageModel) {
        messagesRepository.delete(messageModel);
    }
}
