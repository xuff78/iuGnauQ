package com.xj.guanquan.activity.roast;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.ScreenUtils;
import common.eric.com.ebaselibrary.util.StringUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QPublishAct extends QBaseActivity{

    private EditText editText, complainPhoneEdt, complainEmailEdt, titleEdt, AddrEdt;
    private LinearLayout photoLayout, dateLayout, complainLayout;
    private RelativeLayout copyLayout, roleSelectLayout, shareLayout, timePickerLayout;

    private int PageType=0;
    public static final int TypeTucao=0;
    public static final int TypeDate=1;
    public static final int TypeSecret=2;

    private int RequestType=0;
    public static final int RequestPublish=0;
    public static final int RequestComplain=1;
    public static final int RequestAddComment=2;
    public static final int RequestJoin=3;
    private TextView timeTxt;
    private int imgItemWidth=0;
    private View addIconView;
    private LayoutInflater inflater;
//    private NoteInfo note;
    private StringRequest requestPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_date);

//        note= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
        PageType=getIntent().getIntExtra("PageType", 0);
        RequestType=getIntent().getIntExtra("RequestType", 0);

        inflater= LayoutInflater.from(this);
        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        imgItemWidth=(scrennWidth- (int)ScreenUtils.dpToPxInt(this, 20)-6)/4;

        initData();
    }

    private void initData() {
        if (PageType == TypeTucao&&RequestType==RequestPublish) {
            _setHeaderTitle("开始发布");
            editText.setHint("想吐槽点什么");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            copyLayout.setVisibility(View.VISIBLE);

        } else if (PageType == TypeDate&&RequestType==RequestPublish) {
            _setHeaderTitle("开始约会");
            editText.setHint("写点什么描述约会内容");
            photoLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.VISIBLE);
            initTimePicker();

            timePickerLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dpd.show();
                }
            });

        } else if (PageType == TypeSecret&&RequestType==RequestPublish) {
            _setHeaderTitle("秘密发布");
            editText.setHint("想说点什么秘密");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            roleSelectLayout.setVisibility(View.VISIBLE);

        }else if (RequestType == RequestJoin) {
            _setHeaderTitle("开始报名");
            editText.setHint("写点什么描述报名内容");
            shareLayout.setVisibility(View.VISIBLE);

        }  else if (RequestType == RequestComplain){
            _setHeaderTitle("投诉");
            editText.setHint("请输入要投诉的内容");
            photoLayout.setVisibility(View.VISIBLE);
            complainLayout.setVisibility(View.VISIBLE);
        } else if (RequestType == RequestAddComment){

        }
        if(RequestType!=RequestJoin){
            setAddView();
            seImageView("");
        }
        _setRightHomeGone();
        _setRightHomeText("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHandler();
            }
        });
    }

    @Override
    protected void initView() {
        timeTxt=(TextView)findViewById(R.id.timeTxt);
        AddrEdt=(EditText)findViewById(R.id.AddrEdt);
        titleEdt=(EditText)findViewById(R.id.titleEdt);
        editText=(EditText)findViewById(R.id.publishTxt);
        complainPhoneEdt=(EditText)findViewById(R.id.complainPhoneEdt);
        complainEmailEdt=(EditText)findViewById(R.id.complainEmailEdt);
        photoLayout=(LinearLayout)findViewById(R.id.photoLayout);
        dateLayout=(LinearLayout)findViewById(R.id.dateLayout);
        shareLayout=(RelativeLayout)findViewById(R.id.shareLayout);
        copyLayout=(RelativeLayout)findViewById(R.id.copyLayout);
        roleSelectLayout=(RelativeLayout)findViewById(R.id.roleSelectLayout);
        complainLayout=(LinearLayout)findViewById(R.id.complainLayout);
        timePickerLayout=(RelativeLayout)findViewById(R.id.timePickerLayout);
    }

    @Override
    protected void initHandler() {

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
//        if (StringUtils.isEquals(requestMethod, ApiList.TUCAO_Publish)) {
            ToastUtils.show(this, "提交成功");
            finish();
//        }

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

    private DatePickerDialog dpd=null;
    void initTimePicker(){
        DatePickerDialog.OnDateSetListener otsl=new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                timeTxt.setText(+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                dpd.dismiss();
            }
        };
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        dpd=new DatePickerDialog(this,otsl,year,month,day);
    }

    private void createHandler(){
        if (editText.getText().length() > 0) {
            if(RequestType == RequestPublish){
                if (PageType == TypeTucao) {
                    Map<String, String> params=new HashMap<>();
                    startRequest(ApiList.DATE_Publish, params);
                } else if (PageType == TypeDate) {
                    if (timeTxt.getText().length() == 0) {
                        ToastUtils.show(getApplicationContext(), "请选择日期");
                    } else if (titleEdt.getText().length() == 0) {
                        ToastUtils.show(getApplicationContext(), "请输入主题");
                    } else if (AddrEdt.getText().length() == 0) {
                        ToastUtils.show(getApplicationContext(), "请输入地址");
                    } else {
                        Map<String, String> params=new HashMap<>();
                        params.put("beginTime", timeTxt.getText().toString());
                        params.put("address", AddrEdt.getText().toString());
                        startRequest(ApiList.DATE_Publish, params);
                    }
                } else if (PageType == TypeSecret) {
                    Map<String, String> params=new HashMap<>();
                    params.put("avatar", "http://img0.tuicool.com/IRv6bi.jpg");
                    startRequest(ApiList.SECRET_Publish, params);
                }
            } else if (RequestType == RequestJoin) {
//                Map<String, String> params=new HashMap<>();
//                params.put("id", AddrEdt.getText().toString());
//                startRequest(ApiList.DA, params);
            } else if (RequestType == RequestComplain) {
                Map<String, String> params=new HashMap<>();
                String userphone=complainPhoneEdt.getText().toString();
                String useremail=complainEmailEdt.getText().toString();
                if(userphone.length()==0&&useremail.length()==0) {
                    NoteInfo noteinfo= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
                    params.put("phone", userphone);
                    params.put("email", useremail);
                    params.put("id", noteinfo.getId()+"");
                    if (PageType == TypeTucao) {
                        startRequest(ApiList.TUCAO_AddComplain, params);
                    } else if (PageType == TypeDate) {
                        startRequest(ApiList.DATE_AddComplain, params);
                    }else if (PageType == TypeSecret) {
                        startRequest(ApiList.SECRET_AddComplain, params);
                    }
                }
            } else if (RequestType == RequestAddComment) {
                NoteInfo noteinfo= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
                Map<String, String> params=new HashMap<>();
                params.put("receiveUserId", noteinfo.getUserId()+"");
                params.put("id", noteinfo.getId()+"");
                if (PageType == TypeTucao) {
                    startRequest(ApiList.TUCAO_AddComment, params);
                } else if (PageType == TypeDate) {
                    startRequest(ApiList.DATE_AddComment, params);
                }else if (PageType == TypeSecret) {
                    startRequest(ApiList.SECRET_AddComment, params);
                }
            }

        }else
            ToastUtils.show(getApplicationContext(), "请输入内容");
    }

    private void startRequest(String method, final Map<String, String> mapparams){
        requestPublish = new StringRequest(Request.Method.POST, method, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QPublishAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = mapparams;
                map.put("content", String.valueOf(editText.getText().toString()));
                map.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                return map;
            }
        };
        addToRequestQueue(requestPublish, method, true);
    }

}
