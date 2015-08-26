package com.xj.guanquan.fragment.roast;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QUserDetailActivity;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

/**
 * Created by 可爱的蘑菇 on 2015/8/23.
 */
public class TucaoListFrg extends QBaseFragment {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private TextView leftBtn, rightBtn;
    private int lastVisibleItem;
    private List<NoteInfo> notes;

    public static TucaoListFrg newInstance() {
        TucaoListFrg fragment = new TucaoListFrg();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tucao_item_frg, container, false);

        leftBtn = (TextView) v.findViewById(R.id.leftBtnSub);
        leftBtn.setOnClickListener(listener);
        rightBtn = (TextView) v.findViewById(R.id.rightBtnSub);
        rightBtn.setOnClickListener(listener);
        leftBtn.setText("好友吐槽");
        rightBtn.setText("附近吐槽");
        leftBtn.setSelected(true);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.dataList);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        notes = new ArrayList<NoteInfo>();
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg","小白兔", " ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        notes.add(new NoteInfo("http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "小白兔"," ♂ 23", "10:00", "爱风尚音乐会", "293", "100"));
        mAdapter = new RecyclerViewAdapter(new String[]{"userImg", "userName", "userAge", "createTime", "usrComment", "favorBtn", "commentNums"},
                R.layout.tucao_item_detail, notes);
//        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation) {
//                if (view instanceof SimpleDraweeView) {
//                    SimpleDraweeView iv = (SimpleDraweeView) view;
//                    Uri uri = Uri.parse((String) data);
//                    iv.setImageURI(uri);
//                    return true;
//                }
//                return false;
//            }
//        });

        //设置通用的Holder
        mAdapter.setViewHolderHelper(new RecyclerViewAdapter.ViewHolderHelper() {
            @Override
            public RecyclerView.ViewHolder bindItemViewHolder(View view) {
                return new ItemViewHolder(view);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        mAdapter.isLoadMore(true);
                    }
                }, 2000);
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.isLoadMore(false);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

        });

        return v;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftBtnSub:
                    leftBtn.setSelected(true);
                    rightBtn.setSelected(false);
                    break;
                case R.id.rightBtnSub:
                    leftBtn.setSelected(false);
                    rightBtn.setSelected(true);
                    break;
            }
        }
    };

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImg;
        private TextView userName;
        private TextView userAge;
        private TextView createTime;
        private TextView usrComment;
        private TextView favorBtn;
        private TextView replyNums;
        private TextView shareBtn;

        public TextView getUserName() {
            return userName;
        }

        public void setUserName(TextView userName) {
            this.userName = userName;
        }

        public ImageView getUserImg() {
            return userImg;
        }

        public void setUserImg(ImageView userImg) {
            this.userImg = userImg;
        }

        public TextView getUserAge() {
            return userAge;
        }

        public void setUserAge(TextView userAge) {
            this.userAge = userAge;
        }

        public TextView getCreateTime() {
            return createTime;
        }

        public void setCreateTime(TextView createTime) {
            this.createTime = createTime;
        }

        public TextView getUsrComment() {
            return usrComment;
        }

        public void setUsrComment(TextView usrComment) {
            this.usrComment = usrComment;
        }

        public TextView getFavorBtn() {
            return favorBtn;
        }

        public void setFavorBtn(TextView favorBtn) {
            this.favorBtn = favorBtn;
        }

        public TextView getReplyNums() {
            return replyNums;
        }

        public void setReplyNums(TextView replyNums) {
            this.replyNums = replyNums;
        }

        public TextView getShareBtn() {
            return shareBtn;
        }

        public void setShareBtn(TextView shareBtn) {
            this.shareBtn = shareBtn;
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
        }

        private void initialize(View itemView) {
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.headImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userAge = (TextView) itemView.findViewById(R.id.userAge);
            createTime = (TextView) itemView.findViewById(R.id.createTime);
            usrComment = (TextView) itemView.findViewById(R.id.usrComment);
            favorBtn = (TextView) itemView.findViewById(R.id.favorBtn);
            replyNums = (TextView) itemView.findViewById(R.id.replyNums);
            shareBtn = (TextView) itemView.findViewById(R.id.shareBtn);
        }
    }
}
