package com.codebase.inmateapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.interfaces.CustomAlertDialogClickListener;

public class Helpers {

    public static final int SET_DEFAULT_SMS_REQUEST = 1;

    public static void showFloatingInputDialog(Context context, String title, String currentValue,
                                               CustomAlertDialogClickListener onClickListener) {
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        int left = dpToPx(32);
//        int top = dpToPx(8);
//        int right = dpToPx(32);
//        int bottom = dpToPx(8);
//
//        lp.setMargins(left, top, right, bottom);

        final EditText editText = new EditText(context);
        editText.setText(currentValue);
        editText.setSingleLine();
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editText.setLayoutParams(lp);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton(context.getString(R.string.btn_set), (dialogInterface, i) -> onClickListener.onClick(editText.getText().toString()))
                .setNegativeButton(context.getString(R.string.btn_cancel), null)
                .create();
        dialog.show();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


//    public static void makeDefaultSmsApp(Activity activity) {
//
//        if (!isDefaultSmsApp(activity)) {
//            final Intent changeDefaultIntent = new Intent(
//                    Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//            changeDefaultIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
//                    activity.getPackageName());
//            activity.startActivityForResult(changeDefaultIntent, SET_DEFAULT_SMS_REQUEST);
//        }
//    }
//
//
//    public static boolean isDefaultSmsApp(Context context) {
//        return context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context));
//    }

}
