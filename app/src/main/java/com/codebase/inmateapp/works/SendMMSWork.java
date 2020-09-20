package com.codebase.inmateapp.works;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codebase.inmateapp.repositories.MessagesRepository;

import java.io.File;
import java.io.FileInputStream;

public class SendMMSWork extends Worker {

    private final String TAG = SendMMSWork.class.getSimpleName();

    public static final String SEND_MMS_WORK = "send_mms_work";

    private static final String WORK_STATUS = "work_status";
    private static final String WORK_RESULT = "work_result";

    private static final String MEDIA_DIRECTORY_NAME = "InmateMedia/Media";

    private Context context;

    private MessagesRepository messagesRepository;

    public SendMMSWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        messagesRepository = new MessagesRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        try {

            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    MEDIA_DIRECTORY_NAME);

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "mms.jpg");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap myImg  = BitmapFactory.decodeStream(new FileInputStream(mediaFile), null, options);

//            Settings settings = new Settings();
//            settings.setUseSystemSending(true);
//            Transaction transaction = new Transaction(context, settings);
//            Message message = new Message("Hello test mms", "03457221181");
//            Message message = new Message("Hello test mms", "7025410189");
//            message.setImage(myImg);
//            transaction.sendNewMessage(message, Transaction.NO_THREAD_ID);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("TAGer", "Error in load image." + ex);
        }

        return Result.success(outputData);
    }
}
