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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xj.guanquan.R;
import com.xj.guanquan.adapter.TuCaoAdapter;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.PageInfo;
import com.xj.guanquan.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.ScreenUtils;
import common.eric.com.ebaselibrary.util.StringUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QPublishAct extends QBaseActivity{

    private EditText editText, complainEdt;
    private LinearLayout photoLayout, dateLayout;
    private RelativeLayout copyLayout, roleSelectLayout, shareLayout, complainLayout;
    public static final int TypeTucao=0;
    public static final int TypeDate=1;
    public static final int TypeSecret=2;
    public static final int TypeJoin=3;
    public static final int TypeComplain=4;
    public static final int TypeAddComment=4;
    private int PageType=0;
    private int imgItemWidth=0;
    private View addIconView;
    private LayoutInflater inflater;
//    private NoteInfo note;
    private StringRequest requestPublish, requestDatePublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_date);

//        note= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
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
            editText.setHint("写点什么描述约会内容");
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

        } else if (PageType == TypeComplain){
            _setHeaderTitle("投诉");
            editText.setHint("请输入要投诉的内容");
            photoLayout.setVisibility(View.VISIBLE);
            complainLayout.setVisibility(View.VISIBLE);
        }
        _setRightHomeGone();
        _setRightHomeText("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    if (PageType == TypeTucao) {
                        addToRequestQueue(requestPublish, ApiList.TUCAO_Publish, true);
                    } else if (PageType == TypeDate) {
                        addToRequestQueue(requestDatePublish, ApiList.DATE_Publish, true);
                    } else if (PageType == TypeJoin) {

                    } else if (PageType == TypeSecret) {

                    } else if (PageType == TypeComplain) {

                    } else if (PageType == TypeAddComment) {

                    }
                }else
                    ToastUtils.show(getApplicationContext(), "请输入内容");
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
        complainEdt=(EditText)findViewById(R.id.complainEdt);
        photoLayout=(LinearLayout)findViewById(R.id.photoLayout);
        dateLayout=(LinearLayout)findViewById(R.id.dateLayout);
        shareLayout=(RelativeLayout)findViewById(R.id.shareLayout);
        copyLayout=(RelativeLayout)findViewById(R.id.copyLayout);
        roleSelectLayout=(RelativeLayout)findViewById(R.id.roleSelectLayout);
        complainLayout=(RelativeLayout)findViewById(R.id.complainLayout);
    }

    View.OnClickListener listener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.publishTxt:

                    break;

            }
        }
    };

    @Override
    public void doResponse(Object response) {
        final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(requestMethod, ApiList.TUCAO_Publish)) {
            ToastUtils.show(this, "提交成功");
            finish();
        }

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

    @Override
    protected void initHandler() {
        requestPublish = new StringRequest(Request.Method.POST, ApiList.TUCAO_Publish, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QPublishAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("content", String.valueOf(editText.getText().toString()));
                map.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                return map;
            }
        };
        requestDatePublish = new StringRequest(Request.Method.POST, ApiList.DATE_Publish, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QPublishAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("content", String.valueOf(editText.getText().toString()));
                map.put("beginTime", "2015-9-3 12:50");
                map.put("address", "世界尽头");
                map.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                return map;
            }
        };
    }

}
