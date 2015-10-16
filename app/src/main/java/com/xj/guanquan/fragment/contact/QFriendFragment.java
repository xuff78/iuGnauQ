package com.xj.guanquan.fragment.contact;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QUserDetailActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.PageInfo;
import com.xj.guanquan.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QFriendFragment extends QBaseFragment {
    private StringRequest request;
    private int currentPage = 1;
    private int numPerPage = 20;
    private boolean isLoadMore = false;

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
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"nickName", "avatar", "age", "sexTxt", "time", "signature", "distance"}, R.layout.list_blacklist_item, userInfoList);
        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof SimpleDraweeView) {
                    SimpleDraweeView iv = (SimpleDraweeView) view;
                    Uri uri = Uri.parse((String) data);
                    iv.setImageURI(uri);
                    return true;
                } else if (data instanceof String && (StringUtils.isEquals("♂ ", data.toString()) || StringUtils.isEquals("♀ ", data.toString()))) {
                    ((TextView) view).setText(data.toString());
                    if (StringUtils.isEquals("♂ ", data.toString())) {
                        ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.age_female_border_conner);
                    } else {
                        ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.age_sex_border_conner);
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
        blackListView.setAdapter(mAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                addToRequestQueue(request, false);
            }
        });


        blackListView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
        initHandler();
    }

    private void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.CONTACT_FRIEND_LIST, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject loginData = JSONObject.parseObject(PreferencesUtils.getString(getActivity(), "loginData"));
                map.put("authToken", loginData.getJSONObject("data").getString("authToken"));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("currentPage", String.valueOf(currentPage));
                map.put("numPerPage", String.valueOf(numPerPage));
                map.put("lng", PreferencesUtils.getString(getActivity(), "lng"));
                map.put("lat", PreferencesUtils.getString(getActivity(), "lat"));
                return map;
            }
        };
        addToRequestQueue(request, true);
    }

    private void initialize(View view) {
        blackListView = (RecyclerView) view.findViewById(R.id.blackListView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView nickName;
        private TextView sexTxt;
        private SimpleDraweeView avatar;
        private TextView age;
        private TextView time;
        private TextView signature;
        private TextView distance;

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public TextView getNickName() {
            return nickName;
        }

        public void setNickName(TextView nickName) {
            this.nickName = nickName;
        }

        public TextView getSexTxt() {
            return sexTxt;
        }

        public void setSexTxt(TextView sexTxt) {
            this.sexTxt = sexTxt;
        }

        public SimpleDraweeView getAvatar() {
            return avatar;
        }

        public void setAvatar(SimpleDraweeView avatar) {
            this.avatar = avatar;
        }

        public TextView getAge() {
            return age;
        }

        public void setAge(TextView age) {
            this.age = age;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getSignature() {
            return signature;
        }

        public void setSignature(TextView signature) {
            this.signature = signature;
        }

        public TextView getDistance() {
            return distance;
        }

        public void setDistance(TextView distance) {
            this.distance = distance;
        }

        private void initialize(View itemView) {
            nickName = (TextView) itemView.findViewById(R.id.name);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            sexTxt = (TextView) itemView.findViewById(R.id.sexTxt);
            age = (TextView) itemView.findViewById(R.id.age);
            time = (TextView) itemView.findViewById(R.id.time);
            distance = (TextView) itemView.findViewById(R.id.distance);
            signature = (TextView) itemView.findViewById(R.id.dateDescript);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userInfo", userInfoList.get(getAdapterPosition()));
            ((QBaseActivity) getActivity()).toActivity(QUserDetailActivity.class, bundle);
        }

        @Override
        public boolean onLongClick(View v) {
            //todo long click
            return true;
        }
    }

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (currentPage == 1) {
            swipeRefresh.setRefreshing(false);
            userInfoList = new ArrayList<UserInfo>();
        }
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            if (result.getData().getJSONArray("content") != null) {
                List<UserInfo> resultData = JSONArray.parseArray(result.getData().getJSONArray("content").toJSONString(), UserInfo.class);
                userInfoList.addAll(resultData);
                PageInfo pageInfo = JSONObject.parseObject(result.getData().getJSONObject("page").toJSONString(), PageInfo.class);
                if (userInfoList.size() < pageInfo.getTotalCount()) {
                    isLoadMore = true;
                }
            }
            mAdapter.setData(userInfoList);
            mAdapter.isLoadMore(false);
            mAdapter.notifyDataSetChanged();
        } else if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_LOGIN)) {
            ((QBaseActivity) getActivity()).alertDialog(result.getMsg(), null);
        } else {
            ((QBaseActivity) getActivity()).alertDialog(result.getMsg(), null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }

}
