package com.xj.guanquan.views;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xj.guanquan.R;

/**
 * Created by 可爱的蘑菇 on 2015/8/28.
 */
public class Photo4Layout extends LinearLayout {

    private String[] urls;
    private int imgWith = 0;
    private Activity act;
    private OnClickListener addViewClick;
    private View addIconView;

    public Photo4Layout(Activity context, int width, String[] urls) {
        super(context);
        act = context;
        this.urls = urls;
        setOrientation(LinearLayout.VERTICAL);
        imgWith = width / 4 - 8;
        if (urls != null && urls.length > 0)
            setImageView();
        else
            setVisibility(View.GONE);
    }

    public void setImgCallback(ClickListener callback) {
        this.callback = callback;
    }

    public void setAddViewClick(OnClickListener addViewClick) {
        this.addViewClick = addViewClick;
    }

    private void setImageView() {
        LayoutParams llp = new LayoutParams(imgWith, imgWith);
        llp.leftMargin = 2;
        LinearLayout itemLayout = null;
        for (int i = 0; i < urls.length; i++) {
            final int j = i;
            if (i % 4 == 0) {
                itemLayout = new LinearLayout(act);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                addView(itemLayout);
            }
            TouchGrayImageView img = new TouchGrayImageView(act);
            img.setLayoutParams(llp);
            img.setImageURI(Uri.parse(urls[i]));
            itemLayout.addView(img);
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null)
                        callback.onClick(view, j);
                }
            });
        }
        if (urls.length % 4 == 0) {
            itemLayout = new LinearLayout(act);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(itemLayout);
        }
        addIconView = act.getLayoutInflater().inflate(R.layout.bill_image_item, null);
        ImageView img = (ImageView) addIconView.findViewById(R.id.img);
//        img.setBackgroundResource(R.color.trans_white);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setImageResource(R.mipmap.tianjia);
        addIconView.setLayoutParams(llp);
        itemLayout.addView(addIconView);
        addIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addViewClick != null)
                    addViewClick.onClick(view);
            }
        });
    }

    private ClickListener callback = null;


    public interface ClickListener {
        public void onClick(View v, int position);
    }

}
