package com.xj.guanquan.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2015/9/10.
 */
public class TouchImageView extends SimpleDraweeView {


    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchImageView(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable=getDrawable();
                if(drawable!=null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp=getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
