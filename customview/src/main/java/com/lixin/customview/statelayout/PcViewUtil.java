package com.lixin.customview.statelayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.lixin.customview.R;

public class PcViewUtil {
    public static Drawable getLoadingDrawable(Context context) {
//        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.cf_loading_anim));
//        DrawableCompat.setTint(drawable, getColorPrimary(context));
//        return drawable;
        return ContextCompat.getDrawable(context, R.drawable.state_loading_anim);
    }
}
