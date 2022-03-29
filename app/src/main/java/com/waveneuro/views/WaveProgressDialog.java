package com.waveneuro.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.waveneuro.R;

public class WaveProgressDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public WaveProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void startProgressDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public boolean isShowing() {
        if(alertDialog == null)
            return false;
        return alertDialog.isShowing();
    }
}
