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
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link QFindUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QFindUserFragment extends Fragment {
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

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        findRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        findRecyclerView.setLayoutManager(mLayoutManager);
        findRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initData();
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"name", "age", "sex", "headImage", "height", "weight", "carDescript", "dateDescript"}, R.layout.list_find_user_item, userInfoList);
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

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView sex;
        private SimpleDraweeView headImage;
        private TextView age;
        private TextView height;
        private TextView weight;
        private TextView carDescript;
        private TextView dateDescript;

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
        }

        public SimpleDraweeView getHeadImage() {
            return headImage;
        }

        public void setHeadImage(SimpleDraweeView headImage) {
            this.headImage = headImage;
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

        public TextView getCarDescript() {
            return carDescript;
        }

        public void setCarDescript(TextView carDescript) {
            this.carDescript = carDescript;
        }

        public TextView getDateDescript() {
            return dateDescript;
        }

        public void setDateDescript(TextView dateDescript) {
            this.dateDescript = dateDescript;
        }

        public TextView getSex() {
            return sex;
        }

        public void setSex(TextView sex) {
            this.sex = sex;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        private void initialize(View itemView) {
            name = (TextView) itemView.findViewById(R.id.name);
            headImage = (SimpleDraweeView) itemView.findViewById(R.id.headImage);
            sex = (TextView) itemView.findViewById(R.id.sex);
            headImage = (SimpleDraweeView) itemView.findViewById(R.id.headImage);
            age = (TextView) itemView.findViewById(R.id.age);
            height = (TextView) itemView.findViewById(R.id.height);
            weight = (TextView) itemView.findViewById(R.id.weight);
            carDescript = (TextView) itemView.findViewById(R.id.carDescript);
            dateDescript = (TextView) itemView.findViewById(R.id.dateDescript);
        }
    }

    private void initData() {
        userInfoList = new ArrayList<UserInfo>();
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
        userInfoList.add(new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会"));
    }
}
