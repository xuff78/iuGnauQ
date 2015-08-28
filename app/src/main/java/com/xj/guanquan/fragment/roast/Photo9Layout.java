package com.xj.guanquan.fragment.roast;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xj.guanquan.R;

import java.util.ArrayList;

/**
 * Created by 可爱的蘑菇 on 2015/8/28.
 */
public class Photo9Layout extends LinearLayout {

    private ArrayList<Integer> urls=new ArrayList<Integer>();
    private int imgWith=0;
    private Activity act;
    public Photo9Layout(Activity context, int width, ArrayList<Integer> urls) {
        super(context);
        act=context;
        this.urls=urls;
        setOrientation(LinearLayout.VERTICAL);
        if(urls.size()==1)
            imgWith=width/3*2;
        else if(urls.size()==2)
            imgWith=width/2-4;
        else
            imgWith=width/3-6;
        if(urls.size()>0)
            setImageView();
        else
            setVisibility(View.GONE);
    }

    private void setImageView(){
        LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(imgWith, imgWith);
        llp.leftMargin = 2;
        LinearLayout itemLayout=null;
        for(int i=0;i<urls.size();i++) {
            if(i%3==0){
                itemLayout=new LinearLayout(act);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                addView(itemLayout);
            }
            ImageView img = new ImageView(act);
            img.setLayoutParams(llp);
            img.setImageResource(urls.get(i));
            itemLayout.addView(img);
        }
    }
}
