package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.message.QMsgDetailActivity;
import com.xj.guanquan.common.Constant;
import com.xj.guanquan.common.SmileUtils;
import com.xj.guanquan.model.MessageInfo;

import java.util.ArrayList;

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

    public MessageAdapter(Context context, String username, int chatType) {
        listInflater = LayoutInflater.from(act);
        this.context = context;
        this.username = username;
        this.act = (Activity) context;
        this.conversation = EMChatManager.getInstance().getConversation(username);
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
        return messages == null ? 0 : messages.length;
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
            }
        }
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
        ImageView userImg;
        TextView userMsg;

        public MessageHolder(View itemView) {
            super(itemView);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            userMsg = (TextView) itemView.findViewById(R.id.userMsg);
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

}
