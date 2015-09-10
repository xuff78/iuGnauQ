package com.xj.guanquan.activity.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QUserDetailActivity;
import com.xj.guanquan.adapter.MessageAdapter;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.MessageInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.util.StringUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class QMsgDetailActivity extends QBaseActivity implements View.OnClickListener, EMEventListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<MessageInfo> datalist = new ArrayList<MessageInfo>();
    private EditText msgEdt;
    private MessageAdapter adapter;

    private static final String TAG = "QMsgDetailActivity";
    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;
    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    private int chatType;
    private String toChatUsername;
    private EMConversation conversation;
    private int pagesize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmsg_detail);
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck});
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }

    private void initData() {
        if (chatType == CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
        } else {
            toChatUsername = getIntent().getStringExtra("groupId");

            if (chatType == CHATTYPE_GROUP) {
            }
        }
        onConversationInit();
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        mRecyclerView.setAdapter(adapter);
        adapter.refreshSelectLast();
        msgEdt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        msgEdt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        msgEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String msg = msgEdt.getText().toString();
                    if (!StringUtils.isEmpty(msg.trim())) {
                        sendText(msg);
                    } else
                        ToastUtils.show(QMsgDetailActivity.this, "请输入内容");
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }


    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qmsg_detail));
        _setRightHomeGone();
        _setRightHomeText("个人资料", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //UserInfo userInfo = new UserInfo("孔先生", "http://www.feizl.com/upload2007/2014_09/14090118321004.jpg", " ♂ ", 23, "87kg", "183cm", "奥迪A8L 2014豪华版", "爱风尚音乐会");
                //bundle.putSerializable("userInfo", userInfo);
                toActivity(QUserDetailActivity.class, bundle);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.messageList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        msgEdt = (EditText) findViewById(R.id.msgEdt);

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    protected void onConversationInit() {
        if (chatType == CHATTYPE_SINGLE) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUsername, EMConversation.EMConversationType.Chat);
        } else if (chatType == CHATTYPE_GROUP) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUsername, EMConversation.EMConversationType.GroupChat);
        } else if (chatType == CHATTYPE_CHATROOM) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUsername, EMConversation.EMConversationType.ChatRoom);
        }

        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();

        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * 事件监听
     * <p/>
     * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();

                String username = null;
                //群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    //单聊消息
                    username = message.getFrom();
                }

                //如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUsername)) {
                    refreshUIWithNewMessage();
                    //声音和震动提示有新消息
                    //HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
                } else {
                    //如果消息不是和当前聊天ID的消息
                    //HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                }

                break;
            }
            case EventDeliveryAck: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();
                refreshUI();
                break;
            }
            case EventReadAck: {
                //获取到message
                EMMessage message = (EMMessage) event.getData();
                refreshUI();
                break;
            }
            case EventOfflineMessage: {
                //a list of offline messages
                //List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
                refreshUI();
                break;
            }
            default:
                break;
        }

    }

    private void refreshUIWithNewMessage() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
    }

    private void refreshUI() {
        if (adapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.refresh();
            }
        });
    }

    /**
     * 发送文本消息
     *
     * @param content message content
     */
    public void sendText(String content) {

        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) {
                message.setChatType(EMMessage.ChatType.GroupChat);
            } else if (chatType == CHATTYPE_CHATROOM) {
                message.setChatType(EMMessage.ChatType.ChatRoom);
            }
            //conversation = EMChatManager.getInstance().getConversation(toChatUsername);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            //发送消息
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refreshSelectLast();
            msgEdt.setText("");
            setResult(RESULT_OK);

        }
    }
}
