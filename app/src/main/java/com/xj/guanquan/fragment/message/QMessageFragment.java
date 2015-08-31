package com.xj.guanquan.fragment.message;

import android.content.Intent;
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
import com.xj.guanquan.activity.message.QMsgDetailActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.model.MessageInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link QMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QMessageFragment extends QBaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView limitNum;
    private TextView weather;
    private RecyclerView messageRecycler;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private int lastVisibleItem;

    private List<MessageInfo> messageInfoList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QFindUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QMessageFragment newInstance(String param1, String param2) {
        QMessageFragment fragment = new QMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QMessageFragment() {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        messageRecycler.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        messageRecycler.setLayoutManager(mLayoutManager);
        messageRecycler.setItemAnimator(new DefaultItemAnimator());

        initData();
        //通用adapter设置数据
        mAdapter = new RecyclerViewAdapter(new String[]{"name", "lastMsg", "msgNum", "headImage", "time"}, R.layout.list_message_record_item, messageInfoList);
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
                    tv.setText("[" + (Integer) data + "]");
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

        messageRecycler.setAdapter(mAdapter);
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


        messageRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
        messageInfoList = new ArrayList<MessageInfo>();
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
        messageInfoList.add(new MessageInfo("小明", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", 25, "下午12:57", "下个周三见面吧，小宇告诉我们下个周有个非常重要的事情"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qmessage, container, false);
    }

    private void initialize(View view) {
        limitNum = (TextView) view.findViewById(R.id.limitNum);
        weather = (TextView) view.findViewById(R.id.weather);
        messageRecycler = (RecyclerView) view.findViewById(R.id.messageRecycler);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView headImage;
        private TextView name;
        private TextView time;
        private TextView msgNum;
        private TextView lastMsg;

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

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getMsgNum() {
            return msgNum;
        }

        public void setMsgNum(TextView msgNum) {
            this.msgNum = msgNum;
        }

        public TextView getLastMsg() {
            return lastMsg;
        }

        public void setLastMsg(TextView lastMsg) {
            this.lastMsg = lastMsg;
        }

        public ItemViewHolder(View view) {
            super(view);
            initialize(view);
            view.setOnClickListener(this);
        }

        private void initialize(View view) {
            headImage = (SimpleDraweeView) view.findViewById(R.id.avatar);
            name = (TextView) view.findViewById(R.id.name);
            time = (TextView) view.findViewById(R.id.time);
            msgNum = (TextView) view.findViewById(R.id.msgNum);
            lastMsg = (TextView) view.findViewById(R.id.lastMsg);
        }

        @Override
        public void onClick(View v) {
            //处理RecyclerView的点击事件
            startActivity(new Intent(getActivity(),QMsgDetailActivity.class));
        }
    }
}
