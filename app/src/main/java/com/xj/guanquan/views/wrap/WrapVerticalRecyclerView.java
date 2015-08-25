package com.xj.guanquan.views.wrap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by eric on 2015/8/25.
 */
public class WrapVerticalRecyclerView extends RecyclerView {
    public WrapVerticalRecyclerView(Context context) {
        super(context);
    }

    public WrapVerticalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapVerticalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newWidthSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(newWidthSpec, heightSpec);
    }
}
