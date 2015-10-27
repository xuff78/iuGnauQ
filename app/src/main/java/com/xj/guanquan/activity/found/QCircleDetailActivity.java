package com.xj.guanquan.activity.found;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgDetailActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.CircleInfo;
import com.xj.guanquan.model.ExpandMsgInfo;
import com.xj.guanquan.model.PictureInfo;
import com.xj.guanquan.model.UserInfo;
import com.xj.guanquan.views.pullscrollview.PullScrollView;
import com.xj.guanquan.views.pullscrollview.PullScrollView.OnTurnListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QCircleDetailActivity extends QBaseActivity implements View.OnClickListener, OnTurnListener {
    private CircleInfo circleInfo;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private List<PictureInfo> pictureInfoList;
    private SimpleDraweeView backgroundImage;
    private RecyclerView userPhotos;
    private TextView circleNum;
    private TextView circleDesc;
    private SimpleDraweeView masterImage;
    private SimpleDraweeView headImageOne;
    private SimpleDraweeView headImageTwo;
    private SimpleDraweeView headImageThress;
    private ImageView roastMore;
    private TextView inviteCircle;
    private ImageView toInvite;
    private TextView circleLevel;
    private SimpleDraweeView circlePhotos;
    private TextView photoDesc;
    private ImageView circleMorePhoto;
    private TextView address;
    private TextView distance;
    private TextView createTiem;
    private PullScrollView scrollview;
    private Button joinCircleBtn;
    private LinearLayout attentionArea;
    private Button toMessageBtn;

    private StringRequest request;
    private StringRequest requestJoin;
    private StringRequest requestDetail;
    private String huanxinGroupId;
    JSONObject content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcircle_detail);
    }

    @Override
    protected void initView() {
        initialize();
        circleInfo = (CircleInfo) getIntent().getExtras().getSerializable("circleInfo");
        _setHeaderTitle(circleInfo.getName());
        _setRightHomeGone();

        scrollview.setHeader(backgroundImage);
        scrollview.setOnTurnListener(this);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        userPhotos.setHasFixedSize(true);
        // use a linear layout manager
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mGridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        userPhotos.setLayoutManager(mGridLayoutManager);
        userPhotos.setItemAnimator(new DefaultItemAnimator());

        initData();
        mAdapter = new RecyclerViewAdapter(new String[]{"picture"}, R.layout.list_photos_item, pictureInfoList);
        mAdapter.setIsShowFooter(false);
        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if (view instanceof SimpleDraweeView) {
                    SimpleDraweeView iv = (SimpleDraweeView) view;
                    Uri uri = Uri.parse((String) o);
                    iv.setImageURI(uri);
                    return true;
                }
                return false;
            }
        });
        mAdapter.setViewHolderHelper(new RecyclerViewAdapter.ViewHolderHelper() {
            @Override
            public RecyclerView.ViewHolder bindItemViewHolder(View view) {
                return new ItemViewHolder(view);
            }
        });
        mAdapter.isLoadMore(false);
        userPhotos.setAdapter(mAdapter);
        roastMore.setOnClickListener(this);
        circleMorePhoto.setOnClickListener(this);
        masterImage.setOnClickListener(this);
    }

    private void initData() {
        pictureInfoList = new ArrayList<PictureInfo>();
    }

    @Override
    protected void initHandler() {
        requestDetail = new StringRequest(Request.Method.POST, ApiList.GROUP_DETAIL, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QCircleDetailActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", String.valueOf(circleInfo.getId()));
                map.put("lng", PreferencesUtils.getString(QCircleDetailActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QCircleDetailActivity.this, "lat"));
                return map;
            }
        };

        requestJoin = new StringRequest(Request.Method.POST, ApiList.GROUP_JOIN, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QCircleDetailActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", String.valueOf(circleInfo.getId()));
                map.put("lng", PreferencesUtils.getString(QCircleDetailActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QCircleDetailActivity.this, "lat"));
                return map;
            }
        };
        request = requestDetail;
        addToRequestQueue(request, ApiList.GROUP_DETAIL, true);
    }

    @Override
    public void onClick(View v) {
        if (v == roastMore) {
            Bundle bundle = new Bundle();
            bundle.putInt("circleId", circleInfo.getId());
            toActivity(QCircleMemberActivity.class, bundle);
        } else if (v == circleMorePhoto) {
            toActivity(QCirclePhotosActivity.class);
        } else if (v == toMessageBtn) {
            // 进入聊天页面
            Intent intent = new Intent(this, QMsgDetailActivity.class);
            intent.putExtra("chatType", QMsgDetailActivity.CHATTYPE_GROUP);
            intent.putExtra("groupId", huanxinGroupId);
            intent.putExtra("groupInfo", circleInfo);
            intent.putExtra("messageInfo", new ExpandMsgInfo(null, null, null, circleInfo.getName(),
                    circleInfo.getLogo(), String.valueOf(circleInfo.getId())));
            intent.putExtra("title", circleInfo.getName());
            startActivity(intent);
        } else if (v == joinCircleBtn) {
            addToRequestQueue(requestJoin, ApiList.GROUP_JOIN, true);
        } else if (v == masterImage) {
            Bundle bundle = new Bundle();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(content.getInteger("ownerId"));
            bundle.putSerializable("userInfo", userInfo);
            toActivity(QUserDetailActivity.class, bundle);
        }
    }

    private void initialize() {
        backgroundImage = (SimpleDraweeView) findViewById(R.id.backgroundImage);
        userPhotos = (RecyclerView) findViewById(R.id.userPhotos);
        circleNum = (TextView) findViewById(R.id.circleNumber);
        circleDesc = (TextView) findViewById(R.id.circleDesc);
        masterImage = (SimpleDraweeView) findViewById(R.id.masterImage);
        headImageOne = (SimpleDraweeView) findViewById(R.id.headImageOne);
        headImageTwo = (SimpleDraweeView) findViewById(R.id.headImageTwo);
        headImageThress = (SimpleDraweeView) findViewById(R.id.headImageThress);
        roastMore = (ImageView) findViewById(R.id.roastMore);
        inviteCircle = (TextView) findViewById(R.id.inviteCircle);
        toInvite = (ImageView) findViewById(R.id.toInvite);
        circleLevel = (TextView) findViewById(R.id.circleLevel);
        circlePhotos = (SimpleDraweeView) findViewById(R.id.circlePhotos);
        photoDesc = (TextView) findViewById(R.id.photoDesc);
        circleMorePhoto = (ImageView) findViewById(R.id.circleMorePhoto);
        address = (TextView) findViewById(R.id.address);
        distance = (TextView) findViewById(R.id.distance);
        createTiem = (TextView) findViewById(R.id.createTiem);
        scrollview = (PullScrollView) findViewById(R.id.scroll_view);
        joinCircleBtn = (Button) findViewById(R.id.joinCircleBtn);
        attentionArea = (LinearLayout) findViewById(R.id.attentionArea);
        toMessageBtn = (Button) findViewById(R.id.toMessageBtn);
    }

    @Override
    public void onTurn() {

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private SimpleDraweeView picture;

        public ItemViewHolder(View itemView) {
            super(itemView);
            picture = (SimpleDraweeView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public SimpleDraweeView getPicture() {
            return picture;
        }

        public void setPicture(SimpleDraweeView picture) {
            this.picture = picture;
        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @Override
    protected void doResponse(Object response) {
        final ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (StringUtils.isEquals(requestMethod, ApiList.GROUP_DETAIL)) {
            content = result.getData().getJSONObject("content");
            circleNum.setText(String.valueOf(circleInfo.getId()));
            circleDesc.setText(content.getString("description"));
            Uri uri = Uri.parse(content.getString("ownerAvatar"));
            masterImage.setImageURI(uri);
            JSONArray avaratList = content.getJSONArray("avatarList");
            if (avaratList.size() > 0) {
                Uri uri1 = Uri.parse(avaratList.getJSONObject(0).getString("avatar"));
                headImageOne.setImageURI(uri1);
            } else {
                headImageOne.setVisibility(View.GONE);
            }
            if (avaratList.size() > 1 && avaratList.getJSONObject(1) != null) {
                Uri uri2 = Uri.parse(avaratList.getJSONObject(1).getString("avatar"));
                headImageTwo.setImageURI(uri2);
            } else {
                headImageTwo.setVisibility(View.GONE);
            }
            if (avaratList.size() > 2 && avaratList.getJSONObject(2) != null) {
                Uri uri3 = Uri.parse(avaratList.getJSONObject(2).getString("avatar"));
                headImageThress.setImageURI(uri3);
            } else {
                headImageThress.setVisibility(View.GONE);
            }
            circleLevel.setText(content.getString("level"));
            address.setText(content.getString("address"));
            createTiem.setText(content.getString("time"));
            distance.setText(content.getString("distance"));
            Uri circlePic = Uri.parse(content.getString("albumPicture"));
            circlePhotos.setImageURI(circlePic);
            photoDesc.setText(content.getString("albumContent"));
            int isJoined = content.getInteger("isJoined");
            if (isJoined == 1) {
                attentionArea.setVisibility(View.GONE);
                toMessageBtn.setVisibility(View.VISIBLE);
            } else {
                toMessageBtn.setVisibility(View.GONE);
            }
            huanxinGroupId = content.getString("huanxinGroupId");

        } else if (StringUtils.isEquals(requestMethod, ApiList.GROUP_JOIN)) {
            alertDialog(result.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request = requestDetail;
                    addToRequestQueue(request, ApiList.GROUP_DETAIL, true);
                }
            });
        }
    }
}
