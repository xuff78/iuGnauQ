package com.xj.guanquan.activity.found;

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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.PictureInfo;
import com.xj.guanquan.model.UserInfo;
import com.xj.guanquan.views.pullscrollview.PullScrollView;
import com.xj.guanquan.views.pullscrollview.PullScrollView.OnTurnListener;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

public class QUserDetailActivity extends QBaseActivity implements View.OnClickListener, OnTurnListener {
    private UserInfo userInfo;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private List<PictureInfo> pictureInfoList;
    private Button good;
    private TextView distance;
    private RecyclerView userPhotos;
    private PullScrollView scrollview;
    private TextView sex;
    private LinearLayout sexAgeArea;
    private TextView circleNum;
    private TextView income;
    private TextView carTwo;
    private LinearLayout attentionArea;
    private TextView haunt;
    private TextView carOne;
    private Button attentionBtn;
    private TextView registTime;
    private TextView distanceTime;
    private TextView interest;
    private SimpleDraweeView backgroundImage;
    private TextView age;
    private TextView marriage;
    private SimpleDraweeView socialImage;
    private TextView weight;
    private TextView roastTime;
    private TextView career;
    private SimpleDraweeView headImage;
    private TextView height;
    private TextView roastDistance;
    private TextView relation;
    private TextView roastContent;
    private ImageView roastMore;
    private TextView descript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quser_detail);
        initialize();
    }

    @Override
    protected void initView() {
        initialize();
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        _setHeaderTitle(userInfo.getName());
        _setRightHomeGone();
        _setRightHomeText("投诉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        userPhotos.setAdapter(mAdapter);
    }

    private void initData() {
        pictureInfoList = new ArrayList<PictureInfo>();
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
        pictureInfoList.add(new PictureInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg"));
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == good) {
            good.setSelected(!good.isSelected());
        } else if (v == attentionBtn) {
            attentionBtn.setSelected(!attentionBtn.isSelected());
            attentionBtn.setText(attentionBtn.isSelected() ? "取消关注" : "关注");
        }
    }

    private void initialize() {
        backgroundImage = (SimpleDraweeView) findViewById(R.id.backgroundImage);
        userPhotos = (RecyclerView) findViewById(R.id.userPhotos);
        scrollview = (PullScrollView) findViewById(R.id.scroll_view);
        good = (Button) findViewById(R.id.good);
        sexAgeArea = (LinearLayout) findViewById(R.id.sexAgeArea);
        distance = (TextView) findViewById(R.id.distance);
        sex = (TextView) findViewById(R.id.sex);
        sexAgeArea = (LinearLayout) findViewById(R.id.sexAgeArea);
        circleNum = (TextView) findViewById(R.id.circleNum);
        income = (TextView) findViewById(R.id.income);
        carTwo = (TextView) findViewById(R.id.carTwo);
        attentionArea = (LinearLayout) findViewById(R.id.attentionArea);
        haunt = (TextView) findViewById(R.id.haunt);
        carOne = (TextView) findViewById(R.id.carOne);
        attentionBtn = (Button) findViewById(R.id.attentionBtn);
        registTime = (TextView) findViewById(R.id.registTime);
        distanceTime = (TextView) findViewById(R.id.distanceTime);
        interest = (TextView) findViewById(R.id.interest);
        age = (TextView) findViewById(R.id.age);
        marriage = (TextView) findViewById(R.id.marriage);
        socialImage = (SimpleDraweeView) findViewById(R.id.socialImage);
        weight = (TextView) findViewById(R.id.weight);
        roastTime = (TextView) findViewById(R.id.roastTime);
        socialImage = (SimpleDraweeView) findViewById(R.id.socialImage);
        career = (TextView) findViewById(R.id.career);
        headImage = (SimpleDraweeView) findViewById(R.id.headImage);
        height = (TextView) findViewById(R.id.height);
        roastDistance = (TextView) findViewById(R.id.roastDistance);
        relation = (TextView) findViewById(R.id.relation);
        roastContent = (TextView) findViewById(R.id.roastContent);
        roastMore = (ImageView) findViewById(R.id.roastMore);
        descript = (TextView) findViewById(R.id.descript);

        good.setOnClickListener(this);
        attentionBtn.setOnClickListener(this);
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
}
