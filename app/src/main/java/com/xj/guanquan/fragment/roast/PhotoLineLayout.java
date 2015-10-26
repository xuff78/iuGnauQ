package com.xj.guanquan.fragment.roast;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.xj.guanquan.views.TouchGrayImageView;

/**
 * Created by Administrator on 2015/10/14.
 */
public class PhotoLineLayout extends LinearLayout {

    private String[] urls;
    private int imgWith=0;
    private Activity act;

    public PhotoLineLayout(Activity context, int width, String[] urls) {
        super(context);
        act=context;
        this.urls=urls;
        setOrientation(LinearLayout.VERTICAL);
        imgWith=width/5;
        if(urls.length>0)
            setImageView();
        else
            setVisibility(View.GONE);
    }

    public void setImgCallback(ClickListener callback){
        this.callback=callback;
    }

    private void setImageView(){
        LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(imgWith, imgWith);
        llp.leftMargin = 2;
//        LinearLayout itemLayout=new LinearLayout(act);
        setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<urls.length;i++) {
            final int j=i;
            TouchGrayImageView img = new TouchGrayImageView(act);
            img.setLayoutParams(llp);
            img.setImageURI(Uri.parse(urls[i]));
            addView(img);
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(callback!=null)
                        callback.onClick(view, j);
                }
            });
        }
    }

    private  ClickListener callback=null;
    public interface ClickListener{
        public void onClick(View v, int position);
    }
}
