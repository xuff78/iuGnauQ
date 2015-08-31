package com.xj.guanquan.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.model.MessageInfo;

import java.util.ArrayList;

/**
 * Created by 可爱的蘑菇 on 2015/8/29.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity act;
    ArrayList<MessageInfo> datalist = new ArrayList<MessageInfo>();
    int width = 0;
    TextView footer;

    public int getItemCount() {
        return datalist.size() + 1;
    }

    public int getItemViewType(int position) {
        return position == this.getItemCount() - 1 ? -1 : position;
    }

    LayoutInflater listInflater;

    public MessageAdapter(Activity act, ArrayList<MessageInfo> datalist) {
        this.act = act;
        listInflater = LayoutInflater.from(act);
        this.datalist = datalist;

        WindowManager wm = act.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
//        this.imgWidth=(ConstantUtil.getWidth(act)-ImageUtil.dip2px(act, 30))/2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        RecyclerView.ViewHolder holder = null;
        if (position == -1) {
            footer = new TextView(act);
            footer.setGravity(Gravity.CENTER_HORIZONTAL);
            footer.setPadding(0, 20, 0, 0);
//			footer.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ImageUtil.dip2px(act,90)));
            holder = new FooterViewHolder(footer);
        } else {
            View v=null;
            if(datalist.get(position).getName().equals("我")) {
                v = listInflater.from(act).inflate(R.layout.listitem_message_right_layout, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new MessageRightHolder(v);
            }else {
                v = listInflater.from(act).inflate(R.layout.listitem_message_left_layout, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                holder = new MessageLeftHolder(v);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof FooterViewHolder) {
            return;
        } else {
            if(datalist.get(position).getName().equals("我")) {
                MessageRightHolder vh = (MessageRightHolder)viewHolder;
                vh.userMsg.setText(datalist.get(position).getLastMsg());
            }else {
                MessageLeftHolder vh = (MessageLeftHolder)viewHolder;
                vh.userMsg.setText(datalist.get(position).getLastMsg());
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

    public class MessageLeftHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userMsg;

        public MessageLeftHolder(View itemView) {
            super(itemView);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            userMsg = (TextView) itemView.findViewById(R.id.userMsg);
        }
    }

    public class MessageRightHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userMsg;

        public MessageRightHolder(View itemView) {
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

}
