package com.tykle.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.tykle.R;


/**
 * Created by Dimpal on 04/05/18.
 */

public class ShowSnackBar {

    public static Snackbar snackbar;

    public static void shortSnackBar(View view, Activity activity, String message) {

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(activity.getResources().getColor(R.color.colorGrey));

        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));

        snackbar.show();

    }

    public static void longSnackBar(View view, Activity activity, String message) {

        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(activity.getResources().getColor(R.color.errorColor));

        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));

        snackbar.show();

    }


}
