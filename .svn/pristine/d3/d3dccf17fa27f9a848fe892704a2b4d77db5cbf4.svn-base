package com.hsdi.NetMe.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.hsdi.NetMe.R;

public class DialogUtils {

    /**
     * Creates a dialog with a progress loading bar that can not be removed by the user
     * @param context    context
     * @return a progress dialog
     * */
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    /**
     * Creates an dialog to show the default error message.
     * @param context    context
     * @return an error dialog with the default error message
     * */
    public static AlertDialog getErrorDialog(Context context) {
        return getErrorDialog(context, R.string.error_unexpected);
    }

    /**
     * Creates an dialog to show an error message passed in.
     * @param context              context
     * @param messageResourceId    id of a string message to place in the dialog
     * @return an error dialog with the message associated with the passed in message id
     * */
    public static AlertDialog getErrorDialog(Context context, @StringRes int messageResourceId) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(messageResourceId)
                .setNeutralButton(R.string.ok, null)
                .create();
    }

    /**
     * Creates an dialog to show an error message passed in.
     * @param context    context
     * @param message    the string message to place in the dialog
     * @return an error dialog with the passed in string message
     * */
    public static AlertDialog getErrorDialog(Context context, @NonNull String message) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(message)
                .setNeutralButton(R.string.ok, null)
                .create();
    }

    /**
     * Creates a dialog to show an error message passed in and closes the passed activity once ok is clicked
     * @param activity             the activity to close
     * @param messageResourceId    id of a string message to place in the dialog
     * @return an error dialog with the message associated with the passed in message id
     * */
    public static AlertDialog getFinishingErrorDialog(final Activity activity, @StringRes int messageResourceId) {
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.error)
                .setMessage(messageResourceId)
                .setNegativeButton(
                        R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //activity.finish();
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }

    /**
     * Creates a dialog to show an error message passed in and closes the passed activity once ok is clicked
     * @param activity   the activity to close
     * @param message    the string message to place in the dialog
     * @return an error dialog with the passed in string message
     * */
    public static AlertDialog getFinishingErrorDialog(final Activity activity, @NonNull String message) {
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.error)
                .setMessage(message)
                .setNegativeButton(
                        R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }

    /**
     * Creates a dialog to show the user a rational for needing certain permissions and then requests them
     * @param activity       the activity that is requesting the permissions
     * @param rationalId     the string resource id for the rational
     * @param permissions    the permissions which will be requested
     * @param requestId      the request id the use when requesting the permissions
     * @return the alert dialog request the permissions
     * */
    public static AlertDialog requestPermissionDialog(final Activity activity, @StringRes int rationalId, final String[] permissions, final int requestId) {
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_request_title)
                .setMessage(rationalId)
                .setPositiveButton(
                        R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(activity, permissions, requestId);
                            }
                        }
                )
                .create();
    }
}
