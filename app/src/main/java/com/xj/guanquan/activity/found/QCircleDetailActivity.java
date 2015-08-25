package com.xj.guanquan.activity.found;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    private SimpleDraweeView backgroundImage;
    private RecyclerView userPhotos;
    private PullScrollView scrollview;
    private GridLayoutManager mGridLayoutManager;

    private RecyclerViewAdapter mAdapter;
    private List<PictureInfo> pictureInfoList;

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
    }

    private void initialize() {
        backgroundImage = (SimpleDraweeView) findViewById(R.id.backgroundImage);
        userPhotos = (RecyclerView) findViewById(R.id.userPhotos);
        scrollview = (PullScrollView) findViewById(R.id.scroll_view);
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
