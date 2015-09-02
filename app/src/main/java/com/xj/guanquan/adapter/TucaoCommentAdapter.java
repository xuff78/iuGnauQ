package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.fragment.roast.Photo9Layout;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.TucaoCommentInfo;

import java.util.ArrayList;

import common.eric.com.ebaselibrary.util.ScreenUtils;

/**
 * Created by 可爱的蘑菇 on 2015/9/2.
 */
public class TucaoCommentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity act;
    ArrayList<TucaoCommentInfo> datalist = new ArrayList<TucaoCommentInfo>();
    int width = 0;
    TextView footer;
    int PageType=0;
    NoteInfo note;

    public int getItemCount() {
        return datalist.size() + 2;
    }

    public int getItemViewType(int position) {
        return position == this.getItemCount() - 1 ? -1 : position;
    }

    LayoutInflater listInflater;

    public TucaoCommentAdapter(Activity act, ArrayList<TucaoCommentInfo> datalist, NoteInfo note, int PageType) {
        this.act = act;
        this.PageType=PageType;
        listInflater = LayoutInflater.from(act);
        this.datalist = datalist;
        this.note=note;
        WindowManager wm = act.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
//        this.imgWidth=(ConstantUtil.getWidth(act)-ImageUtil.dip2px(act, 30))/2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        RecyclerView.ViewHolder holder = null;
        if (position == -1) {
            footer = new TextView(act);
            footer.setTextColor(Color.WHITE);
            footer.setGravity(Gravity.CENTER);
            footer.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int)(ScreenUtils.dpToPxInt(act, 30))));
            holder = new FooterViewHolder(footer);
        } else {
            View v = listInflater.from(act).inflate(R.layout.tucao_item_detail, null);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            holder = new NoteHolder(v, position);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof FooterViewHolder) {
            return;
        } else {
            if (position == 0) {
                NoteHolder vh = (NoteHolder) viewHolder;
                vh.userImg.setImageURI(Uri.parse(note.getAvatar()));
                vh.createTime.setText(note.getTime());
                String sex="♂ ";
                if(note.getSex()!=1){
                    sex="♀ ";
                }
                vh.userAge.setText(sex+note.getAge());
                vh.usrComment.setText(note.getContent());
                vh.favorBtn.setText(note.getIsLike() + "");
                vh.replyNums.setText(note.getCommentNum() + "");
                vh.userName.setText(note.getNickName());
                if (note.getPicture().length() > 0) {
                    String[] urls = note.getPicture().split(",");
                    vh.photoLayout.removeAllViews();
                    vh.photoLayout.addView(new Photo9Layout(act, (int) (width - ScreenUtils.dpToPxInt(act, 90)), urls));
                }
                vh.bookBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(act, QPublishAct.class);
                        intent.putExtra("PageType", QPublishAct.TypeJoin);
                        act.startActivity(intent);
                    }
                });
            }else{
                TucaoCommentInfo info=datalist.get(position-1); //因为第一个是原帖
                NoteHolder vh = (NoteHolder) viewHolder;
                vh.userImg.setImageURI(Uri.parse(info.getAvatar()));
                vh.createTime.setText(info.getTime());
                String sex="♂ ";
                if(info.getSex()!=1){
                    sex="♀ ";
                }
                vh.userAge.setText(sex+info.getAge());
                vh.usrComment.setText(info.getContent());
                vh.userName.setText(info.getNickName());
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.favorBtn:
                    break;
                case R.id.replyNums:
                    break;
                case R.id.shareBtn:
                    break;
                case R.id.bookBtn:
                    break;
            }

        }
    };

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View v) {
            super(v);
        }
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView userImg;
        LinearLayout photoLayout;
        View bookBtn, dateExtraLayout;
        TextView userName, userAge, createTime, usrComment, favorBtn, replyNums, shareBtn;

        public NoteHolder(View itemView, int positon) {
            super(itemView);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.userImg);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userAge = (TextView) itemView.findViewById(R.id.userAge);
            createTime = (TextView) itemView.findViewById(R.id.createTime);
            usrComment = (TextView) itemView.findViewById(R.id.usrComment);
            favorBtn = (TextView) itemView.findViewById(R.id.favorBtn);

            if(positon==0) {
                favorBtn.setTag(positon);
                favorBtn.setOnClickListener(listener);
                replyNums = (TextView) itemView.findViewById(R.id.replyNums);
                replyNums.setTag(positon);
                replyNums.setOnClickListener(listener);
                shareBtn = (TextView) itemView.findViewById(R.id.shareBtn);
                shareBtn.setTag(positon);
                shareBtn.setOnClickListener(listener);
                photoLayout = (LinearLayout) itemView.findViewById(R.id.photoLayout);
                bookBtn = itemView.findViewById(R.id.bookBtn);
                bookBtn.setTag(positon);
                bookBtn.setOnClickListener(listener);
            }else{
//                itemView.findViewById(R.id.photoLayout).setVisibility(View.GONE);
                itemView.findViewById(R.id.actionLayout).setVisibility(View.GONE);
            }
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
