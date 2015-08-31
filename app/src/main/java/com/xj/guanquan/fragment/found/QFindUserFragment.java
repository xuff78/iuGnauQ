package com.xj.guanquan.fragment.found;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.activity.home.QScreenActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link QFindUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QFindUserFragment extends QBaseFragment implements OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView findRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private List<UserInfo> userInfoList;
    private int lastVisibleItem;
    private TextView findCircle;
    private TextView screen;

    private StringRequest request;
    private int currentPage = 1;
    private int numPerPage = 20;
    private Integer sex;
    private Integer age;
    private String height;
    private Integer carCert;
    private Integer finallyTime;
    private Boolean isLoadMore = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QFindUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QFindUserFragment newInstance(String param1, String param2) {
        QFindUserFragment fragment = new QFindUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QFindUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qfind_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findRecyclerView = (RecyclerView) view.findViewById(R.id.findRecyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        findCircle = (TextView) view.findViewById(R.id.findCircle);
        screen = (TextView) view.findViewById(R.id.screen);
        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        findRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        findRecyclerView.setLayoutManager(mLayoutManager);
        findRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"nickName", "age", "sex", "avatar", "height", "weight", "car", "dating"}, R.layout.list_find_user_item, userInfoList);
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

        findRecyclerView.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                addToRequestQueue(request, false);
            }
        });


        findRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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

        findCircle.setOnClickListener(this);
        screen.setOnClickListener(this);
        initHandler();
    }

    private void initHandler() {
        request = new StringRequest(Request.Method.POST, ApiList.FIND_USER_LIST, this, this) {
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
                if (sex != null)
                    map.put("sex", String.valueOf(sex));
                if (age != null)
                    map.put("age", String.valueOf(age));
                if (carCert != null)
                    map.put("carCert", String.valueOf(carCert));
                if (finallyTime != null)
                    map.put("finallyTime", String.valueOf(finallyTime));
                if (!StringUtils.isEmpty(height))
                    map.put("height", height);
                map.put("lng", PreferencesUtils.getString(getActivity(), "lng"));
                map.put("lat", PreferencesUtils.getString(getActivity(), "lat"));
                return map;
            }
        };
        addToRequestQueue(request, true);
    }

    @Override
    public void onClick(View v) {
        if (v == findCircle) {
            ((QHomeActivity) getActivity()).initFragment(QFindCircleFragment.newInstance(null, null));
        } else if (v == screen) {
            Intent intent = new Intent(getActivity(), QScreenActivity.class);
            intent.putExtra("sex", sex);
            intent.putExtra("age", age);
            intent.putExtra("height", height);
            intent.putExtra("carCert", carCert);
            intent.putExtra("finallyTime", finallyTime);
            startActivityForResult(intent, 111);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener {
        private TextView nickName;
        private TextView sex;
        private SimpleDraweeView avatar;
        private TextView age;
        private TextView height;
        private TextView weight;
        private TextView car;
        private TextView dating;

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

        public TextView getSex() {
            return sex;
        }

        public void setSex(TextView sex) {
            this.sex = sex;
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

        public TextView getHeight() {
            return height;
        }

        public void setHeight(TextView height) {
            this.height = height;
        }

        public TextView getWeight() {
            return weight;
        }

        public void setWeight(TextView weight) {
            this.weight = weight;
        }

        public TextView getCar() {
            return car;
        }

        public void setCar(TextView car) {
            this.car = car;
        }

        public TextView getDating() {
            return dating;
        }

        public void setDating(TextView dating) {
            this.dating = dating;
        }

        private void initialize(View itemView) {
            nickName = (TextView) itemView.findViewById(R.id.name);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            sex = (TextView) itemView.findViewById(R.id.sex);
            age = (TextView) itemView.findViewById(R.id.age);
            height = (TextView) itemView.findViewById(R.id.height);
            weight = (TextView) itemView.findViewById(R.id.weight);
            car = (TextView) itemView.findViewById(R.id.carDescript);
            dating = (TextView) itemView.findViewById(R.id.dateDescript);
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
            } else {
                userInfoList = null;
            }
            mAdapter.setData(userInfoList);
            mAdapter.notifyDataSetChanged();
            mAdapter.isLoadMore(false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            sex = (Integer) data.getSerializableExtra("sex");
            age = (Integer) data.getSerializableExtra("age");
            finallyTime = (Integer) data.getSerializableExtra("finallyTime");
            carCert = (Integer) data.getSerializableExtra("carCert");
            height = data.getStringExtra("height");
            addToRequestQueue(request, true);
        }
    }
}
