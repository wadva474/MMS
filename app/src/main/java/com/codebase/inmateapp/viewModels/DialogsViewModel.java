package com.codebase.inmateapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.repositories.DialogsRepository;

import java.util.List;

public class DialogsViewModel extends AndroidViewModel {

    private DialogsRepository dialogsRepository;

    public DialogsViewModel(@NonNull Application application) {
        super(application);
        dialogsRepository = new DialogsRepository(application);
    }

    public LiveData<List<DialogModel>> getAllDialogs() {
        return dialogsRepository.getAllDialogs();
    }

    public DialogModel getDialogByAddress(String address) {
        return dialogsRepository.getDialogByAddress(address);
    }

    public void insert(DialogModel dialogModel) {
        dialogsRepository.insert(dialogModel);
    }

    public void update(DialogModel dialogModel) {
        dialogsRepository.update(dialogModel);
    }

    public void delete(DialogModel dialogModel) {
        dialogsRepository.delete(dialogModel);
    }
}
