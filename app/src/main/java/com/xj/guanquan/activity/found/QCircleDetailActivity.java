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
import com.xj.guanquan.model.CircleInfo;
import com.xj.guanquan.model.PictureInfo;
import com.xj.guanquan.views.pullscrollview.PullScrollView;
import com.xj.guanquan.views.pullscrollview.PullScrollView.OnTurnListener;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

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
        _setHeaderTitle(circleInfo.getCircleName());
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
    }

    private void initData() {
        pictureInfoList = new ArrayList<PictureInfo>();
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
        if (v == roastMore) {
            toActivity(QCircleMemberActivity.class);
        } else if (v == circleMorePhoto) {
            toActivity(QCirclePhotosActivity.class);
        }
    }

    private void initialize() {
        backgroundImage = (SimpleDraweeView) findViewById(R.id.backgroundImage);
        userPhotos = (RecyclerView) findViewById(R.id.userPhotos);
        circleNum = (TextView) findViewById(R.id.circleNum);
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
