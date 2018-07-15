package com.portfolio.jgsilveira.customersportfolio.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.portfolio.jgsilveira.customersportfolio.R;

public final class DialogUtil {

    private DialogUtil() {}

    public static void showWarningDialog(Context context, @StringRes int resId) {
        String message = context.getString(resId);
        showWarningDialog(context, message);
    }

    public static void showWarningDialog(Context context, String message) {
        int titleId = R.string.alerta;
        AlertDialog dialog = createDialog(context, message, titleId);
        dialog.show();
    }

    public static void showErrorDialog(Context context, @StringRes int resId) {
        String message = context.getString(resId);
        showErrorDialog(context, message);
    }

    public static void showErrorDialog(Context context, String message) {
        int titleId = R.string.erro;
        AlertDialog dialog = createDialog(context, message, titleId);
        dialog.show();
    }

    private static AlertDialog createDialog(Context context, String message, int titleId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

}
