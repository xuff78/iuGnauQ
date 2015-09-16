package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.activity.message.QMsgDetailActivity;
import com.xj.guanquan.common.Constant;
import com.xj.guanquan.common.ImageCache;
import com.xj.guanquan.common.LoadImageTask;
import com.xj.guanquan.common.QShowBigImage;
import com.xj.guanquan.common.SmileUtils;
import com.xj.guanquan.model.MessageInfo;
import com.xj.guanquan.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import common.eric.com.ebaselibrary.util.PreferencesUtils;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "msg";

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 14;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 15;
    private static final int MESSAGE_TYPE_SENT_ROBOT_MENU = 16;
    private static final int MESSAGE_TYPE_RECV_ROBOT_MENU = 17;

    public static final String IMAGE_DIR = "chat/image/";
    public static final String VOICE_DIR = "chat/audio/";
    public static final String VIDEO_DIR = "chat/video";

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private Context context;
    private EMConversation conversation;
    Activity act;
    int width = 0;
    TextView footer;
    ArrayList<MessageInfo> datalist = new ArrayList<MessageInfo>();
    private String username;
    EMMessage[] messages = null;
    private Map<String, Timer> timers = new Hashtable<String, Timer>();
    private UserInfo userInfo;

    public MessageAdapter(Context context, String username, int chatType) {
        this.context = context;
        this.act = (Activity) context;
        listInflater = LayoutInflater.from(act);
        this.username = username;
        this.conversation = EMChatManager.getInstance().getConversation(username);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(PreferencesUtils.getString(act, "loginData"));
        userInfo = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserInfo.class);
    }

    Handler handler = new Handler() {
        private void refreshList() {
            // UI线程不能直接使用conversation.getAllMessages()
            // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
            messages = (EMMessage[]) conversation.getAllMessages().toArray(new EMMessage[conversation.getAllMessages().size()]);
            for (int i = 0; i < messages.length; i++) {
                // getMessage will set message as read status
                conversation.getMessage(i);
            }
            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (act instanceof QMsgDetailActivity) {
                        LinearLayoutManager listView = ((QMsgDetailActivity) act).getLayoutManager();
                        if (messages.length > 0) {
                            listView.scrollToPositionWithOffset(messages.length - 1, 0);
                        }
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    if (act instanceof QMsgDetailActivity) {
                        LinearLayoutManager listView = ((QMsgDetailActivity) act).getLayoutManager();
                        listView.scrollToPositionWithOffset(position, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public int getItemCount() {
        return messages == null ? 1 : messages.length + 1;
    }

    public int getItemViewType(int position) {
        if (position == this.getItemCount() - 1) {
            return -1;
        } else {
            EMMessage message = messages[position];
            if (message == null) {
                return -1;
            }
            if (message.getType() == EMMessage.Type.TXT) {
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false))
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                else
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
            }
            if (message.getType() == EMMessage.Type.IMAGE) {
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

            }
            if (message.getType() == EMMessage.Type.LOCATION) {
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
            }
            if (message.getType() == EMMessage.Type.VOICE) {
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
            }
            if (message.getType() == EMMessage.Type.VIDEO) {
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
            }
            if (message.getType() == EMMessage.Type.FILE) {
                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
            }
        }
        return -1;
    }

    LayoutInflater listInflater;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        switch (viewType) {
            case MESSAGE_TYPE_RECV_TXT:
                v = listInflater.from(act).inflate(R.layout.listitem_message_left_layout, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new MessageHolder(v);
                break;
            case MESSAGE_TYPE_SENT_TXT:
                v = listInflater.from(act).inflate(R.layout.listitem_message_right_layout, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new MessageHolder(v);
                break;
            case MESSAGE_TYPE_SENT_VOICE:
                v = listInflater.from(act).inflate(R.layout.row_sent_voice, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new VoiceHolder(v);
                break;
            case MESSAGE_TYPE_RECV_VOICE:
                v = listInflater.from(act).inflate(R.layout.row_received_voice, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new VoiceHolder(v);
                break;
            case MESSAGE_TYPE_RECV_IMAGE:
                v = listInflater.from(act).inflate(R.layout.row_received_picture, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new ImageHolder(v);
                break;
            case MESSAGE_TYPE_SENT_IMAGE:
                v = listInflater.from(act).inflate(R.layout.row_sent_picture, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new ImageHolder(v);
                break;
            case -1:
                footer = new TextView(act);
                footer.setGravity(Gravity.CENTER_HORIZONTAL);
                footer.setPadding(0, 20, 0, 0);
                holder = new FooterViewHolder(footer);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof FooterViewHolder) {
            return;
        } else {
            EMMessage message = messages[position];
            EMMessage.ChatType chatType = message.getChatType();
            if (message.getType() == EMMessage.Type.TXT) {
                MessageHolder vh = (MessageHolder) viewHolder;
                handleTextMessage(message, vh, position);
            } else if (message.getType() == EMMessage.Type.VOICE) {
                VoiceHolder vh = (VoiceHolder) viewHolder;
                handleVoiceMessage(message, vh, position);
            } else if (message.getType() == EMMessage.Type.IMAGE) {
                ImageHolder vh = (ImageHolder) viewHolder;
                handleImageMessage(message, vh, position);
            }

            TextView timestamp = (TextView) viewHolder.itemView.findViewById(R.id.timestamp);

            if (timestamp != null)
                if (position == 0) {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                } else {
                    // 两条消息时间离得如果稍长，显示时间
                    EMMessage prevMessage = getItem(position - 1);
                    if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                        timestamp.setVisibility(View.GONE);
                    } else {
                        timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                        timestamp.setVisibility(View.VISIBLE);
                    }
                }
        }
    }

    public EMMessage getItem(int position) {
        if (messages != null && position < messages.length) {
            return messages[position];
        }
        return null;
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View v) {
            super(v);
        }
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView userImg;
        TextView userMsg;

        public MessageHolder(View itemView) {
            super(itemView);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.userImg);
            userMsg = (TextView) itemView.findViewById(R.id.userMsg);
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar, staus_iv;
        SimpleDraweeView iv;
        TextView tv, tv_usernick;
        ProgressBar pb;

        public ImageHolder(View convertView) {
            super(convertView);
            try {
                iv = ((SimpleDraweeView) convertView.findViewById(R.id.iv_sendPicture));
                iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                tv = (TextView) convertView.findViewById(R.id.percentage);
                pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
                staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
            } catch (Exception e) {
            }
        }
    }

    public class VoiceHolder extends RecyclerView.ViewHolder {
        ImageView iv, staus_iv, iv_read_status;
        SimpleDraweeView iv_avatar;
        TextView tv, tv_usernick;
        ProgressBar pb;

        public VoiceHolder(View convertView) {
            super(convertView);
            iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
            iv_avatar = (SimpleDraweeView) convertView.findViewById(R.id.iv_userhead);
            tv = (TextView) convertView.findViewById(R.id.tv_length);
            pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
            tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
            iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
        }
    }

    public void isLoadMore(boolean isMore) {
        if (this.footer != null) {
            if (isMore) {
                this.footer.setText("正在加载中");
            } else {
                this.footer.setText("全部加载完成");
            }
        }

    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
    }

    /**
     * 刷新页面, 选择Position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    /**
     * 文本消息
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleTextMessage(EMMessage message, MessageHolder holder, final int position) {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        holder.userMsg.setText(span, TextView.BufferType.SPANNABLE);
        Uri uri = null;
        try {
            JSONObject jsonObject = message.getJSONObjectAttribute("fromUserInfo");
            uri = Uri.parse(jsonObject.getString("userIcon"));
            holder.userImg.setImageURI(uri);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
//        if (message.direct == EMMessage.Direct.SEND) {
//            switch (message.status) {
//                case SUCCESS: // 发送成功
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.GONE);
//                    break;
//                case FAIL: // 发送失败
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.VISIBLE);
//                    break;
//                case INPROGRESS: // 发送中
//                    holder.pb.setVisibility(View.VISIBLE);
//                    holder.staus_iv.setVisibility(View.GONE);
//                    break;
//                default:
//                    // 发送消息
//                    sendMsgInBackground(message, holder);
//            }
//        }
    }

    /**
     * 图片消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleImageMessage(final EMMessage message, final ImageHolder holder, final int position) {
        holder.pb.setTag(position);
        Uri uri = null;
        try {
            JSONObject jsonObject = message.getJSONObjectAttribute("fromUserInfo");
            uri = Uri.parse(jsonObject.getString("userIcon"));
            holder.iv_avatar.setImageURI(uri);
        } catch (Exception e) {
            Log.e("handleImageMessage", e.getMessage());
        }
        holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                act.startActivityForResult(
                        (new Intent(act, ContextMenu.class)).putExtra("position", position).putExtra("type",
                                EMMessage.Type.IMAGE.ordinal()), QMsgDetailActivity.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });

        // 接收方向的消息
        if (message.direct == EMMessage.Direct.RECEIVE) {
            // "it is receive msg";
            if (message.status == EMMessage.Status.INPROGRESS) {
                // "!!!! back receive";
                holder.iv.setImageResource(R.mipmap.default_image);
                showDownloadImageProgress(message, holder);
                // downloadImage(message, holder);
            } else {
                // "!!!! not back receive, show image directly");
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.iv.setImageResource(R.mipmap.default_image);
                ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
                if (imgBody.getLocalUrl() != null) {
                    // String filePath = imgBody.getLocalUrl();
                    String remotePath = imgBody.getRemoteUrl();
                    String filePath = ImageUtils.getImagePath(remotePath);
                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
                    if (TextUtils.isEmpty(thumbRemoteUrl) && !TextUtils.isEmpty(remotePath)) {
                        thumbRemoteUrl = remotePath;
                    }
                    String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
                    showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
                }
            }
            return;
        }

        // 发送的消息
        // process send message
        // send pic, show the pic directly
        ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
        String filePath = imgBody.getLocalUrl();
        if (filePath != null && new File(filePath).exists()) {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message);
        } else {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message);
        }

        switch (message.status) {
            case SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
                break;
            case FAIL:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                holder.staus_iv.setVisibility(View.GONE);
                holder.pb.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.VISIBLE);
                if (timers.containsKey(message.getMsgId()))
                    return;
                // set a timer
                final Timer timer = new Timer();
                timers.put(message.getMsgId(), timer);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        act.runOnUiThread(new Runnable() {
                            public void run() {
                                holder.pb.setVisibility(View.VISIBLE);
                                holder.tv.setVisibility(View.VISIBLE);
                                holder.tv.setText(message.progress + "%");
                                if (message.status == EMMessage.Status.SUCCESS) {
                                    holder.pb.setVisibility(View.GONE);
                                    holder.tv.setVisibility(View.GONE);
                                    // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
                                    timer.cancel();
                                } else if (message.status == EMMessage.Status.FAIL) {
                                    holder.pb.setVisibility(View.GONE);
                                    holder.tv.setVisibility(View.GONE);
                                    // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
                                    // message.setProgress(0);
                                    holder.staus_iv.setVisibility(View.VISIBLE);
                                    Toast.makeText(act,
                                            act.getString(R.string.send_fail) + act.getString(R.string.connect_failuer_toast), 0)
                                            .show();
                                    timer.cancel();
                                }

                            }
                        });

                    }
                }, 0, 500);
                break;
            default:
                sendPictureMessage(message, holder);
        }
    }

    /**
     * 语音消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */

    private void handleVoiceMessage(final EMMessage message, final VoiceHolder holder, final int position) {
        VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if (len > 0) {
            holder.tv.setText(voiceBody.getLength() + "\"");
            holder.tv.setVisibility(View.VISIBLE);
        } else {
            holder.tv.setVisibility(View.INVISIBLE);
        }
        Uri uri = null;
        try {
            JSONObject jsonObject = message.getJSONObjectAttribute("fromUserInfo");
            uri = Uri.parse(jsonObject.getString("userIcon"));
            holder.iv_avatar.setImageURI(uri);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this, act, username));
        holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                act.startActivityForResult(
                        (new Intent(act, ContextMenu.class)).putExtra("position", position).putExtra("type",
                                EMMessage.Type.VOICE.ordinal()), QMsgDetailActivity.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });
        if (((QMsgDetailActivity) act).playMsgId != null
                && ((QMsgDetailActivity) act).playMsgId.equals(message
                .getMsgId()) && VoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct == EMMessage.Direct.RECEIVE) {
                holder.iv.setImageResource(R.anim.voice_from_icon);
            } else {
                holder.iv.setImageResource(R.anim.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct == EMMessage.Direct.RECEIVE) {
                holder.iv.setImageResource(R.mipmap.chatfrom_voice_playing);
            } else {
                holder.iv.setImageResource(R.mipmap.chatto_voice_playing);
            }
        }


        if (message.direct == EMMessage.Direct.RECEIVE) {
            if (message.isListened()) {
                // 隐藏语音未听标志
                holder.iv_read_status.setVisibility(View.INVISIBLE);
            } else {
                holder.iv_read_status.setVisibility(View.VISIBLE);
            }
            EMLog.d(TAG, "it is receive msg");
            if (message.status == EMMessage.Status.INPROGRESS) {
                holder.pb.setVisibility(View.VISIBLE);
                EMLog.d(TAG, "!!!! back receive");
                ((FileMessageBody) message.getBody()).setDownloadCallback(new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                holder.pb.setVisibility(View.INVISIBLE);
                                notifyDataSetChanged();
                            }
                        });

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                holder.pb.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                });

            } else {
                holder.pb.setVisibility(View.INVISIBLE);

            }
            return;
        }

        // until here, deal with send voice msg
        switch (message.status) {
            case SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
                break;
            case FAIL:
                holder.pb.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                holder.pb.setVisibility(View.VISIBLE);
                holder.staus_iv.setVisibility(View.GONE);
                break;
            default:
                sendMsgInBackground(message, holder);
        }
    }

    private void sendPictureMessage(final EMMessage message, final ImageHolder holder) {
        try {
            String to = message.getTo();

            // before send, update ui
            holder.staus_iv.setVisibility(View.GONE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText("0%");

            final long start = System.currentTimeMillis();
            // if (chatType == ChatActivity.CHATTYPE_SINGLE) {
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "send image message successfully");
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            // send success
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {

                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                            // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
                            holder.staus_iv.setVisibility(View.VISIBLE);
                            Toast.makeText(act,
                                    act.getString(R.string.send_fail) + act.getString(R.string.connect_failuer_toast), 0).show();
                        }
                    });
                }

                @Override
                public void onProgress(final int progress, String status) {
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            holder.tv.setText(progress + "%");
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDownloadImageProgress(final EMMessage message, final ImageHolder holder) {
        EMLog.d(TAG, "!!! show download image progress");
        // final ImageMessageBody msgbody = (ImageMessageBody)
        // message.getBody();
        final FileMessageBody msgbody = (FileMessageBody) message.getBody();
        if (holder.pb != null)
            holder.pb.setVisibility(View.VISIBLE);
        if (holder.tv != null)
            holder.tv.setVisibility(View.VISIBLE);

        msgbody.setDownloadCallback(new EMCallBack() {

            @Override
            public void onSuccess() {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // message.setBackReceive(false);
                        if (message.getType() == EMMessage.Type.IMAGE) {
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                        }
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onProgress(final int progress, String status) {
                if (message.getType() == EMMessage.Type.IMAGE) {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.tv.setText(progress + "%");

                        }
                    });
                }

            }

        });
    }

    /**
     * 发送消息
     *
     * @param message
     * @param holder
     * @param position
     */
    public void sendMsgInBackground(final EMMessage message, final VoiceHolder holder) {
        holder.staus_iv.setVisibility(View.GONE);
        holder.pb.setVisibility(View.VISIBLE);

        final long start = System.currentTimeMillis();
        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {

                updateSendedView(message, holder);
            }

            @Override
            public void onError(int code, String error) {

                updateSendedView(message, holder);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });

    }

    /**
     * 更新ui上消息发送状态
     *
     * @param message
     * @param holder
     */
    private void updateSendedView(final EMMessage message, final VoiceHolder holder) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // send success
                if (message.getType() == EMMessage.Type.VIDEO) {
                    holder.tv.setVisibility(View.GONE);
                }
                EMLog.d(TAG, "message status : " + message.status);
                if (message.status == EMMessage.Status.SUCCESS) {
                    // if (message.getType() == EMMessage.Type.FILE) {
                    // holder.pb.setVisibility(View.INVISIBLE);
                    // holder.staus_iv.setVisibility(View.INVISIBLE);
                    // } else {
                    // holder.pb.setVisibility(View.GONE);
                    // holder.staus_iv.setVisibility(View.GONE);
                    // }

                } else if (message.status == EMMessage.Status.FAIL) {
                    // if (message.getType() == EMMessage.Type.FILE) {
                    // holder.pb.setVisibility(View.INVISIBLE);
                    // } else {
                    // holder.pb.setVisibility(View.GONE);
                    // }
                    // holder.staus_iv.setVisibility(View.VISIBLE);

                    if (message.getError() == EMError.MESSAGE_SEND_INVALID_CONTENT) {
                        Toast.makeText(act, act.getString(R.string.send_fail) + act.getString(R.string.error_send_invalid_content), 0)
                                .show();
                    } else if (message.getError() == EMError.MESSAGE_SEND_NOT_IN_THE_GROUP) {
                        Toast.makeText(act, act.getString(R.string.send_fail) + act.getString(R.string.error_send_not_in_the_group), 0)
                                .show();
                    } else {
                        Toast.makeText(act, act.getString(R.string.send_fail) + act.getString(R.string.connect_failuer_toast), 0)
                                .show();
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @param position
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
                                  final EMMessage message) {
        final String remote = remoteDir;
        EMLog.d("###", "local = " + localFullSizePath + " remote: " + remote);
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            iv.setClickable(true);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EMLog.d(TAG, "image view on click");
                    Intent intent = new Intent(act, QShowBigImage.class);
                    File file = new File(localFullSizePath);
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        intent.putExtra("uri", uri);
                        EMLog.d(TAG, "here need to check why download everytime");
                    } else {
                        // The local full size pic does not exist yet.
                        // ShowBigImage needs to download it from the server
                        // first
                        // intent.putExtra("", message.get);
                        ImageMessageBody body = (ImageMessageBody) message.getBody();
                        intent.putExtra("secret", body.getSecret());
                        intent.putExtra("remotepath", remote);
                    }
                    if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
                            && message.getChatType() != EMMessage.ChatType.GroupChat && message.getChatType() != EMMessage.ChatType.ChatRoom) {
                        try {
                            EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                            message.isAcked = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    act.startActivity(intent);
                }
            });
            return true;
        } else {
            new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, act, message);
            return true;
        }

    }

}
