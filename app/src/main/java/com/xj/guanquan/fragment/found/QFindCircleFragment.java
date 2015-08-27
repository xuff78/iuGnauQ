package com.xj.guanquan.fragment.found;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QCircleDetailActivity;
import com.xj.guanquan.activity.home.QHomeActivity;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.CircleInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link QFindCircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QFindCircleFragment extends Fragment implements OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView findRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private List<CircleInfo> circleInfoList;
    private int lastVisibleItem;
    private TextView findUser;

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
        findRecyclerView = (RecyclerView) view.findViewById(R.id.findRecyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        findUser = (TextView) view.findViewById(R.id.findUser);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        findRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        findRecyclerView.setLayoutManager(mLayoutManager);
        findRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initData();
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"circleName", "level", "circleDesc", "headImage", "distance"}, R.layout.list_find_circle_item, circleInfoList);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        mAdapter.isLoadMore(true);
                    }
                }, 2000);
            }
        });


        findRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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

        findUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findUser) {
            ((QHomeActivity) getActivity()).initFragment(QFindUserFragment.newInstance(null, null));
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener {
        private SimpleDraweeView headImage;
        private TextView circleName;
        private TextView level;
        private TextView distance;
        private RelativeLayout nameArea;
        private TextView circleDesc;

        public SimpleDraweeView getHeadImage() {
            return headImage;
        }

        public void setHeadImage(SimpleDraweeView headImage) {
            this.headImage = headImage;
        }

        public TextView getCircleName() {
            return circleName;
        }

        public void setCircleName(TextView circleName) {
            this.circleName = circleName;
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

        public TextView getCircleDesc() {
            return circleDesc;
        }

        public void setCircleDesc(TextView circleDesc) {
            this.circleDesc = circleDesc;
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void initialize(View itemView) {
            headImage = (SimpleDraweeView) itemView.findViewById(R.id.headImage);
            circleName = (TextView) itemView.findViewById(R.id.circleName);
            level = (TextView) itemView.findViewById(R.id.level);
            distance = (TextView) itemView.findViewById(R.id.distance);
            nameArea = (RelativeLayout) itemView.findViewById(R.id.nameArea);
            circleDesc = (TextView) itemView.findViewById(R.id.circleDesc);
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

    private void initData() {
        circleInfoList = new ArrayList<CircleInfo>();
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
        circleInfoList.add(new CircleInfo("爱疯吧", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", "LV  3", "25 km", "爱音乐，爱风狂，爱是一直的追求"));
    }
}
