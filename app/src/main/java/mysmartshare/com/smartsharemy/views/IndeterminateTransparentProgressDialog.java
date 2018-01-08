
package mysmartshare.com.smartsharemy.views;

import android.app.Activity;
import android.app.Dialog;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import mysmartshare.com.smartsharemy.R;


public class IndeterminateTransparentProgressDialog extends Dialog {

    public static IndeterminateTransparentProgressDialog show(Activity context) {
        return show(context, false);
    }

    public static IndeterminateTransparentProgressDialog show(Activity context, boolean indeterminate) {
        return show(context, indeterminate, false, null);
    }

    public static IndeterminateTransparentProgressDialog show(Activity context, boolean indeterminate, boolean cancelable) {
        return show(context, indeterminate, cancelable, null);
    }

    public static IndeterminateTransparentProgressDialog showSmall(Activity context, boolean indeterminate, boolean cancelable) {

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 0, 0, 50);

        progressBar.setLayoutParams(buttonLayoutParams);

        IndeterminateTransparentProgressDialog dialog = new IndeterminateTransparentProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(null);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.setCanceledOnTouchOutside(true);
        params.y = -100;
        dialog.getWindow().setAttributes(params);

       // dialog.getWindow().getDecorView().setBottom(80);

        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        dialog.show();


      /*  IndeterminateTransparentProgressDialog dialog = new IndeterminateTransparentProgressDialog(context);
        dialog.setCancelable(cancelable);
        //dialog.setOnCancelListener(cancelListener);
        *//* The next line will add the ProgressBar to the dialog. *//*

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);



       *//* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 10);

        progressBar.setLayoutParams(params );
        LinearLayout test = new LinearLayout(getApplicationContext());
        test.addView(progressBar);*//*


        dialog.addContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/



        return dialog;
    }

    public static IndeterminateTransparentProgressDialog show(Activity context, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        IndeterminateTransparentProgressDialog dialog = new IndeterminateTransparentProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        dialog.show();

        return dialog;
    }

    private IndeterminateTransparentProgressDialog(Activity context) {
        super(context, R.style.ThemeProgressDialog);
    }


    private IndeterminateTransparentProgressDialog(Activity context, int theme) {
        super(context, theme);
    }
}
