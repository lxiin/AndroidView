package com.lixin.customview.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import com.lixin.customview.R;

/**
 * 这个是旧的
 * xml中有二个LOading的小菊花用的QMUI中的  现在还没有引用 QMUI
 */
public class DefaultLoadingDialog extends AlertDialog {

    private TextView mTextView;
    private CharSequence mMessage;


    public static DefaultLoadingDialog show(Context context) {
        return show(context, false);
    }

    public static DefaultLoadingDialog show(Context context, CharSequence message) {
        return show(context, message, false);
    }

    @Deprecated
    public static DefaultLoadingDialog show(Context context, boolean isNightTheme) {
        return show(context, null, isNightTheme);
    }

    @Deprecated
    public static DefaultLoadingDialog show(Context context, CharSequence message, boolean isNightTheme) {
        return show(context, message, false, isNightTheme);
    }

    @Deprecated
    public static DefaultLoadingDialog show(Context context, CharSequence message, boolean cancelable, boolean isNightTheme) {
        return show(context, message, cancelable, isNightTheme, null);
    }

    public static DefaultLoadingDialog show(Context context, CharSequence message, boolean cancelAble, boolean isNightTheme, DialogInterface.OnCancelListener cancelListener) {
        DefaultLoadingDialog DefalutLoadingDialog = new DefaultLoadingDialog(context, cancelAble, message, isNightTheme, cancelListener);
        DefalutLoadingDialog.show();
        return DefalutLoadingDialog;
    }

    @Deprecated
    public DefaultLoadingDialog(Context context) {
        super(context);
    }

    @Deprecated
    public DefaultLoadingDialog(Context context, CharSequence message) {
        this(context, message, false);
    }

    @Deprecated

    public DefaultLoadingDialog(Context context, CharSequence message, boolean isNightTheme) {
        super(context);
        mMessage = message;
        setCancelable(false);
    }

    @Deprecated

    public DefaultLoadingDialog(Context context, boolean cancelable, CharSequence message, boolean isNightTheme, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mMessage = message;
    }

    public DefaultLoadingDialog(Context context, int themeResId, CharSequence message) {
        super(context, themeResId);
        mMessage = message;
    }

    
    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mTextView != null) {
            mTextView.setText(mMessage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.default_loading_dialog);
        mTextView = ((TextView) findViewById(R.id.cc_loading_message));
        mTextView.setSelected(true);
        if (mMessage != null) {
            mTextView.setText(mMessage);
        }
    }
}
