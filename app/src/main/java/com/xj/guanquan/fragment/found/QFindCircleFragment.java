package com.xj.guanquan.fragment.found;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QCircleDetailActivity;
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.common.ResponseResult;
import com.xj.guanquan.model.CircleInfo;
import com.xj.guanquan.model.PageInfo;

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
 * Use the {@link QFindCircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QFindCircleFragment extends QBaseFragment implements OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout titleArea;
    private RecyclerView findRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private List<CircleInfo> circleInfoList;
    private int lastVisibleItem;
    private TextView findUser;

    private StringRequest request;
    private int currentPage = 1;
    private int numPerPage = 20;
    private boolean isLoadMore;
    private String method;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QFindUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QFindCircleFragment newInstance(String param1, String param2) {
        QFindCircleFragment fragment = new QFindCircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QFindCircleFragment() {
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
        return inflater.inflate(R.layout.fragment_qfind_circle, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleArea = (RelativeLayout) view.findViewById(R.id.titleArea);
        findRecyclerView = (RecyclerView) view.findViewById(R.id.findRecyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        findUser = (TextView) view.findViewById(R.id.findUser);

        if (mParam1 != null) {
            titleArea.setVisibility(View.GONE);
            method = ApiList.GROUP_MYGROUP;
        } else {
            method = ApiList.FIND_GROUP_LIST;
        }
        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        findRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        findRecyclerView.setLayoutManager(mLayoutManager);
        findRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"name", "level", "description", "logo", "distance"}, R.layout.list_find_circle_item, circleInfoList);
        mAdapter.setViewBinder(new RecyclerViewAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof SimpleDraweeView) {
                    SimpleDraweeView iv = (SimpleDraweeView) view;
                    Uri uri = Uri.parse(data == null ? "" : (String) data);
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

        findUser.setOnClickListener(this);
        initHandler();
    }

    private void initHandler() {
        request = new StringRequest(Request.Method.POST, method, this, this) {
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

    @Override
    public void onClick(View v) {
        if (v == findUser) {
            ((QHomeActivity) getActivity()).initFragment(QFindUserFragment.newInstance(null, null));
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener {
        private SimpleDraweeView logo;
        private TextView name;
        private TextView level;
        private TextView distance;
        private RelativeLayout nameArea;
        private TextView description;

        public SimpleDraweeView getLogo() {
            return logo;
        }

        public void setLogo(SimpleDraweeView logo) {
            this.logo = logo;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getLevel() {
            return level;
        }

        public void setLevel(TextView level) {
            this.level = level;
        }

        public TextView getDistance() {
            return distance;
        }

        public void setDistance(TextView distance) {
            this.distance = distance;
        }

        public RelativeLayout getNameArea() {
            return nameArea;
        }

        public void setNameArea(RelativeLayout nameArea) {
            this.nameArea = nameArea;
        }

        public TextView getDescription() {
            return description;
        }

        public void setDescription(TextView description) {
            this.description = description;
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void initialize(View itemView) {
            logo = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.circleName);
            level = (TextView) itemView.findViewById(R.id.level);
            distance = (TextView) itemView.findViewById(R.id.distance);
            nameArea = (RelativeLayout) itemView.findViewById(R.id.nameArea);
            description = (TextView) itemView.findViewById(R.id.circleDesc);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("circleInfo", circleInfoList.get(getAdapterPosition()));
            ((QBaseActivity) getActivity()).toActivity(QCircleDetailActivity.class, bundle);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onResponse(Object response) {
        super.onResponse(response);
        ResponseResult result = JSONObject.parseObject(response.toString(), ResponseResult.class);
        if (currentPage == 1) {
            swipeRefresh.setRefreshing(false);
            circleInfoList = new ArrayList<CircleInfo>();
        }
        if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_SUCCESS)) {
            if (result.getData().getJSONArray("content") != null) {
                List<CircleInfo> circleInfos = JSONArray.parseArray(result.getData().getJSONArray("content").toJSONString(), CircleInfo.class);
                circleInfoList.addAll(circleInfos);
                if (result.getData().getJSONObject("page") != null) {
                    PageInfo pageInfo = JSONObject.parseObject(result.getData().getJSONObject("page").toJSONString(), PageInfo.class);
                    if (circleInfoList.size() < pageInfo.getTotalCount()) {
                        isLoadMore = true;
                    }
                }
            }
            mAdapter.setData(circleInfoList);
            mAdapter.isLoadMore(false);
            mAdapter.notifyDataSetChanged();

        } else if (StringUtils.isEquals(result.getCode(), ApiList.REQUEST_LOGIN)) {
            ((QBaseActivity) getActivity()).alertDialog(result.getMsg(), null);
        } else {
            ((QBaseActivity) getActivity()).alertDialog(result.getMsg(), null);
        }
    }
}
