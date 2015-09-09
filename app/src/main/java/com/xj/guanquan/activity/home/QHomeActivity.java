package com.xj.guanquan.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.xj.guanquan.R;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.fragment.contact.QContactFragment;
import com.xj.guanquan.fragment.found.QFindUserFragment;
import com.xj.guanquan.fragment.message.QMessageFragment;
import com.xj.guanquan.fragment.roast.TucaoMianFrg;
import com.xj.guanquan.fragment.user.QMeFragment;

import common.eric.com.ebaselibrary.util.FragmentManagerUtil;

public class QHomeActivity extends QBaseActivity implements OnClickListener, EMEventListener {

    private LinearLayout replaceFragment;
    private RadioButton radioBtnfind;
    private RadioButton raidoBtnshits;
    private RadioButton radioBtncontact;
    private RadioButton radioBtnmessage;
    private RadioButton radioBtnme;
    private RadioGroup homeGroup;
    private long mLastExitTime;
    private TextView unreadLabel;
    private int checkId = 0;
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qhome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{
                        EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventConversationListChanged}
        );
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }


    @Override
    protected void initView() {

        initialize();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) unreadLabel.getLayoutParams();
        layoutParams.leftMargin = screenWidth * 27 / 50;
        unreadLabel.setLayoutParams(layoutParams);

        initFragment(QFindUserFragment.newInstance(null, null));
        checkId = radioBtnfind.getId();
        homeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioBtnfind.getId()) {
                    initFragment(QFindUserFragment.newInstance(null, null));
                } else if (checkedId == radioBtncontact.getId()) {
                    initFragment(QContactFragment.newInstance());
                } else if (checkedId == radioBtnme.getId()) {
                    initFragment(QMeFragment.newInstance(null, null));
                } else if (checkedId == radioBtnmessage.getId()) {
                    initFragment(QMessageFragment.newInstance(null, null));
                } else if (checkedId == raidoBtnshits.getId()) {
                    initFragment(TucaoMianFrg.newInstance());
                }
                checkId = checkedId;
            }
        });

    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {

    }

    public void initFragment(Fragment fragment) {
        initTitle(fragment);
        currentFragment = fragment;
        FragmentManagerUtil.newInstance().replaceFragment(getSupportFragmentManager(), fragment, R.id.replaceFragment);
    }

    private void initialize() {
        replaceFragment = (LinearLayout) findViewById(R.id.replaceFragment);
        radioBtnfind = (RadioButton) findViewById(R.id.radioBtn_find);
        raidoBtnshits = (RadioButton) findViewById(R.id.raidoBtn_shits);
        radioBtncontact = (RadioButton) findViewById(R.id.radioBtn_contact);
        radioBtnmessage = (RadioButton) findViewById(R.id.radioBtn_message);
        radioBtnme = (RadioButton) findViewById(R.id.radioBtn_me);
        homeGroup = (RadioGroup) findViewById(R.id.homeGroup);
        unreadLabel = (TextView) findViewById(R.id.message_person);
    }

    public void initTitle(Fragment fragment) {
        //因为具体每个界面的导航栏都不一样，所以就隐藏掉activity的导航，直接在fragment
        //定义导航栏 add by jixiangxiang
        _setHeaderGone();
    }


    @Override
    public void onBackPressed() {
        alertConfirmDialog(getString(R.string.exit_app), new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        }, null);
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        switch (emNotifierEvent.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                // 提示新消息
                //HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                if (checkId == radioBtnmessage.getId()) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    QMessageFragment messageFragment = (QMessageFragment) currentFragment;
                    if (messageFragment != null) {
                        messageFragment.refresh();
                    }
                }
            }
        });
    }
}
