package com.xj.guanquan.activity.roast;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.UserInfo;

import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.ScreenUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QPublishAct extends QBaseActivity{

    private EditText editText;
    private LinearLayout photoLayout, dateLayout;
    private RelativeLayout copyLayout, roleSelectLayout, shareLayout;
    public static final int TypeTucao=0;
    public static final int TypeDate=1;
    public static final int TypeSecret=2;
    public static final int TypeJoin=3;
    private int PageType=0;
    private int imgItemWidth=0;
    private View addIconView;
    private LayoutInflater inflater;

    @Override
    protected void initHandler() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_date);

        PageType=getIntent().getIntExtra("PageType", 0);


        inflater= LayoutInflater.from(this);
        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        imgItemWidth=(scrennWidth- (int)ScreenUtils.dpToPxInt(this, 20)-6)/4;
        initData();
    }

    private void initData() {
        if (PageType == TypeTucao) {
            _setHeaderTitle("开始发布");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            copyLayout.setVisibility(View.VISIBLE);

        } else if (PageType == TypeDate) {
            _setHeaderTitle("开始约会");
            photoLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.VISIBLE);

        } else if (PageType == TypeJoin) {
            _setHeaderTitle("开始报名");
            shareLayout.setVisibility(View.VISIBLE);

        } else if (PageType == TypeSecret) {
            _setHeaderTitle("秘密发布");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            roleSelectLayout.setVisibility(View.VISIBLE);

        }
        _setRightHomeGone();
        _setRightHomeText("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastShort("发布...");
            }
        });
        if(PageType!=TypeJoin){
            setAddView();
            seImageView("");
        }
    }

    @Override
    protected void initView() {
        editText=(EditText)findViewById(R.id.publishTxt);
        photoLayout=(LinearLayout)findViewById(R.id.photoLayout);
        dateLayout=(LinearLayout)findViewById(R.id.dateLayout);
        shareLayout=(RelativeLayout)findViewById(R.id.shareLayout);
        copyLayout=(RelativeLayout)findViewById(R.id.copyLayout);
        roleSelectLayout=(RelativeLayout)findViewById(R.id.roleSelectLayout);
    }

    private void setAddView(){
        LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(imgItemWidth, imgItemWidth);
        llp.rightMargin=2;
        addIconView=inflater.inflate(R.layout.bill_image_item, null);
        ImageView img=(ImageView)addIconView.findViewById(R.id.img);
//        img.setBackgroundResource(R.color.trans_white);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setImageResource(R.mipmap.tianjia);
        photoLayout.addView(addIconView, llp);

        addIconView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                new PhotoDialog(QPublishAct.this).show();
            }
        });
    }

    private void seImageView(final String imgUrl){
//        urls.add(imgUrl);
        LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(imgItemWidth, imgItemWidth);
        llp.rightMargin=2;
        photoLayout.removeView(addIconView);
        final View v=inflater.inflate(R.layout.bill_image_item, null);
        ImageView img=(ImageView)v.findViewById(R.id.img);
        img.setImageResource(R.mipmap.zhaopian);
//        imageloader.displayImage(imgUrl, img);
        photoLayout.addView(v, llp);
        View del=v.findViewById(R.id.deleteIcon);
        del.setVisibility(View.VISIBLE);
        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                alertConfirmDialog(("确认删除?"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        urls.remove(imgUrl);
                        removeImage(v, imgItemWidth, 0);
                    }
                }, null);
            }
        });
        setAddView();
    }

    private void removeImage(final View item, int start, int end){
        item.setVisibility(View.INVISIBLE);
        ValueAnimator anima=ValueAnimator.ofInt(start, end);
        anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                // TODO Auto-generated method stub
                LinearLayout.LayoutParams llpitem = (LinearLayout.LayoutParams) item.getLayoutParams();
                llpitem.width = (Integer) arg0.getAnimatedValue();
                item.setLayoutParams(llpitem);
            }
        });
        anima.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub
                photoLayout.removeView(item);
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });

        anima.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.anim.decelerate_interpolator));
        anima.setDuration(300);
        anima.start();
    }

}
