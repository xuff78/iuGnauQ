package com.xj.guanquan.fragment.message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgDetailActivity;
import com.xj.guanquan.common.Constant;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.common.SmileUtils;
import com.xj.guanquan.model.CircleInfo;
import com.xj.guanquan.model.ExpandMsgInfo;
import com.xj.guanquan.model.MessageInfo;
import com.xj.guanquan.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;
import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

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

    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private UserInfo userInfo;

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
    public void onResume() {
        super.onResume();
        refresh();
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
        messageInfoList = new ArrayList<MessageInfo>();
        conversationList.addAll(loadConversationsWithRecentChat());
        for (EMConversation conversation : conversationList) {
            try {
                JSONObject jsonObject = null;
                String userName = null;
                String userIcon = null;

                if (conversation.isGroup()) {
                    jsonObject = conversation.getLastMessage().getJSONObjectAttribute("groupInfo");
                    userName = jsonObject != null ? jsonObject.getString("groupName") : "";
                    userIcon = jsonObject != null ? jsonObject.getString("groupIcon") : "";
                } else {
                    jsonObject = conversation.getLastMessage().getJSONObjectAttribute("fromUserInfo");
                    JSONObject toUserJson = conversation.getLastMessage().getJSONObjectAttribute("toUserInfo");
                    if (jsonObject != null && StringUtils.isEquals(jsonObject.getString("userId"), String.valueOf(userInfo.getUserId()))) {
                        jsonObject = toUserJson;
                    }
                    userName = jsonObject != null ? jsonObject.getString("userName") : "";
                    userIcon = jsonObject != null ? jsonObject.getString("userIcon") : "";
                }
                MessageInfo messageInfo = new MessageInfo(userName, userIcon,
                        conversation.getUnreadMsgCount(),
                        DateUtils.getTimestampString(new Date(conversation.getLastMessage().getMsgTime())),
                        SmileUtils.getSmiledText(getActivity(), getMessageDigest(conversation.getLastMessage(), getActivity())));
                messageInfoList.add(messageInfo);
            } catch (EaseMobException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                } else if (view instanceof TextView && data instanceof Spannable) {
                    TextView tv = (TextView) view;
                    Spannable spannable = (Spannable) data;
                    tv.setText(spannable, TextView.BufferType.SPANNABLE);
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
                refresh();
                swipeRefresh.setRefreshing(false);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getString(getActivity(), "loginData"));
        userInfo = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserInfo.class);
        return inflater.inflate(R.layout.fragment_qmessage, container, false);
    }

    private void initialize(View view) {
        limitNum = (TextView) view.findViewById(R.id.limitNum);
        weather = (TextView) view.findViewById(R.id.weather);
        messageRecycler = (RecyclerView) view.findViewById(R.id.messageRecycler);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    /**
     * 获取所有会话
     *
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        messageInfoList.clear();
        conversationList.addAll(loadConversationsWithRecentChat());
        for (EMConversation conversation : conversationList) {
            try {
                JSONObject jsonObject = null;
                String userName = null;
                String userIcon = null;

                if (conversation.isGroup()) {
                    jsonObject = conversation.getLastMessage().getJSONObjectAttribute("groupInfo");
                    userName = jsonObject != null ? jsonObject.getString("groupName") : "";
                    userIcon = jsonObject != null ? jsonObject.getString("groupIcon") : "";
                } else {
                    jsonObject = conversation.getLastMessage().getJSONObjectAttribute("fromUserInfo");
                    JSONObject toUserJson = conversation.getLastMessage().getJSONObjectAttribute("toUserInfo");
                    if (jsonObject != null && StringUtils.isEquals(jsonObject.getString("userId"), String.valueOf(userInfo.getUserId()))) {
                        jsonObject = toUserJson;
                    }
                    userName = jsonObject != null ? jsonObject.getString("userName") : "";
                    userIcon = jsonObject != null ? jsonObject.getString("userIcon") : "";
                }


                MessageInfo messageInfo = new MessageInfo(userName, userIcon,
                        conversation.getUnreadMsgCount(),
                        DateUtils.getTimestampString(new Date(conversation.getLastMessage().getMsgTime())),
                        SmileUtils.getSmiledText(getActivity(), getMessageDigest(conversation.getLastMessage(), getActivity())));
                messageInfoList.add(messageInfo);
            } catch (EaseMobException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (mAdapter != null)
            mAdapter.setData(messageInfoList);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture) + imageBody.getFileName();
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息

                if (isRobotMenuMessage(message)) {
                    digest = getRobotMenuMessageDigest(message);
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                EMLog.e("QMessageFragment", "unknow type");
                return "";
        }

        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public boolean isRobotMenuMessage(EMMessage message) {

        try {
            JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
            if (jsonObj.has("choice")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public String getRobotMenuMessageDigest(EMMessage message) {
        String title = "";
        try {
            JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
            if (jsonObj.has("choice")) {
                JSONObject jsonChoice = jsonObj.getJSONObject("choice");
                title = jsonChoice.getString("title");
            }
        } catch (Exception e) {
        }
        return title;
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
            EMConversation conversation = conversationList.get(getAdapterPosition());
            String username = conversation.getUserName();
            String loginName = PreferencesUtils.getString(getActivity(), "username", "");
            MessageInfo messageInfo = messageInfoList.get(getAdapterPosition());
            if (StringUtils.isEquals(username, loginName)) {
                ((QBaseActivity) getActivity()).showToastShort(getString(R.string.Cant_chat_with_yourself));
            } else {
                // 进入聊天页面
                Intent intent = new Intent(getActivity(), QMsgDetailActivity.class);
                if (conversation.isGroup()) {
                    if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                        // it is group chat
                        intent.putExtra("chatType", QMsgDetailActivity.CHATTYPE_CHATROOM);
                        intent.putExtra("groupId", username);
                        intent.putExtra("messageInfo", new ExpandMsgInfo(null, null, null, messageInfo.getName(), messageInfo.getHeadImage(), username));
                    } else {
                        // it is group chat
                        try {
                            intent.putExtra("chatType", QMsgDetailActivity.CHATTYPE_GROUP);
                            JSONObject jsonObject = conversation.getLastMessage().getJSONObjectAttribute("groupInfo");
                            String userName = jsonObject != null ? jsonObject.getString("groupName") : "";
                            String userIcon = jsonObject != null ? jsonObject.getString("groupIcon") : "";
                            CircleInfo circleInfo = new CircleInfo();
                            circleInfo.setPicture(userIcon);
                            circleInfo.setName(userName);
                            circleInfo.setId(Integer.valueOf(jsonObject.getString("groupId")));
                            intent.putExtra("groupInfo", circleInfo);
                            intent.putExtra("messageInfo", new ExpandMsgInfo(null, null, null, messageInfo.getName(), messageInfo.getHeadImage(), username));
                            intent.putExtra("groupId", username);
                        } catch (Exception e) {
                            Log.e("GroupChat", e.getLocalizedMessage());
                        }
                    }

                } else {
                    // it is single chat
                    intent.putExtra("userId", username);
                    intent.putExtra("messageInfo", new ExpandMsgInfo(messageInfo.getName(), messageInfo.getHeadImage(), username, null, null, null));
                }
                startActivity(intent);
            }
        }
    }
}
