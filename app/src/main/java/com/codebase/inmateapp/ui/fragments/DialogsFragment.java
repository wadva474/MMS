package com.codebase.inmateapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebase.inmateapp.MainActivity;
import com.codebase.inmateapp.R;
import com.codebase.inmateapp.adapters.DialogsAdapter;
import com.codebase.inmateapp.databases.DBTables;
import com.codebase.inmateapp.models.DialogModel;
import com.codebase.inmateapp.viewModels.DialogsViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;
import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class DialogsFragment extends Fragment {

    private final String TAG = "DialogFrag";

    private RecyclerView rvDialogsList;
    private DialogsAdapter dialogsAdapter;

    private DialogsViewModel dialogsViewModel;
//    private MessagesViewModel messagesViewModel;

    private List<DialogModel> dialogModelList = new ArrayList<>();

//    ProcessSms processSms;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialogs, container, false);

        ((MainActivity) requireActivity()).HideShowBottomNavigationBar(this);

        rvDialogsList = view.findViewById(R.id.rv_dialogs_list);
//        processSms = new ProcessSms(requireContext());

        dialogsViewModel = ViewModelProviders.of(this).get(DialogsViewModel.class);
//        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);

        dialogsAdapter = new DialogsAdapter(requireContext(), dialogModelList);
        dialogsAdapter.setonItemClickListener(dialogModel -> {
            Bundle args = new Bundle();
            args.putString(DBTables.DIALOG_DIALOG_ADDRESS, dialogModel.getAddress());
            openChatFragment(args);
        });

        DividerItemDecoration itemDecor = new DividerItemDecoration(requireContext(), VERTICAL);
        rvDialogsList.addItemDecoration(itemDecor);

        rvDialogsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDialogsList.setHasFixedSize(true);
        rvDialogsList.setAdapter(dialogsAdapter);

        dialogsViewModel.getAllDialogs().observe(getViewLifecycleOwner(), dialogModels -> {
            Log.d(TAG, "DIALOG OBSERVER: " + dialogModels.size());
            dialogsAdapter.setValues(dialogModels);
        });

        return view;
    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
////        getDialogs();
//
//
//    }

//    private void getDialogs() {
////        messagesViewModel.getMessagesFromServer(new ApiCallback() {
////            @Override
////            public void onSuccess(Object result) {
////                Log.e(TAG, "GetMesssages From server SUCCESS");
////
////            }
////
////            @Override
////            public void onError(String error) {
////
////                Log.e(TAG, "GetMesssages From server ERROR: " + error);
////            }
////        });
//
//    }

//    private List<DialogModel> getAllDialogsAndMessages() {
//        List<MessageModel> messageModelCellularList = processSms.importMessages();
//
//        List<DialogModel> dialogModelList = new ArrayList<>();
//
//        for(int i = 0; i < messageModelCellularList.size(); i++) {
//            DialogModel dialogModel = null;
//                for (int j = 0; j < dialogModelList.size(); j++) {
//                    if (dialogModelList.get(j).getAddress().equalsIgnoreCase(messageModelCellularList.get(i).getAddress())) {
//
//                        Log.d(TAG, "DialogAlreadyExists");
//                        dialogModel = dialogModelList.get(j);
//                        break;
//                    }
//            }
//                if (dialogModel == null) {
//                    dialogModel = new DialogModel();
//                }
//
//            dialogModel.setAddress(messageModelCellularList.get(i).getAddress());
//            dialogModel.setLastMessage(messageModelCellularList.get(i).getBody());
//
//            if (!dialogModelList.contains(dialogModel)) {
//                dialogModelList.add(dialogModel);
//            }
//            dialogsViewModel.insert(dialogModel);
//
//            // inserting into MessageModelList
//            MessageModel messageModel = new MessageModel();
//            messageModel.setId(messageModelCellularList.get(i).getId());
//            messageModel.setAddress(messageModelCellularList.get(i).getAddress());
//            messageModel.setBody(messageModelCellularList.get(i).getBody());
//            messageModel.setDate(messageModelCellularList.get(i).getDate());
//            Log.d(TAG, "MsgId: " + messageModel.getId());
//            Log.d(TAG, "MsgAddress: " + messageModel.getAddress());
//            Log.d(TAG, "TIME: " + messageModel.getDate());
//
//            messagesViewModel.insert(messageModel);
//        }
//
//        messagesViewModel.getMessagesFromServer(new ApiCallback() {
//            @Override
//            public void onSuccess(Object result) {
//                ResponseModel apiResponseModel = (ResponseModel) result;
//                Log.d(TAG, "MessagesToSendFromServer - Secret: " + apiResponseModel.getPayload().getSecret());
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.e(TAG, "MessagesToSendFromServer: " + error);
//            }
//        });
//
//
////        getMessagesToSendServer.enqueue(new Callback<ResponseBody>() {
////            @Override
////            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                if (response.isSuccessful()) {
////                    Log.d(TAG, "RESPONSE FROM SERVER: " + response.body());
////                } else {
////                    Log.e(TAG, response.message());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<ResponseBody> call, Throwable t) {
////                Log.e(TAG, t.getLocalizedMessage());
////            }
////        });
//
//        return dialogModelList;
//    }

//    public List<MessageModel> getAllSms() {
////
////
////        List<String> lstSms = new ArrayList<>();
////        ContentResolver cr = requireContext().getContentResolver();
////
////        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official
////                // CONTENT_URI
////                // from docs
////                new String[] {Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.ADDRESS}, // Select body text
////                null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default
////        // sort
////        // order);
////        int totalSMS = c.getCount();
////
////        if (c.moveToFirst()) {
////            try {
////                for (int i = 0; i < totalSMS; i++) {
////                    lstSms.add(c.getString(0));
////                    c.moveToNext();
////                }
////            } catch (RuntimeException e) {
////                Log.e(TAG, "No Messages");
////            }
////
////        }
////        c.close();
//
////        return lstSms;
//
//        List<MessageModel> messageModelList = new ArrayList<>();
//
//        final String SMS_URI_INBOX = "content://sms/inbox";
//        final String SMS_URI_ALL = "content://sms/";
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type", "status"};
//            Cursor cur = requireContext().getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, projection, null, null, "date desc");
//            if (cur.moveToFirst()) {
//                int index_id = cur.getColumnIndex("_id");
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//                int index_status = cur.getColumnIndex("status");
//                do {
//                    String id = cur.getString(index_id);
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int type = cur.getInt(index_Type);
//                    int status = cur.getInt(index_status);
//
//                    MessageModel messageModel = new MessageModel();
//                    messageModel.setId(id);
//                    messageModel.setAddress(strAddress);
//                    messageModel.setBody(strbody);
//                    messageModel.setCreatedAt(longDate);
//                    messageModel.setType(type);
//                    messageModel.setStatus(status);
//
//                    messageModelList.add(messageModel);
//                } while (cur.moveToNext());
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//
//                Log.d(TAG, "NO RESULT");
////                smsBuilder.append("no result!");
//            }
//    } catch (SQLiteException ex) {
//        Log.d("SQLiteException", ex.getMessage());
//    }
//        return messageModelList;
//}

    private void openChatFragment(Bundle args) {
        findNavController(DialogsFragment.this).navigate(R.id.action_navigation_messages_to_chatFragment, args);
    }
}