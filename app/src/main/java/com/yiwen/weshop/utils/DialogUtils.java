package com.yiwen.weshop.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


public class DialogUtils {

    public static void showSimpleDialog(Activity context, String title, String msg, String posBtn,
                                        DialogInterface.OnClickListener posListener) {
        AlertDialog builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posBtn, posListener)
                .setNegativeButton("返回", null)
                .show();
    }
}
