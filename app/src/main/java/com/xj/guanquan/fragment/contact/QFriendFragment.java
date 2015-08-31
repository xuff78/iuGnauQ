package com.xj.guanquan.fragment.contact;

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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

public class QFriendFragment extends QBaseFragment {

    public static QFriendFragment newInstance() {
        QFriendFragment fragment = new QFriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private RecyclerView blackListView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private int lastVisibleItem;
    private List<UserInfo> userInfoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_qblack_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        initialize(v);
        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        blackListView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        blackListView.setLayoutManager(mLayoutManager);
        blackListView.setItemAnimator(new DefaultItemAnimator());
        initData();
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"name", "headImage", "age", "sex", "time", "dateDescript", "distance"}, R.layout.list_blacklist_item, userInfoList);
        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof SimpleDraweeView) {
                    SimpleDraweeView iv = (SimpleDraweeView) view;
                    Uri uri = Uri.parse((String) data);
                    iv.setImageURI(uri);
                    return true;
                }
                return false;
            }
        });

        //设置通用的Holder
        mAdapter.setViewHolderHelper(new RecyclerViewAdapter.ViewHolderHelper() {
            @Override
            public RecyclerView.ViewHolder bindItemViewHolder(View view) {
                return new ItemViewHolder(view);
            }
        });
        blackListView.setAdapter(mAdapter);

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


        blackListView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
    }

    private void initData() {
        userInfoList = new ArrayList<UserInfo>();
        userInfoList = new ArrayList<UserInfo>();
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
//        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "爱音乐，爱疯尚，爱就是一支的追求", "30min", "25mk"));
    }

    private void initialize(View view) {
        blackListView = (RecyclerView) view.findViewById(R.id.blackListView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView headImage;
        private TextView name;
        private TextView age;
        private TextView sex;
        private TextView dateDescript;
        private TextView time;
        private TextView distance;


        public ItemViewHolder(View view) {
            super(view);
            headImage = (SimpleDraweeView) view.findViewById(R.id.avatar);
            name = (TextView) view.findViewById(R.id.name);
            age = (TextView) view.findViewById(R.id.age);
            sex = (TextView) view.findViewById(R.id.sex);
            dateDescript = (TextView) view.findViewById(R.id.dateDescript);
            time = (TextView) view.findViewById(R.id.time);
            distance = (TextView) view.findViewById(R.id.distance);
            view.setOnClickListener(this);
        }

        public SimpleDraweeView getHeadImage() {
            return headImage;
        }

        public void setHeadImage(SimpleDraweeView headImage) {
            this.headImage = headImage;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getAge() {
            return age;
        }

        public void setAge(TextView age) {
            this.age = age;
        }

        public TextView getSex() {
            return sex;
        }

        public void setSex(TextView sex) {
            this.sex = sex;
        }

        public TextView getDateDescript() {
            return dateDescript;
        }

        public void setDateDescript(TextView dateDescript) {
            this.dateDescript = dateDescript;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getDistance() {
            return distance;
        }

        public void setDistance(TextView distance) {
            this.distance = distance;
        }


        @Override
        public void onClick(View v) {

        }
    }

}
