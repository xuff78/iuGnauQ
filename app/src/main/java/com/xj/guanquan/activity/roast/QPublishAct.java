package com.xj.guanquan.activity.roast;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.contact.QMapSelectActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.MultipartRequest;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.fragment.roast.Photo9Layout;
import com.xj.guanquan.fragment.roast.TucaoMianFrg;
import com.xj.guanquan.model.NoteInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.ScreenUtils;
import common.eric.com.ebaselibrary.util.StringUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QPublishAct extends QBaseActivity {

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;
    private ProgressDialog progressDialog;
    private List<String> picPaths = new ArrayList<String>();
    private EditText editText, complainPhoneEdt, complainEmailEdt, titleEdt;
    private TextView AddrEdt;
    private LinearLayout photoLayout, dateLayout, complainLayout;
    private RelativeLayout copyLayout, roleSelectLayout, shareLayout, timePickerLayout;

    private int PageType = 0;
    public static final int TypeTucao = 0;
    public static final int TypeDate = 1;
    public static final int TypeSecret = 2;

    private int RequestType = 0;
    public static final int RequestPublish = 0;
    public static final int RequestComplain = 1;
    public static final int RequestAddComment = 2;
    public static final int RequestJoin = 3;
    public static final int RequestAddress=0x11;
    private TextView timeTxt;
    private int imgItemWidth = 0;
    private View addIconView;
    private LayoutInflater inflater;
    //    private NoteInfo note;
    private StringRequest requestPublish;
    private String Avatar = null;
    public String requestURL = "";
    private MultipartRequest uploadRequest;
    private int screenHeight=0, screenWidth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_date);

//        note= (NoteInfo) getIntent().getSerializableExtra("NoteInfo");

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth=display.getWidth()/7*6;
        screenHeight=display.getHeight()/7*6;
        progressDialog = new ProgressDialog(this);
        PageType = getIntent().getIntExtra("PageType", 0);
        RequestType = getIntent().getIntExtra("RequestType", 0);

        inflater = LayoutInflater.from(this);
        int scrennWidth = getWindowManager().getDefaultDisplay().getWidth();
        imgItemWidth = (scrennWidth - (int) ScreenUtils.dpToPxInt(this, 20) - 6) / 4;

        initData();
    }

    private void initData() {
        if (PageType == TypeTucao && RequestType == RequestPublish) {
            _setHeaderTitle("开始发布");
            editText.setHint("想吐槽点什么");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            copyLayout.setVisibility(View.VISIBLE);
            setAddView();
        } else if (PageType == TypeDate && RequestType == RequestPublish) {
            _setHeaderTitle("开始约会");
            editText.setHint("写点什么描述约会内容");
            photoLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.VISIBLE);
            setAddView();
            initTimePicker();
            timePickerLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dpd.show();
                }
            });

        } else if (PageType == TypeSecret && RequestType == RequestPublish) {
            _setHeaderTitle("秘密发布");
            editText.setHint("想说点什么秘密");
            photoLayout.setVisibility(View.VISIBLE);
            shareLayout.setVisibility(View.VISIBLE);
            roleSelectLayout.setVisibility(View.VISIBLE);
            setAddView();
            Map<String, String> params = new HashMap<>();
            startRequest(ApiList.SECRET_Avatar, params);
        } else if (RequestType == RequestJoin) {
            _setHeaderTitle("开始报名");
            editText.setHint("写点什么描述报名内容");
            shareLayout.setVisibility(View.VISIBLE);

        } else if (RequestType == RequestComplain) {
            _setHeaderTitle("投诉");
            editText.setHint("请输入要投诉的内容");
            photoLayout.setVisibility(View.VISIBLE);
            complainLayout.setVisibility(View.VISIBLE);
        } else if (RequestType == RequestAddComment) {
            _setHeaderTitle("回复");
            editText.setHint("想回复些什么");
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
        timeTxt = (TextView) findViewById(R.id.timeTxt);
        AddrEdt = (TextView) findViewById(R.id.AddrEdt);
        AddrEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(QPublishAct.this, QMapSelectActivity.class), RequestAddress);
            }
        });
        titleEdt = (EditText) findViewById(R.id.titleEdt);
        editText = (EditText) findViewById(R.id.publishTxt);
        complainPhoneEdt = (EditText) findViewById(R.id.complainPhoneEdt);
        complainEmailEdt = (EditText) findViewById(R.id.complainEmailEdt);
        photoLayout = (LinearLayout) findViewById(R.id.photoLayout);
        dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
        shareLayout = (RelativeLayout) findViewById(R.id.shareLayout);
        copyLayout = (RelativeLayout) findViewById(R.id.copyLayout);
        roleSelectLayout = (RelativeLayout) findViewById(R.id.roleSelectLayout);
        complainLayout = (LinearLayout) findViewById(R.id.complainLayout);
        timePickerLayout = (RelativeLayout) findViewById(R.id.timePickerLayout);
    }

    @Override
    protected void initHandler() {

    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.publishTxt:

                    break;

            }
        }
    };

    String[] avatarImgs = null;

    @Override
    public void doResponse(Object response) {
        final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        JSONObject data = new JSONObject(result.getData());
        if (StringUtils.isEquals(requestMethod, ApiList.SECRET_Avatar)) {
            JSONArray array = data.getJSONArray("content");
            avatarImgs = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                avatarImgs[i] = array.getString(i);
            }
            int width = (int) ScreenUtils.dpToPx(this, 200);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(width, -2);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            Photo9Layout roles = new Photo9Layout(this, width, avatarImgs);
            roles.setLayoutParams(rlp);
            roleSelectLayout.addView(roles);
            roles.setImgCallback(new Photo9Layout.ClickListener() {

                @Override
                public void onClick(View v, int position) {
                    Avatar = avatarImgs[position];
                }
            });

        } else if (StringUtils.isEquals(requestMethod, ApiList.TUCAO_Publish)) {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(TucaoMianFrg.publishSuccess);
                    QPublishAct.this.finish();
                }
            });
        } else if (StringUtils.isEquals(requestMethod, ApiList.DATE_Publish)) {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(TucaoMianFrg.publishSuccess);
                    QPublishAct.this.finish();
                }
            });
        } else if (StringUtils.isEquals(requestMethod, ApiList.SECRET_Publish)) {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(TucaoMianFrg.publishSuccess);
                    QPublishAct.this.finish();
                }
            });
        } else {
            ToastUtils.show(this, "提交成功");
            setResult(TucaoMianFrg.publishSuccess);
            finish();
        }

    }

    private void setAddView() {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(imgItemWidth, imgItemWidth);
        llp.rightMargin = 2;
        addIconView = inflater.inflate(R.layout.bill_image_item, null);
        ImageView img = (ImageView) addIconView.findViewById(R.id.img);
//        img.setBackgroundResource(R.color.trans_white);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setImageResource(R.mipmap.tianjia);
        photoLayout.addView(addIconView, llp);

        addIconView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                new PhotoDialog(QPublishAct.this).show();
                Intent intent = new Intent(QPublishAct.this, SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
            }
        });
    }

    private void seImageView(Bitmap bmp) {
//        urls.add(imgUrl);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(imgItemWidth, imgItemWidth);
        llp.rightMargin = 2;
        photoLayout.removeView(addIconView);
        final View v = inflater.inflate(R.layout.bill_image_item, null);
        ImageView img = (ImageView) v.findViewById(R.id.img);
//        img.setImageResource(R.mipmap.zhaopian);
//        imageloader.displayImage(imgUrl, img);
        img.setImageBitmap(bmp);
        photoLayout.addView(v, llp);
        View del = v.findViewById(R.id.deleteIcon);
        del.setVisibility(View.VISIBLE);
        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                alertConfirmDialog(("确认删除?"), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        urls.remove(imgUrl);
                        removeImage(v, imgItemWidth, 0);
                    }
                }, null);
            }
        });
        setAddView();
    }

    private void removeImage(final View item, int start, int end) {
        item.setVisibility(View.INVISIBLE);
        ValueAnimator anima = ValueAnimator.ofInt(start, end);
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

    private DatePickerDialog dpd = null;

    void initTimePicker() {
        DatePickerDialog.OnDateSetListener otsl = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeTxt.setText(+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                dpd.dismiss();
            }
        };
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(this, otsl, year, month, day);
    }

    private void createHandler() {
        if (editText.getText().length() > 0) {
            if (RequestType == RequestPublish) {
                if (PageType == TypeTucao) {
                    Map<String, String> params = new HashMap<>();
                    if (picPaths.size() > 0) {
                        requestURL = ApiList.TUCAO_Publish;
                        //handler.sendEmptyMessage(TO_UPLOAD_FILE);
                        params.put("content", String.valueOf(editText.getText().toString()));
                        params.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                        params.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                        List<File> files = new ArrayList<File>();
                        for (String picUrl : picPaths) {
                            File file = new File(picUrl);
                            files.add(file);
                        }
                        uploadRequest(requestURL, params, files, "pic");
                    } else {
                        startRequest(ApiList.TUCAO_Publish, params);
                    }
                } else if (PageType == TypeDate) {
                    if (timeTxt.getText().length() == 0) {
                        ToastUtils.show(getApplicationContext(), "请选择日期");
                    } else if (titleEdt.getText().length() == 0) {
                        ToastUtils.show(getApplicationContext(), "请输入主题");
                    } else if (AddrEdt.getText().length() == 0||AddrEdt.getText().toString().equals("请选择地点")) {
                        ToastUtils.show(getApplicationContext(), "请选择地点");
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("beginTime", timeTxt.getText().toString());
                        params.put("address", AddrEdt.getText().toString());
                        params.put("title", titleEdt.getText().toString());
                        if (picPaths.size() > 0) {
                            requestURL = ApiList.DATE_Publish;
                            //handler.sendEmptyMessage(TO_UPLOAD_FILE);
                            params.put("content", String.valueOf(editText.getText().toString()));
                            params.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                            params.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                            List<File> files = new ArrayList<File>();
                            for (String picUrl : picPaths) {
                                File file = new File(picUrl);
                                files.add(file);
                            }
                            uploadRequest(requestURL, params, files, "pic");
                        } else {
                            startRequest(ApiList.DATE_Publish, params);
                        }
                    }
                } else if (PageType == TypeSecret) {
                    if (Avatar != null && Avatar.length() > 0) {
                        Map<String, String> params = new HashMap<>();
                        if (picPaths.size() > 0) {
                            requestURL = ApiList.SECRET_Publish;
                            //handler.sendEmptyMessage(TO_UPLOAD_FILE);
                            params.put("content", String.valueOf(editText.getText().toString()));
                            params.put("lng", PreferencesUtils.getString(QPublishAct.this, "lng"));
                            params.put("lat", PreferencesUtils.getString(QPublishAct.this, "lat"));
                            params.put("avatar", Avatar);
                            List<File> files = new ArrayList<File>();
                            for (String picUrl : picPaths) {
                                File file = new File(picUrl);
                                files.add(file);
                            }
                            uploadRequest(requestURL, params, files, "file_pic");
                        } else {
                            startRequest(ApiList.SECRET_Publish, params);
                        }

                    } else {
                        ToastUtils.show(getApplicationContext(), "请选择角色");
                    }
                }
            } else if (RequestType == RequestJoin) {
                NoteInfo noteinfo = (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
                Map<String, String> params = new HashMap<>();
                params.put("id", noteinfo.getId() + "");
                startRequest(ApiList.DATE_Join, params);
            } else if (RequestType == RequestComplain) {
                Map<String, String> params = new HashMap<>();
                String userphone = complainPhoneEdt.getText().toString();
                String useremail = complainEmailEdt.getText().toString();
                if (userphone.length() != 0 || useremail.length() != 0) {
                    NoteInfo noteinfo = (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
                    params.put("phone", userphone);
                    params.put("email", useremail);
                    params.put("id", noteinfo.getId() + "");
                    if (PageType == TypeTucao) {
                        startRequest(ApiList.TUCAO_AddComplain, params);
                    } else if (PageType == TypeDate) {
                        startRequest(ApiList.DATE_AddComplain, params);
                    } else if (PageType == TypeSecret) {
                        startRequest(ApiList.SECRET_AddComplain, params);
                    }
                } else
                    ToastUtils.show(getApplicationContext(), "请至少填写一个联系方式");
            } else if (RequestType == RequestAddComment) {
                NoteInfo noteinfo = (NoteInfo) getIntent().getSerializableExtra("NoteInfo");
                Map<String, String> params = new HashMap<>();
                params.put("receiveUserId", noteinfo.getUserId() + "");
                params.put("id", noteinfo.getId() + "");
                if (PageType == TypeTucao) {
                    startRequest(ApiList.TUCAO_AddComment, params);
                } else if (PageType == TypeDate) {
                    startRequest(ApiList.DATE_AddComment, params);
                } else if (PageType == TypeSecret) {
                    startRequest(ApiList.SECRET_AddComment, params);
                }
            }

        } else
            ToastUtils.show(getApplicationContext(), "请输入内容");
    }

    private void startRequest(String method, final Map<String, String> mapparams) {
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

    private void uploadRequest(String method, final Map<String, String> params, List<File> files, String key) {
        uploadRequest = new MultipartRequest(method, this, this, key, files, params) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = super.getHeaders();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QPublishAct.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

        };
        addUploadToRequestQueue(uploadRequest, method, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == RequestAddress){
            double lat = data.getDoubleExtra("lat", 0.0);
            double lng = data.getDoubleExtra("lng", 0.0);
            initSearch(lat, lng);
        }else if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            final String picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            picPaths.add(picPath);
            Log.i("Upload", "最终选择的图片=" + picPath);
//            BitmapFactory.Options options1 = new BitmapFactory.Options();
//            options1.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(picPath, options1);
//            options1.inSampleSize = ImageUtils.calculateInSampleSize(options1, screenWidth, screenWidth);  //110,160：转换后的宽和高，具体值会有些出入
//            options1.inJustDecodeBounds = false;
//            final Bitmap bitmap = BitmapFactory.decodeFile(picPath, options1);
//            Bitmap bmp= ImageUtils.getSmallBitmap(picPath);
            final Bitmap bitmap=ImageUtils.getSmallBitmap(picPath);
            seImageView(bitmap);

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    int bitmapSize=getBitmapSize(bitmap);
                    if(bitmapSize>300000) {
                        try {
//                            float scale=300000f/bitmapSize;
                            FileOutputStream out = new FileOutputStream(new File(picPath));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, (int)(100*scale), out);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private PoiSearch.Query query;
    private void initSearch(Double lat, Double lng) {
        query = new PoiSearch.Query("", "", PreferencesUtils.getString(this, "city_code"));
        // keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
        //共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 1000));//设置周边搜索的中心点以及区域
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> poiItemList = poiResult.getPois();
                if (poiItemList.size() > 0) {
                    PoiItem item = poiItemList.get(0);
                    AddrEdt.setText(item.getCityName() + item.getAdName() + item.getTitle());
                }
            }

            @Override
            public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

            }
        });//设置数据返回的监听器
        poiSearch.searchPOIAsyn();//开始搜索
        ToastUtils.show(this, "读取位置...");
    }

    public int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
