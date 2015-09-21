package com.xj.guanquan.activity.found;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.CircleUserInfo;
import com.xj.guanquan.model.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QCircleMemberActivity extends QBaseActivity {

    private SearchView searchView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView circleNumRecycler;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;

    private List<CircleUserInfo> circleUserInfoList;
    private TextView time;
    private TextView relation;
    private SimpleDraweeView headImg;
    private TextView sex;
    private TextView index;
    private TextView distance;
    private TextView age;
    private int lastVisibleItem;
    private Integer circleId;

    private StringRequest request;
    private int currentPage = 1;
    private int numPerPage = 20;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcircle_member);
    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcircle_member));
        _setRightHomeGone();
        initialize();
        circleId = getIntent().getExtras().getInt("circleId");

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        circleNumRecycler.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        circleNumRecycler.setLayoutManager(mLayoutManager);
        circleNumRecycler.setItemAnimator(new DefaultItemAnimator());
        initData();
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"relation", "sex", "age", "distance", "time", "avatar", "nickName"}, R.layout.list_circle_user_item, circleUserInfoList);
        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof SimpleDraweeView) {
                    SimpleDraweeView iv = (SimpleDraweeView) view;
                    Uri uri = Uri.parse((String) data);
                    iv.setImageURI(uri);
                    return true;
                } else if (view instanceof TextView && data instanceof Integer) {
                    TextView tv = (TextView) view;
                    Integer sex = (Integer) data;
                    if (sex == 1) {
                        tv.setText("男");
                    } else if (sex == 2) {
                        tv.setText("女");
                    }
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
        circleNumRecycler.setAdapter(mAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                addToRequestQueue(request, false);
            }
        });


        circleNumRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    if (isLoadMore) {
                        mAdapter.isLoadMore(true);
                        currentPage++;
                        addToRequestQueue(request, false);
                    } else {
                        mAdapter.isLoadMore(false);
                    }
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
        circleUserInfoList = new ArrayList<CircleUserInfo>();

    }

    @Override
    protected void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.GROUP_MEMBER, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(QCircleMemberActivity.this, "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("currentPage", String.valueOf(currentPage));
                map.put("numPerPage", String.valueOf(numPerPage));
                map.put("lng", PreferencesUtils.getString(QCircleMemberActivity.this, "lng"));
                map.put("lat", PreferencesUtils.getString(QCircleMemberActivity.this, "lat"));
                map.put("id", String.valueOf(circleId));
                return map;
            }
        };
        addToRequestQueue(request, true);
    }

    private void initialize() {
        searchView = (SearchView) findViewById(R.id.searchView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        circleNumRecycler = (RecyclerView) findViewById(R.id.circleNumRecycler);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView relation;
        private TextView sex;
        private TextView age;
        private TextView distance;
        private TextView time;
        private SimpleDraweeView headImg;

        public TextView getRelation() {
            return relation;
        }

        public void setRelation(TextView relation) {
            this.relation = relation;
        }

        public TextView getSex() {
            return sex;
        }

        public void setSex(TextView sex) {
            this.sex = sex;
        }

        public TextView getAge() {
            return age;
        }

        public void setAge(TextView age) {
            this.age = age;
        }

        public TextView getDistance() {
            return distance;
        }

        public void setDistance(TextView distance) {
            this.distance = distance;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public SimpleDraweeView getHeadImg() {
            return headImg;
        }

        public void setHeadImg(SimpleDraweeView headImg) {
            this.headImg = headImg;
        }

        public ItemViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            relation = (TextView) view.findViewById(R.id.relation);
            headImg = (SimpleDraweeView) view.findViewById(R.id.headImg);
            sex = (TextView) view.findViewById(R.id.sexTxt);
            index = (TextView) view.findViewById(R.id.index);
            distance = (TextView) view.findViewById(R.id.distance);
            age = (TextView) view.findViewById(R.id.age);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            circleUserInfoList.get(getAdapterPosition());
            //bundle.putSerializable("userInfo", new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
            toActivity(QUserDetailActivity.class, bundle);
        }
    }

    @Override
    protected void doResponse(Object response) {
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (currentPage == 1) {
            swipeRefresh.setRefreshing(false);
            circleUserInfoList = new ArrayList<CircleUserInfo>();
        }
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            if (result.getData().getJSONArray("content") != null) {
                List<CircleUserInfo> resultData = JSONArray.parseArray(result.getData().getJSONArray("content").toJSONString(), CircleUserInfo.class);
                circleUserInfoList.addAll(resultData);
                PageInfo pageInfo = JSONObject.parseObject(result.getData().getJSONObject("page").toJSONString(), PageInfo.class);
                if (circleUserInfoList.size() < pageInfo.getTotalCount()) {
                    isLoadMore = true;
                }
            } else {
                circleUserInfoList = null;
            }
            mAdapter.setData(circleUserInfoList);
            mAdapter.isLoadMore(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }
}
