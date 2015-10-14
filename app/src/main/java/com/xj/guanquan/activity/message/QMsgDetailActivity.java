package com.xj.guanquan.activity.message;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.xj.guanquan.R;
import com.xj.guanquan.adapter.ExpressionAdapter;
import com.xj.guanquan.adapter.ExpressionPagerAdapter;
import com.xj.guanquan.adapter.MessageAdapter;
import com.xj.guanquan.adapter.VoicePlayClickListener;
import com.xj.guanquan.common.GroupRemoveListener;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.SmileUtils;
import com.xj.guanquan.model.CircleInfo;
import com.xj.guanquan.model.ExpandMsgInfo;
import com.xj.guanquan.model.MessageInfo;
import com.xj.guanquan.model.UserInfo;
import com.xj.guanquan.views.ExpandGridView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
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
    private InputMethodManager manager;
    public String playMsgId;

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
    private Drawable[] micImages;
    private List<String> reslist;


    private Button btnsetmodevoice;
    private Button btnsetmodekeyboard;
    private LinearLayout btnpresstospeak;
    private ImageView ivemoticonsnormal;
    private ImageView ivemoticonschecked;
    private RelativeLayout edittextlayout;
    private Button btnsend;
    private LinearLayout rlbottom;
    private ViewPager vPager;
    private LinearLayout llfacecontainer;
    private ImageView btntakepicture;
    private ImageView btnpicture;
    private ImageView btnlocation;
    private ImageView btnvideo;
    private ImageView btnfile;
    private ImageView btnvoicecall;
    private LinearLayout containervoicecall;
    private ImageView btnvideocall;
    private LinearLayout containervideocall;
    private LinearLayout llbtncontainer;
    private Button btnmore, speakBtn;
    private LinearLayout more;
    private LinearLayout barbottom;
    private File cameraFile;

    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private PowerManager.WakeLock wakeLock;
    public boolean isRobot = false;
    private String cameraFileName = "camera_huanxin";

    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };
    private VoiceRecorder voiceRecorder;
    private ClipboardManager clipboard;
    private UserInfo userInfo;
    private ExpandMsgInfo fromUser;
    public EMGroup group;
    private GroupListener groupListener;
    private CircleInfo groupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmsg_detail);
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);
        fromUser = (ExpandMsgInfo) getIntent().getSerializableExtra("messageInfo");
        groupInfo = (CircleInfo) getIntent().getSerializableExtra("groupInfo");
        JSONObject jsonObject = JSONObject.parseObject(PreferencesUtils.getString(this, "loginData"));
        userInfo = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserInfo.class);
        initData();
        initialize();
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

        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        if (chatType == CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
        } else {
            toChatUsername = getIntent().getStringExtra("groupId");

            if (chatType == CHATTYPE_GROUP) {
                onGroupViewCreation();
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
        msgEdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                more.setVisibility(View.GONE);
                ivemoticonsnormal.setVisibility(View.VISIBLE);
                ivemoticonschecked.setVisibility(View.INVISIBLE);
                llfacecontainer.setVisibility(View.GONE);
                llbtncontainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        msgEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnmore.setVisibility(View.GONE);
                    btnsend.setVisibility(View.VISIBLE);
                } else {
                    btnmore.setVisibility(View.VISIBLE);
                    btnsend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initialize();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                ivemoticonsnormal.setVisibility(View.VISIBLE);
                ivemoticonschecked.setVisibility(View.INVISIBLE);
                llfacecontainer.setVisibility(View.GONE);
                llbtncontainer.setVisibility(View.GONE);
                return false;
            }
        });

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == ivemoticonsnormal) {
            more.setVisibility(View.VISIBLE);
            ivemoticonsnormal.setVisibility(View.INVISIBLE);
            ivemoticonschecked.setVisibility(View.VISIBLE);
            llbtncontainer.setVisibility(View.GONE);
            llfacecontainer.setVisibility(View.VISIBLE);
        } else if (v == ivemoticonschecked) {
            ivemoticonsnormal.setVisibility(View.VISIBLE);
            ivemoticonschecked.setVisibility(View.INVISIBLE);
            llbtncontainer.setVisibility(View.VISIBLE);
            llfacecontainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        } else if (v == btnmore) {
            toggleMore(more);
        } else if (v == btnsend) {
            String s = msgEdt.getText().toString();
            sendText(s);
        } else if (v == btnsetmodevoice) {
            if (speakBtn.isShown()) {
                speakBtn.setVisibility(View.GONE);
            } else {
                speakBtn.setVisibility(View.VISIBLE);
            }
        } else if (v == btntakepicture) {
            selectPicFromCamera();// 点击照相图标
        } else if (v == btnpicture) {
            selectPicFromLocal(); // 点击图片图标
        }
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

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(QMsgDetailActivity.this, st4, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(QMsgDetailActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        String st1 = getResources().getString(R.string.Recording_without_permission);
                        String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
                        String st3 = getResources().getString(R.string.send_failure_please);
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
                                        Integer.toString(length), false);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(QMsgDetailActivity.this, st3, Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
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
            initExpandMsg(message);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            //发送消息
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("onSuccess","onSuccess");
                }

                @Override
                public void onError(int i, String s) {
                    Log.e("sendMessage onError", s);
                }

                @Override
                public void onProgress(int i, String s) {
                    Log.e("sendMessage onProgress", s);
                }
            });
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refreshSelectLast();
            msgEdt.setText("");
            setResult(RESULT_OK);

        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) {
                message.setChatType(EMMessage.ChatType.GroupChat);
            } else if (chatType == CHATTYPE_CHATROOM) {
                message.setChatType(EMMessage.ChatType.ChatRoom);
            }
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
            message.addBody(body);
            initExpandMsg(message);
            if (isRobot) {
                message.setAttribute("em_robot_message", true);
            }
            conversation.addMessage(message);
            adapter.refreshSelectLast();
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }

        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        initExpandMsg(message);
        if (isRobot) {
            message.setAttribute("em_robot_message", true);
        }
        conversation.addMessage(message);

        mRecyclerView.setAdapter(adapter);
        adapter.refreshSelectLast();
        setResult(RESULT_OK);
        // more(more);
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    private void initialize() {

        btnsetmodevoice = (Button) findViewById(R.id.btn_set_mode_voice);
        btnsetmodekeyboard = (Button) findViewById(R.id.btn_set_mode_keyboard);
        btnpresstospeak = (LinearLayout) findViewById(R.id.btn_press_to_speak);
        ivemoticonsnormal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        ivemoticonschecked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        edittextlayout = (RelativeLayout) findViewById(R.id.edittext_layout);
        speakBtn = (Button) findViewById(R.id.speakBtn);
        btnmore = (Button) findViewById(R.id.btn_more);
        btnsend = (Button) findViewById(R.id.btn_send);
        rlbottom = (LinearLayout) findViewById(R.id.rl_bottom);
        vPager = (ViewPager) findViewById(R.id.vPager);
        llfacecontainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btntakepicture = (ImageView) findViewById(R.id.btn_take_picture);
        btnpicture = (ImageView) findViewById(R.id.btn_picture);
        btnlocation = (ImageView) findViewById(R.id.btn_location);
//        btnvideo = (ImageView) findViewById(R.id.btn_video);
//        btnvideocall = (ImageView) findViewById(R.id.btn_video_call);
//        btnfile = (ImageView) findViewById(R.id.btn_file);
//        btnvoicecall = (ImageView) findViewById(R.id.btn_voice_call);
//        containervoicecall = (LinearLayout) findViewById(R.id.container_voice_call);
//        btnvideocall = (ImageView) findViewById(R.id.btn_video_call);
//        containervideocall = (LinearLayout) findViewById(R.id.container_video_call);
        llbtncontainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        btnmore = (Button) findViewById(R.id.btn_more);
        more = (LinearLayout) findViewById(R.id.more);
        barbottom = (LinearLayout) findViewById(R.id.bar_bottom);
        mRecyclerView = (RecyclerView) findViewById(R.id.messageList);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        msgEdt = (EditText) findViewById(R.id.msgEdt);

        btnsetmodevoice.setOnClickListener(this);
        ivemoticonsnormal.setOnClickListener(this);
        ivemoticonschecked.setOnClickListener(this);
        btnmore.setOnClickListener(this);
        speakBtn.setOnTouchListener(new PressToSpeakListen());

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.mipmap.record_animate_01),
                getResources().getDrawable(R.mipmap.record_animate_02),
                getResources().getDrawable(R.mipmap.record_animate_03),
                getResources().getDrawable(R.mipmap.record_animate_04),
                getResources().getDrawable(R.mipmap.record_animate_05),
                getResources().getDrawable(R.mipmap.record_animate_06),
                getResources().getDrawable(R.mipmap.record_animate_07),
                getResources().getDrawable(R.mipmap.record_animate_08),
                getResources().getDrawable(R.mipmap.record_animate_09),
                getResources().getDrawable(R.mipmap.record_animate_10),
                getResources().getDrawable(R.mipmap.record_animate_11),
                getResources().getDrawable(R.mipmap.record_animate_12),
                getResources().getDrawable(R.mipmap.record_animate_13),
                getResources().getDrawable(R.mipmap.record_animate_14)};

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        vPager.setAdapter(new ExpressionPagerAdapter(views));
        edittextlayout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        //buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (btnsetmodekeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.xj.guanquan.common.SmileUtils");
                            Field field = clz.getField(filename);
                            msgEdt.append(SmileUtils.getSmiledText(QMsgDetailActivity.this,
                                    (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(msgEdt.getText())) {

                                int selectionStart = msgEdt.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = msgEdt.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            msgEdt.getEditableText().delete(i, selectionStart);
                                        else
                                            msgEdt.getEditableText().delete(selectionStart - 1,
                                                    selectionStart);
                                    } else {
                                        msgEdt.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void toggleMore(View view) {
        if (more.getVisibility() == View.GONE) {
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            llbtncontainer.setVisibility(View.VISIBLE);
            llfacecontainer.setVisibility(View.GONE);
        } else {
            if (llfacecontainer.getVisibility() == View.VISIBLE) {
                llfacecontainer.setVisibility(View.GONE);
                llbtncontainer.setVisibility(View.VISIBLE);
                ivemoticonsnormal.setVisibility(View.VISIBLE);
                ivemoticonsnormal.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            String st = getResources().getString(R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, 0).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), cameraFileName
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendPicture(cameraFile.getAbsolutePath());
//            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
//
//                int duration = data.getIntExtra("dur", 0);
//                String videoPath = data.getStringExtra("path");
//                File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
//                Bitmap bitmap = null;
//                FileOutputStream fos = null;
//                try {
//                    if (!file.getParentFile().exists()) {
//                        file.getParentFile().mkdirs();
//                    }
//                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
//                    if (bitmap == null) {
//                        EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
//                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
//                    }
//                    fos = new FileOutputStream(file);
//
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        fos = null;
//                    }
//                    if (bitmap != null) {
//                        bitmap.recycle();
//                        bitmap = null;
//                    }
//
//                }
//                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }
//            else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
//                if (data != null) {
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        sendFile(uri);
//                    }
//                }

//            }else if (requestCode == REQUEST_CODE_MAP) { // 地图
//                double latitude = data.getDoubleExtra("latitude", 0);
//                double longitude = data.getDoubleExtra("longitude", 0);
//                String locationAddress = data.getStringExtra("address");
//                if (locationAddress != null && !locationAddress.equals("")) {
//                    toggleMore(more);
//                    sendLocationMsg(latitude, longitude, "", locationAddress);
//                } else {
//                    String st = getResources().getString(R.string.unable_to_get_loaction);
//                    Toast.makeText(this, st, 0).show();
//                }
//                // 重发消息
//            } else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
//                    || requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
//                    || requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
//                // 粘贴
//                if (!TextUtils.isEmpty(clipboard.getText())) {
//                    String pasteText = clipboard.getText().toString();
//                    if (pasteText.startsWith(COPY_IMAGE)) {
//                        // 把图片前缀去掉，还原成正常的path
//                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
//                    }
//
//                }
//            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
//                EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
//                addUserToBlacklist(deleteMsg.getFrom());
//            } else if (conversation.getMsgCount() > 0) {
//                adapter.refresh();
//                setResult(RESULT_OK);
//            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
//                adapter.refresh();
//            }
        }
    }

    private void initExpandMsg(EMMessage message) {
        if (chatType == CHATTYPE_SINGLE) {
            if (this.fromUser != null) {
                message.setAttribute("toUserInfo", JSONObject.toJSONString(fromUser));
            }
            ExpandMsgInfo toUser = new ExpandMsgInfo();
            if (userInfo != null) {
                toUser.setUserIcon(userInfo.getAvatar());
                toUser.setUserId(String.valueOf(userInfo.getUserId()));
                toUser.setUserName(userInfo.getNickName());
                message.setAttribute("fromUserInfo", JSONObject.toJSONString(toUser));
            }
        } else {
            ExpandMsgInfo toUser = new ExpandMsgInfo();
            if (userInfo != null) {
                toUser.setUserIcon(userInfo.getAvatar());
                toUser.setUserId(String.valueOf(userInfo.getUserId()));
                toUser.setUserName(userInfo.getNickName());
                message.setAttribute("fromUserInfo", JSONObject.toJSONString(toUser));
            }
            ExpandMsgInfo group = new ExpandMsgInfo();
            group.setGroupId(String.valueOf(groupInfo.getId()));
            group.setGroupIcon(groupInfo.getPicture() == null ? "" : groupInfo.getPicture());
            group.setGroupName(groupInfo.getName());
            message.setAttribute("groupInfo", JSONObject.toJSONString(group));
        }
    }

    protected void onGroupViewCreation() {
        group = EMGroupManager.getInstance().getGroup(toChatUsername);

        if (group != null) {
            setTitle(group.getGroupName());
        } else {
            setTitle(toChatUsername);
        }

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);
    }

    /**
     * 监测群组解散或者被T事件
     */
    class GroupListener extends GroupRemoveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {
                String st13 = getResources().getString(R.string.you_are_group);

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        showToastShort(st13);
                        finish();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            runOnUiThread(new Runnable() {
                String st14 = getResources().getString(R.string.the_current_group);

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        showToastShort(st14);
                        finish();
                    }
                }
            });
        }

    }
}
