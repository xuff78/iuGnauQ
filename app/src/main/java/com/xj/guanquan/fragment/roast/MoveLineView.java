package com.xj.guanquan.fragment.roast;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.xj.guanquan.R;

import common.eric.com.ebaselibrary.util.ScreenUtils;

public class MoveLineView extends LinearLayout{
	
	public Scroller mScroller;
	protected final Interpolator SMOOTH_INTERPOLATOR = new SmoothInterpolator();
	private View line;
	private int width, height, itemNum=0, x=0, oldPos=0;
	private int screenWitdh=0;
	
	public MoveLineView(Activity context, RelativeLayout frame, int itemNum) {
		super(context);
		this.itemNum=itemNum;
		height= (int) ScreenUtils.dpToPxInt(context, 35);
		width= (int) ScreenUtils.dpToPxInt(context, 100);

		WindowManager wm = context.getWindowManager();
		this.screenWitdh=wm.getDefaultDisplay().getWidth();;
		mScroller = new Scroller(context, SMOOTH_INTERPOLATOR);
		
		RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(screenWitdh, -2);
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
		frame.addView(this, rlp);
//		setBackgroundColor(Color.GRAY);
		
		line=new View(context);
		LayoutParams vlp=new LayoutParams(width, height);
		line.setBackgroundResource(R.drawable.menu_yellow_corner);
		addView(line, vlp);
//		x=screenWitdh/4-width/2;
//		scrollTo(x, 0);
	}
	
	public void setPos(int pos){
		int centerInPos=(screenWitdh/(itemNum*2))*(pos*2-1);
		final int toleft=centerInPos-width/2;
		mScroller.startScroll(x, 0, toleft-x, 0, 500);
		invalidate();
	};
	
	@Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset())//如果mScroller没有调用startScroll，这里将会返回false。
        {
            scrollTo(-mScroller.getCurrX(), 0);
            x=mScroller.getCurrX();
            line.invalidate();
        }
    }

	class SmoothInterpolator implements Interpolator {

		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	}

}
