package com.lixin.customview.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import com.lixin.customview.R;

public class LoadingDialog extends AlertDialog {

    private TextView mTextView;
    private CharSequence mMessage;
    private CircularRingView circularRingView;

    protected LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(Context context,CharSequence message){
        super(context);
        mMessage = message;
        setCancelable(false);
    }

    public LoadingDialog(Context context,boolean cancelable,CharSequence message,OnCancelListener listener){
        super(context,cancelable,listener);
        mMessage = message;
    }

    public static LoadingDialog show(Context context, CharSequence message) {
        return show(context, message, false,null);
    }

    public static LoadingDialog show(Context context,CharSequence mMessage,boolean cancelable,OnCancelListener listener){
        LoadingDialog loadingDialog = new LoadingDialog(context,cancelable,mMessage,null);
        loadingDialog.show();
        return loadingDialog;
    }


    @Override
    public void show() {
        super.show();
        if (circularRingView != null){
            circularRingView.startAnim();
        }
    }

    @Override
    public void dismiss() {
        if (circularRingView != null){
            circularRingView.stopAnim();
        }
        super.dismiss();
    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mTextView != null){
            mTextView.setText(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.loading_dialog);
        mTextView = ((TextView) findViewById(R.id.cc_loading_message));
        circularRingView = findViewById(R.id.circular_view);
        mTextView.setSelected(true);
        if (mMessage != null) {
            mTextView.setText(mMessage);
        }
    }
}
